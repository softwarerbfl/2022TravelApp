package com.project.travel.repository;

import com.project.travel.domain.UserImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class UserImageRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(UserImage userImage){
        em.persist(userImage);
    }
}
