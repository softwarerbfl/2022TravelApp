package com.project.travel.repository;

import com.project.travel.domain.UserImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class UserImageRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(UserImage userImage){
        em.persist(userImage);
    }
    public UserImage findUserImageByUser(Long userId){
        try{
            return em.createQuery("select i from UserImage i inner join i.user u where u.id = :userId", UserImage.class)
                    .setParameter("userId",userId)
                    .getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
