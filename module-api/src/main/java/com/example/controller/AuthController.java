package com.example.controller;

import com.example.Ultil.GlobalVariable;
import com.example.auth.AuthenticationService;
import com.example.dto.ApiResponseDto;
import com.example.dto.AuthenticationRequest;
import com.example.dto.AuthenticationResponse;
import com.example.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ApiResponseDto register(@RequestBody @Valid RegisterRequest registerRequest,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                return ApiResponseDto.actionFail(error.getObjectName() + " - " + error.getDefaultMessage());
            }
        }
        AuthenticationResponse authen = service.register(registerRequest);
        if (authen == null) {
            return ApiResponseDto.actionFail(String.format("Account %s already exists", registerRequest.getEmail()));
        }
        return ApiResponseDto.actionSuccess(authen, GlobalVariable.ACTION_SUCCESS);
    }

    @PostMapping("/authorization")
    public ApiResponseDto login(@RequestBody @Valid AuthenticationRequest authenticationRequest,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                return ApiResponseDto.actionFail(error.getObjectName() + " - " + error.getDefaultMessage());
            }
        }

        Object object = service.authenticate(authenticationRequest);
        if (object != null) {
            return ApiResponseDto.actionSuccess(object, GlobalVariable.ACTION_SUCCESS);
        }
        return ApiResponseDto.actionFail("Incorrect account or password");
    }

    @PostMapping("/test")
    public ApiResponseDto test(@RequestBody RegisterRequest registerRequest) {
        try {
            return ApiResponseDto.actionSuccess(service.register(registerRequest), GlobalVariable.ACTION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseDto.actionFail(e + GlobalVariable.ACTION_FAILED);
        }
    }

}
