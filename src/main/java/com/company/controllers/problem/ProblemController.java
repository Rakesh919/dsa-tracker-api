package com.company.controllers.problem;

import com.company.constants.ErrorConstants;
import com.company.constants.SuccessConstants;
import com.company.dto.request.ProblemDTO;
import com.company.dto.response.SuccessResponse;
import com.company.entity.problems.Problem;
import com.company.service.problem.ProblemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    public ProblemService problemService;

    private static final Logger logger = LoggerFactory.getLogger(ProblemController.class);


    @GetMapping("/get")
    public ResponseEntity<?> getProblemController(@NotNull(message = "Id can't be null") @RequestParam int id){
        try{
            Problem details = problemService.getProblemById(id);
            if(details==null){
                logger.error("Details not found for this ID: {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.PROBLEM_NOT_FOUND);
            }
            return ResponseEntity.ok(new SuccessResponse(SuccessConstants.getSuccessCode(),SuccessConstants.getDetailsFetched(),details));
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> addProblem(@RequestBody ProblemDTO dto){
        try{
            Problem problem = new Problem();
            problem.setTitle(dto.getTitle());
            problem.setDescription(dto.getDescription());
            problem.setLanguage(dto.getLanguage());
            problem.setDifficulty(dto.getDifficulty());
            problem.setLinks(dto.getLinks());
            problem.setTopics(dto.getTopics());

           Problem savedDetails =  problemService.addProblem(problem);
           SuccessResponse response = new SuccessResponse("SUC-200","Details Added Successfully",savedDetails.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProblemsController(){
        try{
            List<Problem> list  = problemService.getProblems();
            return ResponseEntity.ok(new SuccessResponse("SUC-200","List Fetched Successfully",list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/")
    public ResponseEntity<?> deleteProblem(@NotNull(message = "Id can't be null") @RequestParam int id){
        try{
            boolean isDeleted = problemService.deleteProblemById(id);
            if(!isDeleted){
                logger.error("Problem not found for this ID: {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.PROBLEM_NOT_FOUND);
            }
            return ResponseEntity.ok(new SuccessResponse("SUC-200","Problem Deleted Successfully",true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
