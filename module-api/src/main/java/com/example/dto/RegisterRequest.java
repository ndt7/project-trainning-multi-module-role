package com.example.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotNull(message = "Firstname can't be empty.")
    @Size(min = 2, max = 20, message = "Firstname length 2-20 character.")
    private String firstname;
    @NotNull(message = "Lastname can't be empty")
    @Size(min = 2, max = 20, message = "Lastname length 2-20 character")
    private String lastname;
    @NotNull(message = "Email can't be empty.")
    @Email(message = "Must be email format.")
    private String email;
    @NotNull(message = "Password can't empty.")
    @Size(min = 5, max = 30, message = "Password length 5-20 character.")
    private String password;
    @NotNull
    @Min(1)
    @Max(value = 120, message = "age 1-120.")
    private Long age;

    @NotNull
    @Length(min = 9, max = 12, message = "length 9-10")
    private String phoneNumber;
}
