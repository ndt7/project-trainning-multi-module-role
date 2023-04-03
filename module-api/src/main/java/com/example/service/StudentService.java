package com.example.service;

import com.example.dto.CommonResponse;
import com.example.dto.RegisterRequest;
import com.example.dto.Role;
import com.example.entity.People;
import com.example.entity.Subject;
import com.example.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final PeopleRepository peopleRepo;
    private final GetEmailIntroduceService getEmailIntroduce;
    private final PasswordEncoder passwordEncoder;
    private final CommonService commonService;
    private final Environment env;
    ModelMapper modelMapper = new ModelMapper();

    public People editStudent(People people) {
        try {
            Optional<People> savedPeople = peopleRepo.findByEmailAndRole(people.getEmail(), Role.STUDENT);

            String email = getEmailIntroduce.get(people.getIntroduce());
            Optional<People> peopleCheckEmail = peopleRepo.findByEmail(email);
            if (peopleCheckEmail.isPresent()) {
                System.out.printf("email %s đã tồn tại trong hệ thống", email);
                return null;
            }
            String phone = (Arrays.asList(env.getActiveProfiles()).contains("dev")) ? Base64.getEncoder().encodeToString(people.getPhoneNumber().getBytes()) : people.getPhoneNumber();

            String e = (email != null) ? email : people.getEmail();
            if (savedPeople.isPresent()) {
                People p = savedPeople.get();
                p.setEmail(e);
                p.setPhoneNumber(phone);
                p.setIntroduce(people.getIntroduce());
                p.setFirstname(people.getFirstname());
                p.setLastname(people.getLastname());
                p.setStudentCardCode(people.getStudentCardCode());
                p.setPassword(passwordEncoder.encode(people.getPassword()));
                peopleRepo.save(p);
                return p;
            } else {
                System.out.printf("User %s không tồn tại trong hệ thống.", people.getEmail());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CommonResponse jobCommon() {
        Optional<Integer> countStudent = peopleRepo.countByRole(Role.STUDENT);
        Optional<Integer> countTeacher = peopleRepo.countByRole(Role.TEACHER);
        Map<Subject, List<People>> subjectPeopleMap = commonService.findCountPeopleSameSubject();
        Map<Integer, Long> sameAge = commonService.findPeopleSameAge(peopleRepo.findAll());

        return CommonResponse.builder()
                .countStudent(countStudent.get())
                .countTeacher(countTeacher.get())
                .countSameAge(sameAge)
                .countSameSubject(subjectPeopleMap)
                .build();
    }

    public People addStudent(RegisterRequest request) {
        Optional<People> people_ = peopleRepo.findByEmail(request.getEmail());
        if (people_.isEmpty()) {
            String phone = (Arrays.asList(env.getActiveProfiles()).contains("dev")) ? Base64.getEncoder().encodeToString(request.getPhoneNumber().getBytes()) : request.getPhoneNumber();
            People people = modelMapper.map(request, People.class);
            System.out.println(people.toString());
            people.setRole(Role.STUDENT);
            people.setPhoneNumber(phone);
            people.setPassword(passwordEncoder.encode(request.getPassword()));
            return peopleRepo.save(people);
        }
        return null;
    }
}
