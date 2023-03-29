package com.example.controller;

import com.example.auth.AuthenticationService;
import com.example.dto.AuthenticationResponse;
import com.example.dto.CommonResponse;
import com.example.dto.RegisterRequest;
import com.example.entity.People;
import com.example.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
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
    public People addStudent(@RequestBody RegisterRequest request) {
        return studentService.addStudent(request);
    }
    @PreAuthorize("hasAnyAuthority('STUDENT', 'ADMIN')")
    @PostMapping("/edit")
    public ResponseEntity<People> editStudent(@RequestBody People people) {
        return studentService.editStudent(people);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/jobcommon")
    public CommonResponse jobCommon() {
        return studentService.jobCommon();
    }

}
