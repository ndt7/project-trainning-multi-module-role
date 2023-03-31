package com.example.service;

import com.example.entity.People;
import com.example.entity.Subject;
import com.example.repository.PeopleRepository;
import com.example.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final SubjectRepository subjectRepo;
    private final PeopleRepository peopleRepo;

    public Map<Integer, Long> findPeopleSameAge(List<People> peopleList) {
        return peopleList.stream().collect(Collectors.groupingBy(People::getAge, Collectors.counting()));
    }

    public Map<Subject, List<People>> findCountPeopleSameSubject() {

        List<Subject> subjectList = subjectRepo.findAll();
        List<People> peopleList = peopleRepo.findAll();

        return subjectList.stream()
                .collect(Collectors.toMap(
                        subject -> subject,
                        subject -> peopleList.stream()
                                .filter(people -> people.getSubjects().contains(subject))
                                .collect(Collectors.toList())
                ));
    }
}
