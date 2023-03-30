package com.example.controller;

import com.example.Ultil.ActionResponse;
import com.example.Ultil.GlobalVariable;
import com.example.dto.RegisterRequest;
import com.example.entity.People;
import com.example.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
//@Lazy
@RequestMapping("/api/student")
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableMethodSecurity
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PreAuthorize("hasAnyAuthority('TEACHER', 'ADMIN')")
    @PostMapping("/add")
    public Object addStudent(@RequestBody RegisterRequest request) {
        try {
            return ActionResponse.actionSuccess(studentService.addStudent(request), GlobalVariable.ACTION_SUCCESS);
        } catch (Exception e) {
            return ActionResponse.actionFail(e + GlobalVariable.ACTION_FAILED);
        }
    }

    @PreAuthorize("hasAnyAuthority('STUDENT', 'ADMIN')")
    @PostMapping("/edit")
    public Object editStudent(@RequestBody People people) {
        try {
            Object object = studentService.editStudent(people);
            if (object != null) {
                return ActionResponse.actionSuccess(object, GlobalVariable.ACTION_SUCCESS);
            }
            return ActionResponse.actionFail("User does not exit.");

        } catch (Exception e) {
            return ActionResponse.actionFail(e + GlobalVariable.ACTION_FAILED);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/jobcommon")
    public Object jobCommon() {
        try {
            return ActionResponse.actionSuccess(studentService.jobCommon(), GlobalVariable.ACTION_SUCCESS);
        } catch (Exception e) {
            return ActionResponse.actionFail(e + GlobalVariable.ACTION_FAILED);
        }
    }

}
