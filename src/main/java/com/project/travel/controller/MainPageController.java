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

        return (userService.calRecommendTag(userId)!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(userService.calRecommendTag(userId)):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    /**
     * 메인 페이지에 추천 게시물들 전송
     */
    @GetMapping("main/{userId}/posts")
    public ResponseEntity<List<List<Post>>> recommendPost(@PathVariable("userId") Long userId){

        List<Tag> tags=userService.calRecommendTag(userId); //추천 태그 목록
        List<List<Post>> posts=new ArrayList<>();
        for(int i=0;i<tags.size();i++){
            List<Post> posts1=postService.searchPosts(tags.get(i).getTagContent());
            posts.add(i,posts1);
        }
        return (!posts.isEmpty()) ?
                ResponseEntity.status(HttpStatus.OK).body(posts):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
