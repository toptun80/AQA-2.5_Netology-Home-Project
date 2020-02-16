package ru.netology.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDataModel {
    private final String userFullName;
    private final String userPhone;
    private final String cityPreInput;
}