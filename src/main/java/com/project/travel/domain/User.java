package com.project.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.LifecycleState;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private String userName;
    private String userId;
    private String userPassword;
    private String userPasswordCheck;

    //사용자의 검색 기록
    @OneToMany(mappedBy="user")
    @JsonIgnore
    private List<Tag> userSearch=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Post> posts=new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "likeUsers", cascade=CascadeType.ALL)
    private List<Post> likePosts=new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="user_image_id")
    private UserImage userImage;

    //사용자가 검색했을 때 검색 기록(tag)을 추가
    public void addTag(Tag tag){
        userSearch.add(tag);
        tag.setUser(this);
    }
}