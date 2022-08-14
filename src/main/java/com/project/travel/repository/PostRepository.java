package com.project.travel.repository;

import com.project.travel.domain.Post;
import com.project.travel.domain.Tag;
import com.project.travel.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostRepository{

    @PersistenceContext
    private EntityManager em;

    public void save(Post post){
        em.persist(post);
    }
    public Post findOne(Long id){
        Post post=em.find(Post.class, id);
        return post;
    }

    /**
     * 좋아요 개수가 높은 순서로 모든 게시물(post)들을 return
     * @return List<Post>
     */
    public List<Post> findAll() {
        try{
            return em.createQuery("select u from Post u order by u.likes desc", Post.class)
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }

    }

    public List<Post> findByHashtag(Tag tag){
        String t = tag.getTagContent();
        return em.createQuery("select u from Post u where u.tags= : t", Post.class)
                .setParameter("t",t)
                .getResultList();
    }




}