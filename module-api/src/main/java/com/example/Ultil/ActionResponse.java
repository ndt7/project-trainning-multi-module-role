package com.example.Ultil;


import com.example.dto.ApiResponseDto;

public final class ActionResponse {
    private ActionResponse() {
        //Not called
    }

    public static ApiResponseDto actionSuccess(Object T, String method) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setMessage(method + GlobalVariable.ACTION_SUCCESS);
        apiResponseDto.setStatus(GlobalVariable.STRING_SUCCESS);
        apiResponseDto.setDetails(T);
        return apiResponseDto;
    }

    public static ApiResponseDto actionFail(String method) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setMessage(method + GlobalVariable.ACTION_FAILED);
        apiResponseDto.setStatus(GlobalVariable.STRING_ERROR);
        return apiResponseDto;
    }
}