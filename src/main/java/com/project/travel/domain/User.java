package com.project.travel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.LifecycleState;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    //사용자의 추천 태그 계산
    public void calRecommendTag(){
        //모든 태그를 하나에 모음
        List<Tag> allTags=new ArrayList<>();
        for(int i=0;i<userSearch.size();i++){
            allTags.add(i,this.userSearch.get(i));
        }
        for(int j=userSearch.size();j<userSearch.size()+likePosts.size();j++){
            allTags.add(j,likePosts.get(j-userSearch.size()).getTags().get(j-userSearch.size()));
        }
        //중복 제거한 태그들의 개수를 셈
        HashMap<String,Integer> calc=new HashMap<>();
        for(int a=0;a<allTags.size();a++){
            calc.put(allTags.get(a).getTagContent(),0);
        }

        for(int b=0;b<allTags.size();b++){
            //b번째 태그의 개수 가져옴
            Integer tagContentCount=calc.get(allTags.get(b).getTagContent());
            tagContentCount+=1;
        }

        List<Map.Entry<String, Integer>> list_entries=new ArrayList<Map.Entry<String, Integer>>(calc.entrySet());
        Collections.sort(list_entries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //내림차순 정렬된 list_entries 내의 태그들을 차례대로 recommendTags에 추가가
        for(int i=0;i<list_entries.size();i++){
            Tag tag=new Tag();
            User user=this;
            tag.setTagContent(list_entries.get(i).getKey());
            tag.setUser(user);
            this.recommendTags.add(i,tag);
        }
    }
}