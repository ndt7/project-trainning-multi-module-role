package com.example.service;

import com.example.dto.AuthenticationResponse;
import com.example.dto.CommonResponse;
import com.example.dto.RegisterRequest;
import com.example.dto.Role;
import com.example.entity.People;
import com.example.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final PeopleRepository peopleRepo;
    private final GetEmailIntroduceService getEmailIntroduce;
    private final PasswordEncoder passwordEncoder;
    ModelMapper modelMapper = new ModelMapper();

    public ResponseEntity<People> editStudent(People people) {
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
        }
        else {
            return ResponseEntity.badRequest().build();        }

    }

    public CommonResponse jobCommon() {
        Optional<Integer> countStudent = peopleRepo.countByRole(Role.STUDENT);
        Optional<Integer> countTeacher = peopleRepo.countByRole(Role.TEACHER);
        List<Objects[]> objects = peopleRepo.countTotalPeopleByAge();


        return CommonResponse.builder()
                .countStudent(countStudent.get())
                .countTeacher(countTeacher.get())
                .countSameAge(objects.size())
                .build();
    }

    public People addStudent(RegisterRequest request){
        People people_ = modelMapper.map(request, People.class);
        people_.setRole(Role.STUDENT);
        peopleRepo.save(people_);
        return people_;
    }

}
