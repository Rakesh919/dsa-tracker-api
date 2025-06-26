package com.company.constants;

import com.company.dto.response.ErrorResponse;
import lombok.Data;

@Data
public class ErrorConstants {

    public static final ErrorResponse INTERNAL_SERVER_ERROR = new ErrorResponse("Internal Server Error","INT-500");
    public static final ErrorResponse BAD_REQUEST = new ErrorResponse("Bad Request", "BAD-400");
    public static final ErrorResponse USER_NOT_FOUND = new ErrorResponse("User not found","NOT-404");
    public static final ErrorResponse PROBLEM_NOT_FOUND = new ErrorResponse("Problem Not Found","NOT-404");
    public static final ErrorResponse EMAIL_ALREADY_EXISTS = new ErrorResponse("Email Already Exists, Please use a different Email","BAD-400");
    public static final ErrorResponse EMAIL_NOT_FOUND = new ErrorResponse("Email Not Found","NOT-404");
    public static final ErrorResponse OTP_NOT_MATCH = new ErrorResponse("Invalid Otp","BAD-400");
    public static final ErrorResponse EMAIL_IS_REQUIRED = new ErrorResponse("Email is Required","BAD-400");
    public static final ErrorResponse TOKEN_IS_REQUIRED = new ErrorResponse("Invalid or Missing Token","BAD-400");
    public static final ErrorResponse NO_PROBLEM_FOUND = new ErrorResponse("No Problem Found for this user","NOT-404");
    public static final ErrorResponse PASSWORD_IS_REQUIRED = new ErrorResponse("Password is Required","BAD-400");
    public static final ErrorResponse NO_RECORD_FOUND_FOR_EMAIL = new ErrorResponse("No Record Found For this Email","NOT-404");
    public static final ErrorResponse PASSWORD_DO_NOT_MATCH = new ErrorResponse("Password do not match for this Email","NOT-404");
    public static final ErrorResponse UNABLE_TO_DELETE_THE_PROBLEM = new ErrorResponse("Unable to delete the Problem","BAD-400");
}
