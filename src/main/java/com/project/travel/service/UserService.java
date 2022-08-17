package com.project.travel.service;

import com.project.travel.controller.UserForm;
import com.project.travel.domain.*;
import com.project.travel.repository.TagRepository;
import com.project.travel.repository.UserImageRepository;
import com.project.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final EntityManagerFactory emf;
    private final AwsS3Service awsS3Service;
    private final UserImageService userImageService;
    private final UserImageRepository userImageRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(User user){
        validateDuplication(user);
        userRepository.save(user);
        return user.getId();
    }
    @Transactional
    public void save(User user){
        userRepository.save(user);
    }


    //아이디 중복 확인 함수
    private void validateDuplication(User user){
        User findUser=userRepository.findByUserId(user.getUserId());
        if(findUser!=null){
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }
    /**
     * userId(String)로 user검색
     */
    public User findByUserId(String userId){
        User user=userRepository.findByUserId(userId);
        return user;
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
    @Transactional
    public User loginCheck(UserForm dto){
        return userRepository.checkIdPassword(dto.getUserId(),dto.getUserPassword());
    }

    /**
     * user가 find를 검색했을 때 검색 기록 저장
     * @param userId
     * @param find
     */
    @Transactional
    public void findAndSaveTag(Long userId, String find) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        Tag tag = new Tag();
        tag.setTagContent(find);

        User user = findOne(userId);
        em.detach(user);
        List<Tag> newUserSearch = user.getUserSearch();
        newUserSearch.add(tag);
        user.setUserSearch(newUserSearch);

        tag.setUser(user);
        tagRepository.save(tag);

        transaction.commit();
    }

    /**
     * user가 이미지를 등록
     * @param userId
     * @param multipartFile
     */
    @Transactional
    public UserImage findAndSaveUserImage(Long userId, List<MultipartFile> multipartFile) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        User user = findOne(userId);
        em.detach(user);

        //이미지 객체 생성
        List<String> s=awsS3Service.uploadImage(multipartFile);
        Image image=new Image();
        image.setFileName(s.get(0));
        image.setFileOriName(multipartFile.get(0).getOriginalFilename());
        image.setFileUrl("https://travel-app-image-bucket.s3.ap-northeast-2.amazonaws.com/"+s.get(0));

        //유저 이미지 객체 생성
        UserImage userImage=new UserImage();
        userImage.setImage(image);

        //유저 이미지 등록
        userImage.setUser(user);
        user.setUserImage(userImage);

        userImageRepository.save(userImage);

        transaction.commit();
        return userImage;
    }
    /**
     * user의 추천 태그 계산
     */
    @Transactional
    public List<Tag> calRecommendTag(Long userId){
        EntityManager em=emf.createEntityManager();
        EntityTransaction transaction=em.getTransaction();
        //transaction 시작
        transaction.begin();

        User user=userRepository.findOne(userId);
        em.detach(user);

        //태그 객체 생성
        List<Tag> recommend=new ArrayList<>();

        //태그 계산
        //모든 태그를 하나에 모음
        List<Tag> allTags=new ArrayList<>();
        for(int i=0;i<user.getUserSearch().size();i++){
            allTags.add(i,user.getUserSearch().get(i));
        }
        int index=user.getUserSearch().size();
        for(int j=user.getUserSearch().size();j<user.getUserSearch().size()+user.getLikePosts().size();j++){
            for(int k=0;k<user.getLikePosts().get(j-user.getUserSearch().size()).getTags().size();k++){
                allTags.add(index,user.getLikePosts().get(j-user.getUserSearch().size()).getTags().get(k));
                index+=1;
            }
        }
        //중복 제거한 태그들의 개수를 셈
        HashMap<String,Integer> calc=new HashMap<>();

        for(int a=0;a<allTags.size();a++){
            calc.put(allTags.get(a).getTagContent(),0);
        }

        for(int b=0;b<allTags.size();b++){
            //b번째 태그의 개수 가져옴
            Integer tagContentCount=calc.get(allTags.get(b).getTagContent());
            tagContentCount+=1;
            calc.put(allTags.get(b).getTagContent(),tagContentCount);
        }

        List<Map.Entry<String, Integer>> list_entries=new ArrayList<Map.Entry<String, Integer>>(calc.entrySet());
        Collections.sort(list_entries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //내림차순 정렬된 list_entries 내의 태그들을 차례대로 recommend에 추가
        for(int i=0;i<list_entries.size();i++){
            Tag tag=new Tag();
            tag.setTagContent(list_entries.get(i).getKey());
            tag.setUser(user);
            recommend.add(i,tag);
        }

        //사용자의 추천 태그로 등록
        user.setRecommendTags(recommend);


        //transaction 닫힘
        transaction.commit();

        return recommend;
    }
}
