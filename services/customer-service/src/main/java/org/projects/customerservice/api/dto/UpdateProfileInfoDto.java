package org.projects.customerservice.api.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileInfoDto {

    private String firstname;

    private String lastname;

    private LocalDate dateOfBirth;
}
