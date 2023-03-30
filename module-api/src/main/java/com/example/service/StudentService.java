package com.example.service;

import com.example.dto.CommonResponse;
import com.example.dto.RegisterRequest;
import com.example.dto.Role;
import com.example.entity.People;
import com.example.entity.Subject;
import com.example.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final PeopleRepository peopleRepo;
    private final GetEmailIntroduceService getEmailIntroduce;
    private final PasswordEncoder passwordEncoder;
    private final CommonService commonService;
    ModelMapper modelMapper = new ModelMapper();

    public Object editStudent(People people) {
        try {
            Optional<People> people_ = peopleRepo.findByEmailAndRole(people.getEmail(), Role.STUDENT);
            String email = getEmailIntroduce.get(people.getIntroduce());
            String e = (email != null) ? email : people.getEmail();
            if (people_.isPresent()) {
                people_.get().setEmail(e);
                people_.get().setPhoneNumber(Base64.getEncoder().encodeToString(people.getPhoneNumber().getBytes()));
                people_.get().setIntroduce(people.getIntroduce());
                people_.get().setFirstname(people.getFirstname());
                people_.get().setLastname(people.getLastname());
                people_.get().setStudentCardCode(people.getStudentCardCode());
                people_.get().setPassword(passwordEncoder.encode(people.getPassword()));
                peopleRepo.save(people_.get());
                return ResponseEntity.ok(people_.get());
            } else {
                System.out.println("User does not exit.");
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
        System.out.println(subjectPeopleMap.toString());
        System.out.println(sameAge.toString());


        return CommonResponse.builder()
                .countStudent(countStudent.get())
                .countTeacher(countTeacher.get())
                .countSameAge(sameAge)
                .countSameSubject(subjectPeopleMap)
                .build();
    }

    public ResponseEntity<People> addStudent(RegisterRequest request) {
        Optional<People> people_ = peopleRepo.findByEmail(request.getEmail());
        if (people_.isEmpty()) {
            People people = modelMapper.map(request, People.class);
            System.out.println(people.toString());
            people.setRole(Role.STUDENT);
            return ResponseEntity.ok(peopleRepo.save(people));
        }
        System.out.println("User does not exit.");
        return ResponseEntity.badRequest().build();
    }


}
