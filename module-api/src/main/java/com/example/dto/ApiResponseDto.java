package com.example.dto;

import com.example.Ultil.GlobalVariable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto {
    private String status;
    private String message;
    private Object detailBuilder;

    public static ApiResponseDto actionSuccess(Object T, String method) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setMessage(method + GlobalVariable.ACTION_SUCCESS);
        apiResponseDto.setStatus(GlobalVariable.STRING_SUCCESS);
        apiResponseDto.setDetailBuilder(T);
        return apiResponseDto;
    }

    public static ApiResponseDto actionFail(String method) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setMessage(method);
        apiResponseDto.setStatus(GlobalVariable.STRING_ERROR);
        return apiResponseDto;
    }
}