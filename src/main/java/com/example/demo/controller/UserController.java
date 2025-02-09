package com.example.demo.controller;

import com.example.demo.mapper.ImageMapper;
import com.example.demo.model.dto.request.CreateUserRequest;
import com.example.demo.model.dto.request.UpdateUserContactInfoRequest;
import com.example.demo.model.dto.request.UpdateUserRequest;
import com.example.demo.model.dto.request.UserImageRequest;
import com.example.demo.model.dto.response.SimpleMessageResponse;
import com.example.demo.model.dto.response.UserContactInfoResponse;
import com.example.demo.model.dto.response.UserInfoResponse;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.UserImage;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users/")
public class UserController {

    private final UserService userService;
    private final ImageMapper imageMapper;

    @PreAuthorize("hasAuthority('CAN_CREATE')")
    @PostMapping("create")
    public ResponseEntity<SimpleMessageResponse> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userService.createUser(createUserRequest));
    }

    @GetMapping("{id}")
    public ResponseEntity<UserInfoResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping()
    public ResponseEntity<UserInfoResponse> getUser() {
        UUID id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<UserInfoResponse>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PreAuthorize("hasAuthority('CAN_UPDATE')")
    @PatchMapping("update/{id}") // for admin
    public ResponseEntity<SimpleMessageResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest, @PathVariable UUID id) {
        return ResponseEntity.ok(userService.updateUser(updateUserRequest, id));
    }

    @PatchMapping("update") // for user
    public ResponseEntity<SimpleMessageResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        UUID id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(userService.updateUser(updateUserRequest, id));
    }

    @PreAuthorize("hasAuthority('CAN_DELETE')")
    @DeleteMapping("delete/{id}") // for admin
    public ResponseEntity<SimpleMessageResponse> deleteUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

    @DeleteMapping("delete") // for user
    public ResponseEntity<SimpleMessageResponse> deleteUser() {
        UUID id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

    @GetMapping("contact-info/{id}") // by id
    public ResponseEntity<UserContactInfoResponse> getUserContactInfo(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserContactInfo(id));
    }

    @GetMapping("contact-info") // self
    public ResponseEntity<UserContactInfoResponse> getUserContactInfo() {
        UUID id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(userService.getUserContactInfo(id));
    }

    @PatchMapping("update-contact-info") // user
    public ResponseEntity<SimpleMessageResponse> updateUser(@RequestBody UpdateUserContactInfoRequest updateUserContactInfoRequest) {
        UUID id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(userService.updateUserContactInfo(updateUserContactInfoRequest, id));
    }

    @PreAuthorize("hasAuthority('CAN_UPDATE')")
    @PatchMapping("update-contact-info/{id}") //admin
    public ResponseEntity<SimpleMessageResponse> updateUser(@RequestBody UpdateUserContactInfoRequest updateUserContactInfoRequest, @PathVariable UUID id) {
        return ResponseEntity.ok(userService.updateUserContactInfo(updateUserContactInfoRequest, id));
    }

    @PostMapping("image") // create for self
    public ResponseEntity<SimpleMessageResponse> uploadImage(@Validated @ModelAttribute UserImageRequest imageRequest) {
        UserImage image = imageMapper.toEntity(imageRequest);
        UUID id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(userService.uploadImage(id, image));
    }

    @PreAuthorize("hasAuthority('CAN_CREATE')")
    @PostMapping("{id}/image") // create by admin
    public ResponseEntity<SimpleMessageResponse> uploadImage(@PathVariable UUID id, @Validated @ModelAttribute UserImageRequest imageRequest) {
        UserImage image = imageMapper.toEntity(imageRequest);
        return ResponseEntity.ok(userService.uploadImage(id, image));
    }


    @GetMapping("{id}/image") // read someones
    public ResponseEntity<SimpleMessageResponse> getImageByUserId(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getImageByUserId(id));
    }

    @GetMapping("image") // read self
    public ResponseEntity<SimpleMessageResponse> getImageByUserId() {
        UUID id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(userService.getImageByUserId(id));
    }

    @PatchMapping("image") //update self
    public ResponseEntity<SimpleMessageResponse> updateImage(@Validated @ModelAttribute UserImageRequest imageRequest) {
        UserImage image = imageMapper.toEntity(imageRequest);
        UUID id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        return ResponseEntity.ok(userService.updateImage(id, image));
    }

    @PreAuthorize("hasAuthority('CAN_UPDATE')")
    @PatchMapping("{id}/image") //update by admin
    public ResponseEntity<SimpleMessageResponse> updateImage(@PathVariable UUID id, @Validated @ModelAttribute UserImageRequest imageRequest) {
        UserImage image = imageMapper.toEntity(imageRequest);
        return ResponseEntity.ok(userService.updateImage(id, image));
    }

    @PreAuthorize("hasAuthority('CAN_DELETE')")
    @DeleteMapping("{id}/image") //delete
    public ResponseEntity<SimpleMessageResponse> deleteImageByUserId(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.deleteImageByUserId(id));
    }

    @DeleteMapping("image") //delete self
    public ResponseEntity<SimpleMessageResponse> deleteImageByUserId() {
        UUID id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(userService.deleteImageByUserId(id));
    }
}
