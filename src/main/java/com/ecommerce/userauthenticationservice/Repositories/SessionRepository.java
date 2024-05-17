package com.ecommerce.userauthenticationservice.Repositories;

import com.ecommerce.userauthenticationservice.models.Session;
import com.ecommerce.userauthenticationservice.models.enums.SessionState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByTokenAndUser_idAndSessionState(String token, Long userId, SessionState sessionState);
}
