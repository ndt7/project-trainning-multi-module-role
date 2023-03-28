package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity
public class StudentController {
    @RequestMapping("/student")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
//    @Secured({ "ADMIN", "STUDENT" })
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("student role");
    }

    @RequestMapping("/teacher")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
//    @Secured({"ADMIN", "TEACHER"})
    public ResponseEntity<String> teacher(){

        return ResponseEntity.ok("teacher role");
    }
}
