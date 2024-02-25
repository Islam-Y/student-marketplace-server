package com.startit.itemservice.entity;

import com.startit.shared.transfer.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users_item_ms")
public class UserEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String familyName;

    @Column(name = "isu_number", nullable = false, unique = true)
    private Integer isuNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
