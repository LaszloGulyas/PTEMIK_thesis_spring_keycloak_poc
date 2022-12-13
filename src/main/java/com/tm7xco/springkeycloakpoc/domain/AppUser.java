package com.tm7xco.springkeycloakpoc.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;


@Entity
@Data
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "user_name", unique = true)
    private String username;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_email")
    private String email;

}
