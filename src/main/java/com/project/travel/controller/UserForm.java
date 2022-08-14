package com.project.travel.controller;

import com.project.travel.domain.User;
import lombok.Builder;
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

    @Builder
    public void UserInfoDto(User user){
        this.userName=user.getUserName();
        this.userId=user.getUserId();
        this.userPassword=user.getUserPassword();
    }
}