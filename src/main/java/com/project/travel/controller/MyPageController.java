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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String,Object>> myPost(@PathVariable("userId") Long userId){
        List<Post> myPost=postService.viewMyPost(userId); //user가 작성한 전체 게시물들

        Map<String,Object> result=new HashMap<>();

        //i번째 게시물의 첫 번째 사진의 url
        for(int i=0;i<myPost.size();i++){
            Post post=myPost.get(i);
            String url=postService.viewPostImageUrl(post.getId());

            result.put(url,post);
        }

        return (postService.viewMyPost(userId)!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(result):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    /**
     * 마이페이지에 유저가 좋아한 게시물(Post) 모두 전송
     */
    @GetMapping("/myPage/{userId}/post/likes")
    public ResponseEntity<Map<String,Object>> likePosts(@PathVariable("userId") Long userId){
        List<Post> likePost=postService.viewLikePost(userId); //user가 좋아요를 누른 전체 게시물들
        Map<String,Object> result=new HashMap<>();

        //i번째 게시물의 첫 번째 사진의 url
        for(int i=0;i<likePost.size();i++){
            Post post=likePost.get(i);
            String url=postService.viewPostImageUrl(post.getId());

            result.put(url,post);
        }

        return (result!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(result):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}