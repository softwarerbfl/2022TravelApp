package com.project.travel.repository;

import com.project.travel.domain.Place;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PlaceRepository {

    @PersistenceContext
    private EntityManager em;


    public void save(Place place){
        em.persist(place);
    }

    public Place findOne(Long id){
        Place place=em.find(Place.class, id);
        return place;
    }

    public Place findByName(String name){
        return em.find(Place.class, name);
    }



}