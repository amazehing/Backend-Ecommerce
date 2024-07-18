package com.metinbudak.ecommerce.controller;

import com.metinbudak.ecommerce.dto.UserCreateDto;
import com.metinbudak.ecommerce.dto.UserReadDto;
import com.metinbudak.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/users/me")
                .build()
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserReadDto> getUser() {
        return ResponseEntity.ok(userService.getAuthenticatedUser());
    }

    @GetMapping("/users/me/token")
    public ResponseEntity<String> getBearerToken() {
        return ResponseEntity.ok(userService.getAuthenticatedUserToken());
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserReadDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
