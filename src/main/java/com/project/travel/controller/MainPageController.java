package com.project.travel.controller;

import com.project.travel.domain.Post;
import com.project.travel.domain.Tag;
import com.project.travel.domain.User;
import com.project.travel.service.PostService;
import com.project.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private final PostService postService;
    private final UserService userService;
    /**
     * 메인 페이지에 추천 태그들 전송
     */
    @GetMapping("main/{userId}/tags")
    public ResponseEntity<List<Tag>> recommendTag(@PathVariable("userId") Long userId){
        List<Tag> recommendTags=userService.calRecommendTag(userId);

        return (recommendTags.subList(0,3)!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(recommendTags.subList(0,3)):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    /**
     * 메인 페이지에 추천 게시물들 전송
     */
    @GetMapping("main/{userId}/posts")
    public ResponseEntity<List<List<Post>>> recommendPost(@PathVariable("userId") Long userId){

        List<Tag> tags=userService.calRecommendTag(userId); //추천 태그 목록
        tags=tags.subList(0,3);
        List<List<Post>> posts=new ArrayList<>();
        for(int i=0;i<tags.size();i++){
            List<Post> posts1=postService.searchPosts(tags.get(i).getTagContent());
            posts.add(i,posts1);
        }
        return (!posts.isEmpty()) ?
                ResponseEntity.status(HttpStatus.OK).body(posts):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    /**
     * 메인 페이지에 추천 게시물들의 대표사진 url전송
     */
    @GetMapping("main/{userId}/posts/image")
    public ResponseEntity<List<List<String>>> recommendPostImage(@PathVariable("userId") Long userId){
        List<Tag> tags=userService.calRecommendTag(userId); //추천 태그 목록
        tags=tags.subList(0,3);
        List<List<String>> postImageUrl=new ArrayList<>();
        for(int i=0;i<tags.size();i++){
            List<Post> posts=postService.searchPosts(tags.get(i).getTagContent());
            List<String> url=new ArrayList<>(); //각 태그에 해당하는 게시물들의 대표사진 url
            //i번 태그를 가진 게시물들의 대표사진의 url들을 모으는 반복문
            for(int j=0;j<posts.size();j++){
                url.add(j,postService.viewPostImageUrl(posts.get(j).getId()));
            }
            postImageUrl.add(i,url);
        }
        return (postImageUrl!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(postImageUrl):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}