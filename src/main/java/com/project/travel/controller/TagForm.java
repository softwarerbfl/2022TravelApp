package com.project.travel.controller;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.SecondaryTable;

@Getter
@Setter
public class TagForm {
    private String tagContent; //검색한 tag의 내용
    private String userId; //검색한 사용자의 id
}
