package com.company.dto.request;

import com.company.validation.annotation.ValidPassword;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

    @NotNull(message = "Name can't be empty")
    private String name;

    private int score;

    @NotNull(message = "Email can't be null")
    @Column(unique = true,nullable = false)
    @Email(message = "Please enter a valid Email")
    private String email;

    @NotNull(message = "Password can't be null")
    @ValidPassword
    private String password;

    @NotNull(message = "Otp is required")
    private int otp;
}
