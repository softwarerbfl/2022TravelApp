package com.project.travel.controller;

import com.project.travel.domain.Image;
import com.project.travel.domain.PlaceType;
import com.project.travel.domain.UserImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PostForm {

    private Long userId; //유저 아이디
    private String title; //제목
    private List<String> sTags; //태그
    private List<PlaceType> placeTypes; //관광지/숙소/맛집
    private List<Float> scores; //평점
    private Long money;//예산
    private List<String> contents; //세부 내용
    private List<MultipartFile> images; //이미지
    private List<Integer> days; //몇일차인지
    private List<String> sPlaces; //장소 이름
    private List<String> addresses; //장소의 주소
}