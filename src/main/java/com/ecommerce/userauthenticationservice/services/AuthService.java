package com.ecommerce.userauthenticationservice.services;

import com.ecommerce.userauthenticationservice.Repositories.SessionRepository;
import com.ecommerce.userauthenticationservice.Repositories.UserRepository;
import com.ecommerce.userauthenticationservice.exceptions.InvalidLoginPassword;
import com.ecommerce.userauthenticationservice.exceptions.UserAlreadyExistsDuringSignUpException;
import com.ecommerce.userauthenticationservice.exceptions.UserNotExistDuringLoginException;
import com.ecommerce.userauthenticationservice.models.Session;
import com.ecommerce.userauthenticationservice.models.User;
import com.ecommerce.userauthenticationservice.models.enums.SessionState;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SecretKey secretKey;

    private static String secretKeyString = "secret1920";

    public User signUp(String email, String password){
        //Check user is present
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            throw new UserAlreadyExistsDuringSignUpException("User already present");
        }
        //create new user and save in db
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }

    public User loginByPassword(String email, String password){
        //validate email
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotExistDuringLoginException("User email is not valid");
        }
        //validate password
        if(!bCryptPasswordEncoder.matches(password, userOptional.get().getPassword())){
            throw new InvalidLoginPassword("Invalid Password");
        }
        return userOptional.get();
    }


    public Pair<User, MultiValueMap<String, String>> loginByPasswordAndJWT(String email, String password){
        //validate email
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotExistDuringLoginException("User email is not valid");
        }
        //validate password
        if(!bCryptPasswordEncoder.matches(password, userOptional.get().getPassword())){
            throw new InvalidLoginPassword("Invalid Password");
        }

        //generate JWT with Hardcoded data
//        String message = "{\n" +
//                "   \"email\": \"anurag@scaler.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"buddy\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"2ndJuly2024\"\n" +
//                "}";

        //User detail in JWT
        Map<String, Object> claim = new HashMap<>();
        claim.put("email", userOptional.get().getEmail());
        claim.put("roles", userOptional.get().getUserRoleSet());
        long nowInMillis = System.currentTimeMillis();
        claim.put("iat", nowInMillis); // issued At
        claim.put("expiry", nowInMillis+1000); // expiry time


        //Generate signature and algo

       // byte[] content = message.getBytes(StandardCharsets.UTF_8);
        //String token = Jwts.builder().content(content).compact(); //Hardedcoded content
        //String token = Jwts.builder().content(content).signWith(secretKey).compact(); //With signature and algo and hardcoded content
        String token = Jwts.builder().claims(claim).signWith(secretKey).compact(); // With user detail as claim and auto generated secret
        //String token = Jwts.builder().claims(claim).signWith(SignatureAlgorithm.HS256, secretKeyString).compact();// signature with user defined secret key
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);

        //Store session for session management and validation purpose for futher request
        Session session = new Session();
        session.setUser(userOptional.get());
        session.setSessionState(SessionState.ACTIVE);
        session.setToken(token);
        sessionRepository.save(session);


        return Pair.of(userOptional.get(), headers);
    }

    public Boolean validateToken(String token, Long userId){
        //check input token and user present in db
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_idAndSessionState(token, userId, SessionState.ACTIVE);
        if(sessionOptional.isEmpty()){
            throw new RuntimeException("Invalid token");
        }

        //check expiry of token
        //Decrypt signature
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        Long expiry = (Long)claims.get("expiry");
        Session  session = sessionOptional.get();
        Long nowInMillis = System.currentTimeMillis();
        if(expiry < nowInMillis){
            System.out.println("Expiry" + expiry);
            System.out.println("nowInMillis"+ nowInMillis);
            session.setSessionState(SessionState.IN_ACTIVE);
            sessionRepository.save(session);
            throw new RuntimeException("Token Expired: Please login again");
        }

        //validate email
        Optional<User> userOptional = userRepository.findById(userId);
        if(!claims.get("email").equals(userOptional.get().getEmail())){
            session.setSessionState(SessionState.IN_ACTIVE);
            sessionRepository.save(session);
            throw  new RuntimeException("Email not matched: Please login again");
        }

        return true;
    }
}
