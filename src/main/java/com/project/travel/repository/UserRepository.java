package com.project.travel.repository;

import com.project.travel.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Member;
import java.util.List;

@Repository
public class UserRepository {
    @PersistenceContext
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
    public List<User> findByName(String userName){
        return em.createQuery("select u from User u where u.userName= :userName",User.class)
                .setParameter("userName",userName)
                .getResultList();
    }
}
