package com.company.constants;

import com.company.dto.response.ErrorResponse;
import lombok.Data;

@Data
public class ErrorConstants {

    public static final ErrorResponse INTERNAL_SERVER_ERROR = new ErrorResponse("Internal Server Error","INT-500");
    public static final ErrorResponse BAD_REQUEST = new ErrorResponse("Bad Request", "BAD-400");
    public static final ErrorResponse USER_NOT_FOUND = new ErrorResponse("User not found","NOT-404");
    public static final ErrorResponse PROBLEM_NOT_FOUND = new ErrorResponse("Problem Not Found","NOT-404");
    public static final ErrorResponse EMAIL_ALREADY_EXISTS = new ErrorResponse("Email Already Exists","BAD-400");
    public static final ErrorResponse EMAIL_NOT_FOUND = new ErrorResponse("Email Not Found","NOT-404");
    public static final ErrorResponse OTP_NOT_MATCH = new ErrorResponse("Invalid Otp","BAD-400");
    public static final ErrorResponse EMAIL_IS_REQUIRED = new ErrorResponse("Email is Required","BAD-400");
}
