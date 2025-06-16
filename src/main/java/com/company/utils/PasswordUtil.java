package com.company.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class PasswordUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

// Generate hashed password
    public String hashPassword(String rawPassword) {
        logger.info("Generate hash password util method");
        try{
            return encoder.encode(rawPassword);
        } catch (RuntimeException e) {
            logger.error("Exception Occurred at generate hash password util method");
            throw new RuntimeException(e);
        }
    }

    // Verify password against stored hash
    public boolean matchPassword(String rawPassword, String hashedPassword) {
        logger.info("match password util method start");
        try{
        return encoder.matches(rawPassword, hashedPassword);
        } catch (RuntimeException e) {
            logger.error("Exception Occurred at match password util method");
            throw new RuntimeException(e);
        }
    }
}