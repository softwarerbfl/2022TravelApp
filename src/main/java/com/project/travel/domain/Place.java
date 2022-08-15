package com.project.travel.domain;

import com.project.travel.domain.Image;
import com.project.travel.domain.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="place_id")
    private Long id; //장소의 고유 인덱스

    private String name; //장소 이름
    private Float score; //평점
    private String address; //주소
    private String content;//세부 내용

    @Enumerated(EnumType.STRING)
    private PlaceType placeType; //장소 타입(숙소, 식당, 관광지)

    @Embedded
    private Image image; //장소의 이미지

    @ManyToMany(mappedBy = "places")
    private List<Post> posts= new ArrayList<>();

}
