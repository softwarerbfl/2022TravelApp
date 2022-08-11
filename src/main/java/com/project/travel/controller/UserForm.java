package com.project.travel.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserForm {
    @NotEmpty(message="회원 이름은 필수 입니다.")
    private String userName;

    private String userId;
    private String userPassword;
    private String userPasswordCheck;
}
