package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CommonResponse {
    private Integer countStudent;
    private Integer countTeacher;
    private Integer countSameAge;
    private Integer countSameSubject;
}
