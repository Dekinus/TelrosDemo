package com.example.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static com.example.demo.model.entity.Privilege.CAN_CREATE;
import static com.example.demo.model.entity.Privilege.CAN_DELETE;
import static com.example.demo.model.entity.Privilege.CAN_UPDATE;

@Getter
@AllArgsConstructor
public enum Role {

    USER(List.of()),
    ADMIN(List.of(CAN_DELETE, CAN_UPDATE, CAN_CREATE));

    private final List<Privilege> privileges;
}
