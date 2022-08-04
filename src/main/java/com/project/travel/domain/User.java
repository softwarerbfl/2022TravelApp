package com.project.travel.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    @Column(name="user_id")
    private Long id;

    private String userName;
    private String userId;
    private String password;
    private String passwordCheck;

}