package com.ecommerce.userauthenticationservice.models;

import com.ecommerce.userauthenticationservice.models.enums.SessionState;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Session extends BaseModel{
    private String token;
    @Enumerated(EnumType.ORDINAL)
    private SessionState sessionState;
    @ManyToOne
    private User user; // one user have many session like active, inactive. Many active session is also possible like pc , mobile, tablet sessions


}
