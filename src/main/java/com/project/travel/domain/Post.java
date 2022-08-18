package com.project.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Proxy(lazy = false)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id; //게시물 고유 인덱스

    private String title; //게시물 제목
    private Long likes; //좋아요 개수
    private Double score; //게시물 평점
    private Long money; //예산
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id")
    private User user; //게시물 작성자

    @JsonIgnore
    @ManyToMany
    @JoinColumn(name="id")
    private List<User> likeUsers;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name="post_place",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="place_id"))
    private List<Place> places=new ArrayList<>(); //게시물 내의 장소들


    @JsonIgnore
    @ManyToMany
    @JoinTable(name="post_tag",
            joinColumns = @JoinColumn(name="post_id"),
            inverseJoinColumns = @JoinColumn(name="tag_id"))
    private List<Tag> tags= new ArrayList<>(); //게시물 내의 해시테그

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> day=new ArrayList<>(); //몇 번째 날인지에 대한 정보

    public static Post createPost(User user, String title, List<Tag> tags,
                                  List<Integer> days,
                                  List<Place> places,
                                  Long money) {
        Post post = new Post();
        post.setUser(user);
        post.setTitle(title);
        post.setTags(tags);
        post.setMoney(money);
        post.setPlaces(places);
        post.setDay(days);
        post.setLikes(0L); //좋아요 0으로 초기화

        // 평균평점 계산
        Double s = 0.0;
        for (int i = 0; i < post.getPlaces().size(); i++){
            s += post.getPlaces().get(i).getScore();
        }
        s = s/post.getPlaces().size();
        s=Double.valueOf(Math.round(s));
        post.setScore(s);

        return post;

    }

    public void addLike() {
        this.likes += 1;
    }
}
