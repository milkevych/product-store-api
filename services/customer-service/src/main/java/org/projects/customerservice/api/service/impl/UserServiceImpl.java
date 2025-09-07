package org.projects.customerservice.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.coyote.BadRequestException;
import org.projects.customerservice.api.dto.*;
import org.projects.customerservice.api.exception.BusinessException;
import org.projects.customerservice.api.exception.ErrorCode;
import org.projects.customerservice.api.exception.NotFoundException;
import org.projects.customerservice.api.service.UserMapper;
import org.projects.customerservice.api.service.UserService;
import org.projects.customerservice.store.entity.Role;
import org.projects.customerservice.store.entity.User;
import org.projects.customerservice.store.repository.RoleRepository;
import org.projects.customerservice.store.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(final String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
    }

        
    @Override
    public void updateProfileInfo(UpdateProfileInfoDto dto, String userId) {
        final User savedUser= userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        userMapper.mergerUserInfo(savedUser, dto);
        userRepository.save(savedUser);
    }

    @Override
    public void changePassword(ChangePasswordDto dto, String userId) {
        if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
            throw new BusinessException(ErrorCode.CHANGE_PASSWORD_MISMATCH);
        }

        final User savedUser= userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), savedUser.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        final String encoded = passwordEncoder.encode(dto.getNewPassword());
        savedUser.setPassword(encoded);
        userRepository.save(savedUser);
    }

    @Override
    public void deactivateAccount(String userId) {
        final User user= userRepository.findById(userId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        if (!user.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED);
        }

        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public void reactivateAccount(String userId) {
        final User user = userRepository.findById(userId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));
        if (user.isEnabled()) {
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_ACTIVATED);
        }

        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(DeleteAccountDto dto, String userId) {

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!currentUser.equals(userId)) {
            throw new BusinessException(ErrorCode.DENIED_DELETE_USER);
        }

        final User user = userRepository.findById(userId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

        if (!dto.getCurrentPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        userRepository.deleteById(userId);
    }

    @Override
    public String createUser(RegistrationDto dto) {

        checkUserEmail(dto.getEmail());
        checkUserPhoneNumber(dto.getPhoneNumber());
        checkPasswords(dto.getPassword(), dto.getConfirmPassword());

        var role = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new EntityNotFoundException("Role user does not exitst"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        var user = userMapper.toUser(dto);
        user.setRoles(roles);
        log.debug("Saving user {}", user);
        userRepository.save(user);

        List<User> users = new ArrayList<>();
        users.add(user);
        role.setUsers(users);
        roleRepository.save(role);

        return user.getId();
    }

    @Override
    public UserResponseDto getCurrentUser(String userId){
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
        return new UserResponseDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles(),
                user.isEnabled(),
                user.isLocked(),
                user.isExpired(),
                user.isCredentialsExpired()
                );

    }

    @Override
    public UserResponseDto findUserByEmail(String email) {
        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new UserResponseDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.getRoles(),
                user.isEnabled(),
                user.isLocked(),
                user.isExpired(),
                user.isCredentialsExpired()
                );
    }


    private void checkUserEmail(final String email) {
        final boolean emailExists = userRepository.existsByEmailIgnoreCase(email);
        if (emailExists) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    private void checkUserPhoneNumber(final String userPhoneNumber) {
        final boolean phoneNumberExists = userRepository.existsByPhoneNumber(userPhoneNumber);
        if (phoneNumberExists) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
        }
    }

    private void checkPasswords(final String password, final String confirmPassword) {
        if (password == null || !password.equals(confirmPassword)) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }
    }


}
