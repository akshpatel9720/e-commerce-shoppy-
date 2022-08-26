package com.user.serviceImpl;

import com.user.DTO.UserDTO;
import com.user.entity.UserEntity;
import com.user.repository.UserRepository;
import com.user.service.EmailService;
import com.user.service.UserService;
import com.user.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Override
    public Map<String, Object> getById(Long id) {
        Map<String, Object> map = new HashMap<>();
        Optional<UserEntity> user1 = userRepository.findById(id);
        if (user1.isPresent()) {

            map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.SUCCESS_MESSAGE_GET);
            map.put(ResponseMessage.DATA, user1);
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.FAIL_MESSAGE_GET);
            map.put(ResponseMessage.DATA, new ArrayList<>());
        }
        return map;
    }

    @Override
    public Map<String, Object> update(UserEntity userEntity) {
        Map<String, Object> map = new HashMap<>();
        Optional<UserEntity> existingUserEntity = userRepository.findById(userEntity.getId());
        if (existingUserEntity.isPresent()) {
            existingUserEntity.get().setUpdatedDate(LocalDateTime.now());
            existingUserEntity.get().setPhoneNumber(userEntity.getPhoneNumber());
            existingUserEntity.get().setFirstName(userEntity.getFirstName());
            existingUserEntity.get().setLastName(userEntity.getLastName());
            UserEntity updateUser = userRepository.save(existingUserEntity.get());
            map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.SUCCESS_MESSAGE_UPDATE);
            map.put(ResponseMessage.DATA, updateUser);
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.FAIL_MESSSAGE_UPDATE);
        }
        return map;
    }

    @Override
    public Map<String, Object> delete(Long id) {
        Map<String, Object> map = new HashMap<>();
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.SUCCESS_MESSAGE_DELETE);
            userRepository.deleteById(id);
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.FAIL_MESSAGE_DELETE);
        }
        return map;
    }

    @Override
    public Map<String, Object> updateEmail(Long id, String oldEmail, String newEmail) {
        Map<String, Object> map = new HashMap<>();
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            userEntity.setEmail(newEmail);
            userEntity.setUpdatedDate(LocalDateTime.now());
            userEntity.setActive(Boolean.TRUE);
            userRepository.save(userEntity);
            emailService.sendMailToUpdateEmail(userEntity);
            map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.SUCCESS_MESSAGE_UPDATE_EMAIL);
            map.put(ResponseMessage.DATA, userEntity);
        } else {
            map.put(ResponseMessage.STATUS, ResponseMessage.FAIL_API_CODE);
            map.put(ResponseMessage.MESSAGE, ResponseMessage.FAIL_MESSAGE_DELETE_EMAIL);
            map.put(ResponseMessage.DATA, new ArrayList<>());
        }
        return map;
    }

    @Override
    public Map<String, Object> getUser(UserDTO userDTO) {
        Map<String, Object> map = new HashMap<>();
        Boolean status = Boolean.valueOf(userDTO.getWhere().get("isActive").toString());
        Integer page = Integer.valueOf(userDTO.getPagination().get("page").toString());
        Integer pagesize = Integer.valueOf(userDTO.getPagination().get("rowsPerPage").toString());
        Page<UserEntity> userEntities = null;
        Pageable pageable = PageRequest.of(page, pagesize);
        List<UserEntity> userEntityList = new ArrayList<>();
        userEntities = userRepository.findByStatus(status, pageable);
        for (UserEntity userEntities1 : userEntities) {
            userEntityList.add(userEntities1);
        }
        map.put(ResponseMessage.STATUS, ResponseMessage.SUCCESS_API_CODE);
        map.put(ResponseMessage.MESSAGE, ResponseMessage.SUCCESS_MESSAGE_UPDATE_EMAIL);
        map.put(ResponseMessage.DATA, userEntityList);
        return map;
    }
}
