package com.example.controller;

import com.example.Ultil.ActionResponse;
import com.example.Ultil.GlobalVariable;
import com.example.auth.AuthenticationService;
import com.example.dto.AuthenticationRequest;
import com.example.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class HomeController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public Object register(@RequestBody RegisterRequest registerRequest) {
        return ActionResponse.actionSuccess(service.register(registerRequest), GlobalVariable.ACTION_SUCCESS);
    }

    @PostMapping("/authorization")
    public Object login(@RequestBody AuthenticationRequest authenticationRequest) {
        Object object = service.authenticate(authenticationRequest);
        if (object != null) {
            return ActionResponse.actionSuccess(object, GlobalVariable.ACTION_SUCCESS);
        }
        return ActionResponse.actionFail("Incorrect account or password" + GlobalVariable.STRING_ERROR);
    }

    @PostMapping("/test")
    public Object test(@RequestBody RegisterRequest registerRequest) {
        try {
            return ActionResponse.actionSuccess(service.register(registerRequest), GlobalVariable.ACTION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ActionResponse.actionFail(e + GlobalVariable.ACTION_FAILED);
        }
    }

}
