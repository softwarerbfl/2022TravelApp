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
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        try{
            return em.createQuery("select u from Post u order by u.likes desc", Post.class)
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }

    }

    public List<Post> findByHashtag(String tag){
        try{
            return em.createQuery("select p from Post p inner join p.tags pt where pt.tagContent = :tag", Post.class)
                    .setParameter("tag",tag)
                    .getResultList();
        }catch(NoResultException e){
            return null;
        }

    }

}

