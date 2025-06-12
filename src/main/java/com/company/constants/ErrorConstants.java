package com.company.constants;

import com.company.dto.response.ErrorResponse;

public class ErrorConstants {

    public static final ErrorResponse INTERNAL_SERVER_ERROR = new ErrorResponse("Internal Server Error","INT-500");
    public static final ErrorResponse BAD_REQUEST = new ErrorResponse("Bad Request", "BAD-400");
    public static final ErrorResponse USER_NOT_FOUND = new ErrorResponse("User not found","NOT-400");
}
