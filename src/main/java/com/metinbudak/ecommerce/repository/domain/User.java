package com.metinbudak.ecommerce.repository.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = true;

    @NotBlank
    @Column(nullable = false)
    private String firstname;

    @NotBlank
    @Column(nullable = false)
    private String lastname;

    @Email @NotBlank
    @Column(nullable = false)
    private String email;

    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    protected User() {
        // for JPA
    }

    protected User(String username, String password, boolean enabled, String firstname, String lastname, String email, Set<Authority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.authorities = authorities;
    }

    public static User regularUser(String username, String password, String firstname, String lastname, String email) {
        return new User(username, password, true, firstname, lastname, email, Set.of(Authority.userAuthority(username)));
    }

    public static User adminUser(String username, String password, String firstname, String lastname, String email) {
        return new User(username, password, true, firstname, lastname, email, Set.of(Authority.adminAuthority(username)));
    }

}