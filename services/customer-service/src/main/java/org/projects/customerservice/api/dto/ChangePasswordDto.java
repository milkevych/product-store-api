package org.projects.customerservice.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDto {

    private String currentPassword;

    private String newPassword;

    private String confirmNewPassword;

}
