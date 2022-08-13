package com.project.travel.controller;


import com.project.travel.domain.User;
import com.project.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 전체 USER조회
     */
    @GetMapping("/user/all")
    public List<User> allUser(Model model){
        List<User> users=userService.findMembers();
        model.addAttribute("users", users);
        return users;
    }
    /**
     * 회원가입 API
     * @param dto : 회원가입할 때 입력하는 정보를 담은 객체
     * @return : 제대로 되었으면 HttpStatus.OK와 함께 USER를 BODY에 담아 전송
     * 아닐 경우 HttpStatus.BAD_REQUEST 전송
     */
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

    /**
     * @param dto : 로그인할 때 입력받은 정보를 담은 객체
     * @param result
     * @return : 제대로 되었으면 HttpStatus.OK와 함께 USER를 BODY에 담아 전송
     * 아닐 경우 HttpStatus.BAD_REQUEST 전송
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserForm dto, BindingResult result){
        if(result.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        User user=userService.loginCheck(dto);
        return (user!=null)?
                ResponseEntity.status(HttpStatus.OK).body(user):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

}
