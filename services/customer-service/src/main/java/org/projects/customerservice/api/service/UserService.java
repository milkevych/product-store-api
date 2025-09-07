package org.projects.customerservice.api.service;

import org.projects.customerservice.api.dto.*;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void updateProfileInfo(UpdateProfileInfoDto dto, String userId);

    void changePassword(ChangePasswordDto dto, String userId);

    void deactivateAccount(String userId);

    void reactivateAccount(String userId);

    void deleteAccount(DeleteAccountDto dto, String userId);

    String createUser(RegistrationDto dto);

    UserResponseDto getCurrentUser(String userId);

    UserResponseDto findUserByEmail(String email);
}
