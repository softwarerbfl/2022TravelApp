package com.project.travel.service;

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
    private  final PlaceRepository placeRepository;
    private  final TagRepository tagRepository;

    //게시물 작성
    public Long savePost(Long userId, String title,
                         List<String> sTags,
                         List<PlaceType> placeTypes,
                         List<Float> scores,
                         List<String> contents,
                         List<Image> images,
                         List<Integer> days,
                         List<String> sPlaces){

        User user = userRepository.findOne(userId);

        List<Place> places = new ArrayList<>();
        for (int i = 0; i < sPlaces.size();i++){
            Place place = new Place();
            place.setName(sPlaces.get(i));
            place.setScore(scores.get(i));
            place.setContent(contents.get(i));
            place.setImage(images.get(i));
            place.setPlaceType(placeTypes.get(i));
            placeRepository.save(place);
            places.add(place);
        }

        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < sTags.size();i++){
            Tag tag = new Tag();
            tag.setTagContent(sTags.get(i));
            tagRepository.save(tag);
            tags.add(tag);
        }

        Post post = Post.createPost(user, title, tags, days, places);
        postRepository.save(post);

        return post.getId();
    }

    //게시물 보기
    public Post viewPost(Long postId){
        return postRepository.findOne(postId);
    }

    // 좋아요 가장 많은 순으로 정렬해서 리턴
    public List<Post> defaultPosts(){
        if(postRepository.findAll().isEmpty()){
            throw new IllegalStateException("글이 없습니다.");
        }
        return postRepository.findAll();
    }

    // 검색받은 태그에 따른 글 리턴
    public List<Post> searchPosts(String sTag){
        if(postRepository.findByHashtag(sTag).isEmpty()){
            throw new IllegalStateException("글이 없습니다.");
        }
        return postRepository.findByHashtag(sTag);
    }

    //포스트 좋아요
    public void likePost(Long userId, Long postId){
        userRepository.findOne(userId).getLikePosts().add(postRepository.findOne(postId));
        postRepository.findOne(postId).getLikeUsers().add(userRepository.findOne(userId));

        //post 좋아요 +1
        postRepository.findOne(postId).addLike();

    }





}
