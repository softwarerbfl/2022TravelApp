package com.project.travel.controller;


import com.project.travel.domain.User;
import com.project.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<User> create(@RequestBody UserForm dto, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user=new User();
        user.setUserId(dto.getUserId());
        user.setUserName(dto.getUserName());
        user.setUserPassword(dto.getUserPassword());
        userService.join(user);
        return (user!=null)?
                ResponseEntity.status(HttpStatus.OK).body(user):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
