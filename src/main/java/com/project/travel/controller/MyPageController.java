package com.project.travel.controller;

import com.project.travel.domain.Image;
import com.project.travel.domain.User;
import com.project.travel.domain.UserImage;
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
    @PostMapping("myPage/{userId}/userImage/regist")
    public ResponseEntity<UserImage> registUserImage(@PathVariable("userId") Long userId, @RequestPart("file") List<MultipartFile> multipartFile){
        User user=new User();
        user=userService.findOne(userId);

        //이미지 객체 생성
        List<String> s=awsS3Service.uploadImage(multipartFile);
        List<Image> images=new ArrayList<>();
        Image image = new Image();
        image.setFileName(s.get(0));
        image.setFileOriName(multipartFile.get(0).getOriginalFilename());
        image.setFileUrl("https://travel-app-image-bucket.s3.ap-northeast-2.amazonaws.com/"+s.get(0));


        //유저 이미지 객체 생성
        UserImage userImage=new UserImage();
        userImage.setImage(image);
        userImage.setUser(user);
        userImageService.join(userImage);
        //유저 이미지 등록
        user.setUserImage(userImage);


        return (userImage!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(userImage):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    /**
     * 마이페이지에 유저 이미지 전송
     */
    @GetMapping("myPage/{userId}/userImage")
    public ResponseEntity<Image> myPageUserImage(@PathVariable("userId") Long userId){
        User user=new User();
        user=userService.findOne(userId); //프론트에서 경로에 넣어준 userId로 user찾기
        Image userImage=user.getUserImage().getImage();
        return (userImage!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(userImage):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    /**
     * 마이페이지에 유저 이름 전송
     */
    @GetMapping("myPage/{userId}/userName")
    public String myPageUserName(@PathVariable("userId") Long userId){
        User user=new User();
        user=userService.findOne(userId);

        return user.getUserName();
    }
    /**
     * 마이 페이지에 유저가 쓴 게시물(Post)들 전송
     */

    /**
     * 마이페이지에 유저가 좋아한 게시물(Post)들 전송
     */
}
