package com.company.service.problem;

import com.company.entity.problems.Problem;
import com.company.repository.problem.ProblemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {

    @Autowired
    public ProblemRepository problemRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProblemService.class);


    public Problem addProblem(Problem createDto){
        logger.info("Add Problem controller method started");
        try{
            return problemRepository.save(createDto);

        } catch (Exception e) {
            logger.error("Exception Occurred at add problem service method");
            throw new RuntimeException(e);
        }
    }

    public List<Problem> getProblems(){
        try {
            return problemRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Problem getProblemById(int id){
        try{
            return problemRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteProblemById(int id){
        try{
        Problem details = problemRepository.findById(id).orElse(null);
        if(details==null){
            logger.error("Details not found for this ID: {}",id);
            return false;
        }
        problemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Problem> findProblemByDifficultyLevel(String level){
        try{
           return problemRepository.findProblemsByDifficulty(level);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
}

}
