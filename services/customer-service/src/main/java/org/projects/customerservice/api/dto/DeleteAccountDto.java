package org.projects.customerservice.api.dto;

import lombok.Getter;

@Getter
public class DeleteAccountDto {

    private String currentPassword;

    private String confirmPassword;

}
