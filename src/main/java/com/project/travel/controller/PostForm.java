package com.project.travel.controller;

import com.project.travel.domain.Image;
import com.project.travel.domain.PlaceType;
import com.project.travel.domain.UserImage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter @Setter
//@NotEmpty
public class PostForm {

    private Long userId;
    private String title;
    private List<String> sTags;
    private List<PlaceType> placeTypes;
    private List<Float> scores;
    private List<String> contents;
    private List<MultipartFile> images;
    private List<Integer> days;
    private List<String> sPlaces;
}