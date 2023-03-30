package com.example.dto;

import com.example.entity.People;
import com.example.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommonResponse {
    private Integer countStudent;
    private Integer countTeacher;
    private Map<Integer, Long> countSameAge;
    private Map<Subject, List<People>> countSameSubject;
}
