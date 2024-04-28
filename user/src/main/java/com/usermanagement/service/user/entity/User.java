package com.usermanagement.service.user.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;


@Data
@Entity
@Table(name = "users",uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
}
