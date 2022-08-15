package com.project.travel.controller;

import com.project.travel.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


import com.project.travel.domain.Image;
import com.project.travel.domain.Post;
import com.project.travel.domain.User;
import com.project.travel.service.PostService;
import com.project.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final AwsS3Service awsS3Service;

    private String find = new String();

    String url = "https://travel-app-image-bucket.s3.ap-northeast-2.amazonaws.com/33b1742a-1693-4aed-a08d-ea4ee7293f6b.png";

    @PostMapping(value = "/posting")
    public ResponseEntity<String> createPost(@Valid PostForm form) {
        List<MultipartFile> multipartFile = form.getImages();
        List<String> s = awsS3Service.uploadImage(multipartFile);


        List<Image> images = new ArrayList<>();
        for (int i = 0; i < s.size();i++){
            Image image = new Image();
            image.setFileName(s.get(i));
            image.setFileOriName(multipartFile.get(i).getOriginalFilename());
            image.setFileUrl(url+s.get(i));
            images.add(image);
        }
        postService.savePost(form.getUserId(),
                form.getTitle(),
                form.getSTags(),
                form.getPlaceTypes(),
                form.getScores(),
                form.getContents(),
                images,
                form.getDays(),
                form.getSPlaces());
        return ResponseEntity.status(HttpStatus.OK).body("Post Success");
    }


    // 게시물 열람 페이지
    @GetMapping(value = "/post/{id}")
    public ResponseEntity<Post> viewPost(@PathVariable("id") Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(postService.viewPost(postId));
    }
    @GetMapping(value = "/post/user")
    public User getUserIdByPost(Long userId){
        return userService.findOne(userId);
    }


    // 여행 탐색 페이지
    @GetMapping(value = "/search/default")
    public ResponseEntity<List<Post>> getDefaultPosts(){
        return ResponseEntity.status(HttpStatus.OK).
                body(postService.defaultPosts());
    }

    @GetMapping(value = "search/{find}")
    public ResponseEntity<List<Post>> searchPost(){
        return ResponseEntity.status(HttpStatus.OK).
                body(postService.searchPosts(find));
    }
    @PostMapping(value = "/like/{userId}/{postId}")
    public ResponseEntity<String> likePost(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId){
        postService.likePost(userId , postId);
        return ResponseEntity.status(HttpStatus.OK).
                body("Succeeded Like!");
    }

}