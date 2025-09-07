package org.projects.customerservice.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {

        private String firstname;
        private String lastname;
        private String email;
        private String phoneNumber;
        private String password;
        private String confirmPassword;
}

