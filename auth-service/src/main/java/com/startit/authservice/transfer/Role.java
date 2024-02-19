package com.startit.authservice.transfer;

import org.springframework.security.core.GrantedAuthority;

public enum Role {

    CUSTOMER,
    SELLER,
    SUPERUSER

    ;

    public GrantedAuthority getAuthority() {
        return this::name;
    }
}
