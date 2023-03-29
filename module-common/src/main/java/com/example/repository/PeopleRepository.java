package com.example.repository;

import com.example.dto.Role;
import com.example.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<People, Integer> {
    Optional<People> findByEmail(String email);

    Optional<People> findByEmailAndRole(String email, Role STUDENT);

    Optional<Integer> countByRole(Role role);

    @Query("select p.age, count(p.age) from People as p group by p.age")
    List<Objects[]> countTotalPeopleByAge();
}
