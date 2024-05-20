package com.startit.userservice.entity;

import com.startit.shared.transfer.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String familyName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
