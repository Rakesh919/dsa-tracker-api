package com.company.service.problem;

import com.company.entity.problems.UserProblem;
import com.company.repository.problem.UserProblemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProblemService {

    private static final Logger logger = LoggerFactory.getLogger(UserProblemService.class);

    @Autowired
    private UserProblemRepository userProblemRepository;

    public void addUserProblem(UserProblem dto){
        try{
            userProblemRepository.save(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateSolvedStatus(int id,boolean status){
        try{
            UserProblem isExists = userProblemRepository.findById(id).orElse(null);
            if(isExists==null){
                logger.error("Problem not found for this ID: {}",id);
                return false;
            }
            isExists.setSolved(status);
            userProblemRepository.save(isExists);
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserProblem> userProblems(int id){
        try{
            return userProblemRepository.findProblemByUserId(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<UserProblem> findProblemBySolvedStatus(int id,boolean isSolved){
        try{
            return userProblemRepository.findByUserIdAndSolved(id,isSolved);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void findAndDeleteByProblemId(int id){
        try{
            userProblemRepository.findByProblemIdAndDelete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void findAndDeleteByUserId(int id){
        try{
            userProblemRepository.findByUserIdAndDelete(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
