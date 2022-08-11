package com.project.travel.service;

import com.project.travel.domain.User;
import com.project.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    public Long join(User user){
        validateDuplication(user);
        userRepository.save(user);
        return user.getId();
    }
    //중복되는 회원 이름 있을 경우 예외처리
    private void validateDuplication(User user){
        List<User> findMembers=userRepository.findByName(user.getUserName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    /**
     * 전체 회원 조회
     */
    public List<User> findMembers(){
        return userRepository.findAll();
    }
    public User findOne(Long userId){
        return userRepository.findOne(userId);
    }

}
