package com.project.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.LifecycleState;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User{
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn//(name="user_image_id")
    private UserImage userImage;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Tag> recommendTags=new ArrayList<>();


    //사용자가 검색했을 때 검색 기록(tag)을 추가
    public void addTag(Tag tag){
        userSearch.add(tag);
        tag.setUser(this);
    }


    /**
     * 비밀번호 확인
     * @param plainPassword 암호화 이전의 비밀번호
     * @param passwordEncoder 암호화에 사용된 클래스
     * @return true | false
     */
    public boolean checkPassword(String plainPassword, BCryptPasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(plainPassword, this.userPassword);
    }
}