package com.hieunt.medlink.app.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordGenerator() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
