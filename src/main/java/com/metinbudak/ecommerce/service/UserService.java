package com.metinbudak.ecommerce.service;

import com.metinbudak.ecommerce.dto.UserCreateDto;
import com.metinbudak.ecommerce.dto.UserReadDto;
import com.metinbudak.ecommerce.exception.RecordNotFoundException;
import com.metinbudak.ecommerce.exception.UsernameAlreadyExistsException;
import com.metinbudak.ecommerce.repository.UserRepository;
import com.metinbudak.ecommerce.repository.domain.User;
import com.metinbudak.ecommerce.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserReadDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByUsernameIgnoreCase(userCreateDto.getUsername())) {
            throw new UsernameAlreadyExistsException(userCreateDto.getUsername());
        }

        User user = User.regularUser(
                userCreateDto.getUsername(),
                passwordEncoder.encode(userCreateDto.getPassword()),
                userCreateDto.getFirstname(),
                userCreateDto.getLastname(),
                userCreateDto.getEmail()
        );

        user = userRepository.save(user);

        return this.mapToDto(user);
    }

    public UserReadDto getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.getByUsername(userDetails.getUsername());
        return this.mapToDto(user);
    }

    public String getAuthenticatedUserToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(userDetails);
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RecordNotFoundException("User with username '%s' doesn't exists".formatted(username)));
        userRepository.delete(user);
    }

    public List<UserReadDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDto).toList();
    }

    public UserReadDto mapToDto(User user) {
        return UserReadDto.builder()
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }
}
