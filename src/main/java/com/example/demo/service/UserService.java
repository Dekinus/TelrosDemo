package com.example.demo.service;

import com.example.demo.exception.DataNotFoundException;
import com.example.demo.exception.ImageDownloadException;
import com.example.demo.exception.ImageUploadException;
import com.example.demo.exception.RegistrationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.request.CreateUserRequest;
import com.example.demo.model.dto.request.UpdateUserContactInfoRequest;
import com.example.demo.model.dto.request.UpdateUserRequest;
import com.example.demo.model.dto.response.SimpleMessageResponse;
import com.example.demo.model.dto.response.UserContactInfoResponse;
import com.example.demo.model.dto.response.UserInfoResponse;
import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.UserImage;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SimpleMessageResponse createUser(CreateUserRequest createUserRequest) {

        if (userRepository.findByEmail(createUserRequest.email()).isPresent()) {
            throw new RegistrationException("User with this email already exists");
        }

        User user = new User();
        user.setName(createUserRequest.name());
        user.setSurname(createUserRequest.surname());
        user.setPatronymic(createUserRequest.patronymic());
        user.setPhoneNumber(createUserRequest.phoneNumber());
        user.setDateOfBirth(createUserRequest.dateOfBirth());
        user.setEmail(createUserRequest.email());
        user.setPassword(passwordEncoder.encode(createUserRequest.password()));
        user.setRole(Role.USER);

        userRepository.save(user);
        return new SimpleMessageResponse("User was created");
    }

    public UserInfoResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User with UUID " + id + " was not found"));
        return new UserInfoResponse(
                user.getName(),
                user.getSurname(),
                user.getPatronymic(),
                user.getDateOfBirth(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getImage()
        );
    }

    @Transactional
    public SimpleMessageResponse updateUser(UpdateUserRequest updateUserRequest, UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User with UUID " + id + " was not found"));

        if (userRepository.findByEmail(updateUserRequest.email()).isPresent()) {
            throw new RegistrationException("User with this email already exists");
        }

        Optional.ofNullable(updateUserRequest.name()).filter(StringUtils::isNoneBlank).ifPresent(user::setName);
        Optional.ofNullable(updateUserRequest.surname()).filter(StringUtils::isNoneBlank).ifPresent(user::setSurname);
        Optional.ofNullable(updateUserRequest.patronymic()).filter(StringUtils::isNoneBlank).ifPresent(user::setPatronymic);
        Optional.ofNullable(updateUserRequest.dateOfBirth()).ifPresent(user::setDateOfBirth);
        Optional.ofNullable(updateUserRequest.phoneNumber()).filter(StringUtils::isNoneBlank).ifPresent(user::setPhoneNumber);
        Optional.ofNullable(updateUserRequest.email()).filter(StringUtils::isNoneBlank).ifPresent(user::setEmail);


        userRepository.save(user);
        return new SimpleMessageResponse("User with UUID " + id + " was updated");
    }


    @Transactional
    public SimpleMessageResponse deleteUserById(UUID id) {
        userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User with UUID " + id + " was not found"));
        userRepository.deleteById(id);
        return new SimpleMessageResponse("User with UUID " + id + " was deleted");
    }

    public UserContactInfoResponse getUserContactInfo(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User with UUID " + id + " was not found"));
        return new UserContactInfoResponse(
                user.getSurname() + " " + user.getName() + " " + Optional.ofNullable(user.getPatronymic()).orElse(""),
                user.getPhoneNumber(),
                user.getEmail()
        );
    }

    @Transactional
    public SimpleMessageResponse updateUserContactInfo(UpdateUserContactInfoRequest updateUserContactInfoRequest, UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User with UUID " + id + " was not found"));
        user.setPhoneNumber(updateUserContactInfoRequest.phoneNumber());
        user.setEmail(updateUserContactInfoRequest.email());
        return new SimpleMessageResponse("User's with UUID " + id + "contact info was updated");
    }

    public Page<UserInfoResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserInfoResponse);
    }

    @Transactional
    public SimpleMessageResponse uploadImage(UUID id, UserImage image) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User with UUID " + id + " was not found"));
        if (user.getImage() == null) {
            String fileName = imageService.uploadImage(image);
            user.setImage(fileName);
            userRepository.save(user);
        } else {
            throw new ImageUploadException("User already has an image. Use update image.");
        }
        return new SimpleMessageResponse("Image added to user with UUID " + id);
    }

    public SimpleMessageResponse getImageByUserId(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User with UUID " + id + " was not found"));
        if (user.getImage() == null)
            throw new ImageDownloadException("User with UUID " + id + " has no image");
        return new SimpleMessageResponse(user.getImage());
    }

    @Transactional
    public SimpleMessageResponse updateImage(UUID id, UserImage image) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User with UUID " + id + " was not found"));
        if (user.getImage() == null) {
            throw new ImageUploadException("User has no image. Use upload image.");
        } else {
            String fileName = imageService.updateImage(image, user.getImage());
            user.setImage(fileName);
            userRepository.save(user);
            return new SimpleMessageResponse("Image for User with UUID " + id + " was updated");
        }
    }

    @Transactional
    public SimpleMessageResponse deleteImageByUserId(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User with UUID " + id + " was not found"));
        if (user.getImage() == null)
            return new SimpleMessageResponse("User with UUID " + id + " has no image");

        imageService.deleteImage(user.getImage());

        user.setImage(null);
        userRepository.save(user);
        return new SimpleMessageResponse("Image for User with UUID " + id + " was deleted");
    }
}
