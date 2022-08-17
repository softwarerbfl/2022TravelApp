package com.project.travel.controller;

import com.project.travel.domain.*;
import com.project.travel.repository.UserImageRepository;
import com.project.travel.service.AwsS3Service;
import com.project.travel.service.PostService;
import com.project.travel.service.UserImageService;
import com.project.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final UserService userService;
    private final PostService postService;
    private final AwsS3Service awsS3Service;
    private final UserImageService userImageService;
    /**
     * 마이페이지에 유저 이미지 등록
     */
    @PostMapping("/myPage/{userId}/userImage/regist")
    public ResponseEntity<String> registUserImage(@PathVariable("userId") Long userId, @RequestPart("file") List<MultipartFile> multipartFile){
        UserImage userImage=userService.findAndSaveUserImage(userId, multipartFile);
        return (userImage!=null) ?
                ResponseEntity.status(HttpStatus.OK).body("POST succeed"):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    /**
     * 마이페이지에 유저 이미지 전송
     */
    @GetMapping("/myPage/{userId}/userImage")
    public ResponseEntity<Image> myPageUserImage(@PathVariable("userId") Long userId){

        return (userImageService.findUserImageByUser(userId)!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(userImageService.findUserImageByUser(userId)):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    /**
     * 마이페이지에 유저 이름 전송
     */
    @GetMapping("/myPage/{userId}/userName")
    public ResponseEntity<String> myPageUserName(@PathVariable("userId") Long userId){
        return (userService.findOne(userId).getUserName()!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(userService.findOne(userId).getUserName()):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    /**
     * 마이 페이지에 유저가 쓴 게시물(Post)들 전송
     */
    @GetMapping("/myPage/{userId}/post/myRecords")
    public ResponseEntity<List<Post>> myPost(@PathVariable("userId") Long userId){
        return (postService.viewMyPost(userId)!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(postService.viewMyPost(userId)):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    /**
     * 마이페이지에 유저가 좋아한 게시물(Post) 모두 전송
     */
    @GetMapping("/myPage/{userId}/post/likes")
    public ResponseEntity<List<Post>> likePosts(@PathVariable("userId") Long userId){
        return (postService.viewLikePost(userId)!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(postService.viewLikePost(userId)):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}