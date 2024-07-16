package com.metinbudak.ecommerce.repository.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "authorities")
public class Authority implements Serializable {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Id
    @Column(nullable = false)
    private String username;

    @Id
    @Column(nullable = false)
    private String authority;

    protected Authority() {
        // for JPA
    }

    protected Authority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public static Authority userAuthority(String username) {
        return new Authority(username, Authority.ROLE_USER);
    }

    public static Authority adminAuthority(String username) {
        return new Authority(username, Authority.ROLE_ADMIN);
    }

}