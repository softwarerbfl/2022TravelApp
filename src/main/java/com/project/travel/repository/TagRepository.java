package com.project.travel.repository;

import com.project.travel.controller.TagForm;
import com.project.travel.domain.Post;
import com.project.travel.domain.Tag;
import com.project.travel.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class TagRepository {
    private final EntityManager em;
    public TagRepository(EntityManager em) {
        this.em = em;
    }
    public void save(Tag tag){
        em.persist(tag);
    }

   //검색한 문자열이 포함된 게시물(Post) 리턴
    public List<Post> findByString(TagForm tagForm){
        Tag tag=new Tag();
        tag.setTagContent(tagForm.getTagContent());
        return em.createQuery("select t from Tag t where t.tagContent like :tagContent")
                .setParameter("tagContent",tagForm.getTagContent())
                .getResultList();
    }
}