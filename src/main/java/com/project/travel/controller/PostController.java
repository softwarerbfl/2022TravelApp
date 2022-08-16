package com.project.travel.controller;

import com.project.travel.domain.*;
import com.project.travel.repository.TagRepository;
import com.project.travel.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


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

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final AwsS3Service awsS3Service;
    private final TagRepository tagRepository;
    private String find = new String();

    String url = "https://travel-app-image-bucket.s3.ap-northeast-2.amazonaws.com/33b1742a-1693-4aed-a08d-ea4ee7293f6b.png";

    /**
     * 게시물 업로드
     * @param form
     * @return 완료 메시지
     */
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
                form.getSPlaces(),
                form.getMoney());
        return ResponseEntity.status(HttpStatus.OK).body("Post Success");
    }

    /**
     * 사용자가 find를 검색했을 때 user의 userSearch에 저장함과 동시에 검색한 게시물(Post)반환
     * @param find
     * @param userId
     * @return List<Post>
     */
    @Transactional
    @ResponseBody
    @GetMapping(value = "/search/{userId}/{find}")
    public ResponseEntity<List<Post>> searchPost(@PathVariable("find") String find, @PathVariable("userId") Long userId){
        Tag tag = new Tag();
        tag.setTagContent("find");
        tagRepository.save(tag);
        User user = userService.findOne(userId);
        user.getUserSearch().add(tag);
        userService.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(postService.searchPosts(find));
    }
    /**
     * 게시물 구경
     * @param postId
     * @return 게시물, 태그, 장소
     */
    @Transactional
    @ResponseBody
    @GetMapping(value = "/post/{postId}")
    public ResponseEntity<Post> viewPost(@PathVariable("postId") Long postId){
        return ResponseEntity.status(HttpStatus.OK).body(postService.viewPost(postId));
    }

    @Transactional
    @ResponseBody
    @GetMapping(value = "/post/{postId}/tags")
    public ResponseEntity<List<Tag>> viewPostTags(@PathVariable("postId") Long postId){
        Post post = postService.viewPost(postId);
        List<Tag> tags =new ArrayList<>();

        for (int j = 0; j < post.getTags().size(); j++) {
            Tag tag = post.getTags().get(j);
            tags.add(tag);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tags);
    }

    @Transactional
    @ResponseBody
    @GetMapping(value = "/post/{postId}/places")
    public ResponseEntity<List<Place>> viewPostPlaces(@PathVariable("postId") Long postId){
        Post post = postService.viewPost(postId);
        List<Place> places =new ArrayList<>();

        for (int j = 0; j < post.getTags().size(); j++) {
            Place place = post.getPlaces().get(j);
            places.add(place);
        }
        return ResponseEntity.status(HttpStatus.OK).body(places);
    }

    /**
     * 검색 창의 default 게시물들
     * @return List<Post>
     */
    @GetMapping(value = "/search/default")
    public ResponseEntity<List<Post>> getDefaultPosts(){
        return ResponseEntity.status(HttpStatus.OK).
                body(postService.defaultPosts());
    }

    /**
     * 검색창의 default 게시물들의 tags들
     * @return List<List<Post>>
     */
    @Transactional
    @ResponseBody
    @GetMapping(value = "/search/default/tags")
    public ResponseEntity<List<List<Tag>>> searchDefaultTags(){
        List<Post> posts = postService.defaultPosts();
        List<List<Tag>> tagss = new ArrayList<>();
        List<Tag> tags =new ArrayList<>();

        for (int i = 0; i<posts.size(); i++){
            for (int j = 0; j < posts.get(i).getTags().size(); j++) {
                Tag tag = posts.get(i).getTags().get(j);
                tags.add(tag);
            }
            tagss.add(tags);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tagss);
    }

    /**
     * 검색창의 default 게시물들의 places들
     * @return List<List<Place>>
     */
    @Transactional
    @ResponseBody
    @GetMapping(value = "/search/default/places")
    public ResponseEntity<List<List<Place>>> searchDefaultPlaces(){
        List<Post> posts = postService.defaultPosts();
        List<List<Place>> placess = new ArrayList<>();
        List<Place> places =new ArrayList<>();

        for (int i = 0; i<posts.size(); i++){
            for (int j = 0; j < posts.get(i).getPlaces().size(); j++) {
                Place place = posts.get(i).getPlaces().get(j);
                places.add(place);
            }
            placess.add(places);
        }
        return ResponseEntity.status(HttpStatus.OK).body(placess);
    }

        /**
     * 검색할 때 find가 포함된 게시물들의 태그(tag) 반환
     * @param find
     * @return List<List<Tag>>
     */
    @Transactional
    @ResponseBody
    @GetMapping(value = "/search/{find}/tags")
    public ResponseEntity<List<List<Tag>>> searchPostsTags(@PathVariable("find") String find){
        List<Post> posts = postService.searchPosts(find);
        List<List<Tag>> tagss = new ArrayList<>();
        List<Tag> tags =new ArrayList<>();

        for (int i = 0; i<posts.size(); i++){
            for (int j = 0; j < posts.get(i).getTags().size(); j++) {
                Tag tag = posts.get(i).getTags().get(j);
                tags.add(tag);
            }
            tagss.add(tags);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tagss);
    }
    /**
     * 검색할 때 find가 포함된 게시물의 장소(places) 반환
     * @param find
     * @return List<List<Place>>
     */
    @Transactional
    @ResponseBody
    @GetMapping(value = "/search/{find}/places")
    public ResponseEntity<List<List<Place>>> searchPostsPlaces(@PathVariable("find") String find){
        List<Post> posts = postService.searchPosts(find);
        List<List<Place>> placess = new ArrayList<>();
        List<Place> places =new ArrayList<>();

        for (int i = 0; i<posts.size(); i++){
            for (int j = 0; j < posts.get(i).getPlaces().size(); j++) {
                Place place = posts.get(i).getPlaces().get(j);
                places.add(place);
            }
            placess.add(places);
        }
        return ResponseEntity.status(HttpStatus.OK).body(placess);
    }

    /**
     * userId인 유저가 postId인 포스트에 좋아요를 눌렀을 때 좋아요 개수 증가하는 api
     * @param userId
     * @param postId
     * @return succeeded 메시지
     */
    @PostMapping(value = "/like/{userId}/{postId}")
    public ResponseEntity<String> likePost(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId){
        postService.likePost(userId , postId);
        return ResponseEntity.status(HttpStatus.OK).
                body("Succeeded Like!");
    }

}