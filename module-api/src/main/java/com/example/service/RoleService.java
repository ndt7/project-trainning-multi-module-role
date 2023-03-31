package com.example.service;

import com.example.dto.Role;
import com.example.entity.People;
import com.example.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final PeopleRepository peopleRepo;

    public Role checkRole(String username) {
        Optional<People> people = peopleRepo.findByEmail(username);
        return people.map(People::getRole).orElse(null);
    }


}
