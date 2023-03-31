package com.example.controller;

import com.example.Ultil.GlobalVariable;
import com.example.dto.ApiResponseDto;
import com.example.dto.RegisterRequest;
import com.example.dto.Role;
import com.example.entity.People;
import com.example.service.RoleService;
import com.example.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@Lazy
@RequestMapping("/api/student")
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableMethodSecurity
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final RoleService roleService;

    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    @PostMapping("/add")
    public ApiResponseDto addStudent(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                return ApiResponseDto.actionFail(error.getObjectName() + " - " + error.getDefaultMessage());
            }
        }
        People people = studentService.addStudent(request);
        if (people == null) {
            return ApiResponseDto.actionFail(String.format("Account %s already exit.", request.getEmail()));
        } else {
            return ApiResponseDto.actionSuccess(people, "");
        }
    }

    @PreAuthorize("hasAnyAuthority('STUDENT', 'ADMIN')")
    @PostMapping("/edit")
    public ApiResponseDto editStudent(@RequestBody @Valid People people, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<FieldError> errors = bindingResult.getFieldErrors();
                for (FieldError error : errors) {
                    return ApiResponseDto.actionFail(error.getObjectName() + " - " + error.getDefaultMessage());
                }
            }
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userDetails.getUsername().equals(people.getEmail()) || roleService.checkRole(userDetails.getUsername()).equals(Role.ADMIN)) {
                People p = studentService.editStudent(people);
                if (p != null) {
                    return ApiResponseDto.actionSuccess(p, GlobalVariable.ACTION_SUCCESS);
                } else {
                    return ApiResponseDto.actionFail(String.format("User %s does not exit.", p.getEmail()));
                }
            }
        } catch (Exception e) {
            return ApiResponseDto.actionFail(e + "\n" + GlobalVariable.ACTION_FAILED);
        }
        return ApiResponseDto.actionFail("You don't have access.");
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/jobcommon")
    public ApiResponseDto jobCommon() {
        try {
            return ApiResponseDto.actionSuccess(studentService.jobCommon(), GlobalVariable.ACTION_SUCCESS);
        } catch (Exception e) {
            return ApiResponseDto.actionFail(e + GlobalVariable.ACTION_FAILED);
        }
    }

}
