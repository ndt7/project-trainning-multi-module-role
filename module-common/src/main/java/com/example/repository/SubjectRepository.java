package com.example.repository;

import com.example.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

//    @Query("select s.nameSubject, count(p.email) from Subject as s, People as p " +
//            "join people_subject_set as ps on ps.people_id = p.id" +
//            "join Subject on s.id = ps.subject_id ")
//    List<Object> findAllByNameSubjectWithCountPeople();
}
