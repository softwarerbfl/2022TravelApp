package com.project.travel.service;

import com.project.travel.controller.PostForm;
import com.project.travel.domain.*;
import com.project.travel.domain.Place;
import com.project.travel.repository.PlaceRepository;
import com.project.travel.repository.PostRepository;
import com.project.travel.repository.TagRepository;
import com.project.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PlaceRepository placeRepository;
    private final TagRepository tagRepository;

    //게시물 작성
    public Long savePost(Long userId, String title,
                         List<String> sTags,
                         List<PlaceType> placeTypes,
                         List<Float> scores,
                         List<String> contents,
                         List<Image> images,
                         List<Integer> days,
                         List<String> sPlaces,
                         Long money,
                         List<String> addresses) {

        User user = userRepository.findOne(userId);

        List<Place> places = new ArrayList<>();
        for (int i = 0; i < sPlaces.size(); i++) {
            Place place = new Place();
            place.setName(sPlaces.get(i));
            place.setScore(scores.get(i));
            place.setContent(contents.get(i));
            place.setImage(images.get(i));
            place.setPlaceType(placeTypes.get(i));
            place.setAddress(addresses.get(i));
            placeRepository.save(place);
            places.add(place);
        }

        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < sTags.size(); i++) {
            Tag tag = new Tag();
            tag.setTagContent(sTags.get(i));
            tagRepository.save(tag);
            tags.add(tag);
        }

        Post post = Post.createPost(user, title, tags, days, places, money);
        postRepository.save(post);

        return post.getId();
    }

    //게시물 보기
    public Post viewPost(Long postId) {
        return postRepository.findOne(postId);
    }

    //자기가 쓴 게시물 보기
    public List<Post> viewMyPost(Long userId){
        List<Post> posts=postRepository.findByUserId(userId);
        return posts;
    }
    //게시물들 첫 번째 이미지의 url 전송
    public String viewPostImageUrl(Long postId){
        Post post=postRepository.findOne(postId);
        String url=post.getPlaces().get(0).getImage().getFileUrl();
        return url;
    }
    // 좋아요 가장 많은 순으로 정렬해서 리턴
    public List<Post> defaultPosts() {
        if (postRepository.findAll().isEmpty()) {
            throw new IllegalStateException("글이 없습니다.");
        }
        return postRepository.findAll();
    }

    // 검색받은 태그에 따른 글 리턴
    public List<Post> searchPosts(String sTag) {
        if (postRepository.findByHashtag(sTag).isEmpty()) {
            throw new IllegalStateException("글이 없습니다.");
        }
        return postRepository.findByHashtag(sTag);
    }

    //포스트 좋아요
    public void likePost(Long userId, Long postId) {
        //사용자가 중복하여 한 게시물에 좋아요를 누르는 경우
        if (userRepository.findOne(userId).getLikePosts().contains(postRepository.findOne(postId))) {
            return;
        }
        userRepository.findOne(userId).getLikePosts().add(postRepository.findOne(postId));
        postRepository.findOne(postId).getLikeUsers().add(userRepository.findOne(userId));
        //post 좋아요 +1
        postRepository.findOne(postId).addLike();
    }

    //좋아요한 게시물(post) 조회
    public List<Post> viewLikePost(Long userId){
        User user=userRepository.findOne(userId);
        List<Post> likePosts= user.getLikePosts();

        return likePosts;
    }
}
