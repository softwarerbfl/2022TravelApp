package com.project.travel.service;

import com.project.travel.controller.UserForm;
import com.project.travel.domain.User;
import com.project.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    /**
     * 회원 가입
     */
    @Transactional
    public Long join(User user){
        validateDuplication(user);
        userRepository.save(user);
        return user.getId();
    }
    //아이디 중복 확인 함수
    private void validateDuplication(User user){
        User findUsers=userRepository.findByUserId(user.getUserId());
        if(findUsers.getUserId()!=null){
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    /**
     * 전체 회원 조회
     */
    public List<User> findMembers(){
        return userRepository.findAll();
    }

    /**
     * 회원 아이디로 회원 조회
     */
    public User findOne(Long userId){
        return userRepository.findOne(userId);
    }
    /**
     * 로그인 시 아이디에 해당하는 비밀번호가 일치할 경우 해당 USER객체 반환
     */
    public User loginCheck(UserForm dto){
        return userRepository.checkIdPassword(dto.getUserId(),dto.getUserPassword());
    }
}
