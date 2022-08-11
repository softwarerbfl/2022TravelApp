package com.project.travel.repository;

import com.project.travel.controller.UserForm;
import com.project.travel.domain.User;
import com.project.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Member;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private EntityManager em;

    public void save(User user){
        em.persist(user);
    }

    public User findOne(Long id){
        User user=em.find(User.class, id);
        return user;
    }
    public List<User> findAll(){
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }
    public User findByUserId(String userId){
        return em.createQuery("select u from User u where u.userId= :userId", User.class)
                .setParameter("userId",userId)
                .getSingleResult();
    }


    public User checkIdPassword(String userId, String userPassword) {
        return em.createQuery("select u from User u where u.userId= :userId and u.userPassword= :userPassword", User.class)
                .setParameter("userId",userId)
                .setParameter("userPassword",userPassword)
                .getSingleResult();
    }
}
