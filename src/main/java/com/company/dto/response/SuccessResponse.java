package com.company.dto.response;

public class SuccessResponse {

    public String code;
    public String message;
    public Object data;

    public SuccessResponse(String code,String message, Object data){
        this.code = code;
        this.message= message;
        this.data = data;
    }
}
