package com.project.travel.service;

import com.project.travel.domain.UserImage;
import com.project.travel.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserImageService {
    @Autowired
    private final UserImageRepository userImageRepository;

    @Transactional
    public Long join(UserImage userImage){
        userImageRepository.save(userImage);
        return userImage.getId();
    }

}
