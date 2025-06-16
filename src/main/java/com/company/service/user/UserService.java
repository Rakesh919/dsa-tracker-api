package com.company.service.user;

import com.company.entity.user.User;
import com.company.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User addUserService(User dto){
        try{
            return userRepository.save(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(int id){
        try{
            return userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteUser(int id){
        try{
            User isDetailsExist = userRepository.findById(id).orElse(null);
            if(isDetailsExist==null){
                logger.error("User Details not found for this ID: {}",id);
                return false;
            }
            userRepository.deleteById(id);
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmailExists(String email){
        try{
            return userRepository.existsByEmail(email);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
