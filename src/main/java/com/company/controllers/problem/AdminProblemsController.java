package com.company.controllers.problem;

import com.company.constants.ErrorConstants;
import com.company.dto.response.SuccessResponse;
import com.company.entity.problems.AdminProblems;
import com.company.service.problem.AdminProblemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminProblemsController {

    private static final Logger logger = LoggerFactory.getLogger(AdminProblemsController.class);

    @Autowired
    private AdminProblemService adminProblemService;

    @PostMapping("/problem/add")
    public ResponseEntity<?> addProblemController(@RequestBody AdminProblems dto){
        try{
           AdminProblems savedDetails = adminProblemService.addProblem(dto);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Problem Added Successfully",savedDetails));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/problem/get")
    public ResponseEntity<?> getProblemController(@RequestParam int id){
        try{
            AdminProblems isExists = adminProblemService.getProblemById(id);
            if(isExists==null){
                logger.error("Problem not found for this ID :{}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.NO_PROBLEM_FOUND);
            }
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Problem Details fetched Successfully",isExists));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/problem/all")
    public ResponseEntity<?> getAllProblemsController(){
        try{
            List<AdminProblems> list = adminProblemService.getAllProblems();
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","All Problem Fetched Successfully",list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/problem/delete")
    public ResponseEntity<?> deleteProblemById(@RequestParam int id){
        try{
            AdminProblems isExist = adminProblemService.getProblemById(id);
            if(isExist==null){
                logger.error("No Problem found for this ID :{}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.NO_PROBLEM_FOUND);
            }

            boolean isDeleted = adminProblemService.deleteProblemById(id);

            if(!isDeleted){
                logger.error("Error in Deleting problem for this ID : {}",id);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.UNABLE_TO_DELETE_THE_PROBLEM);
            }
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Problem Deleted Successfully",true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/problem/edit")
    public ResponseEntity<?> editProblemController(@RequestBody AdminProblems dto){
        try{
            AdminProblems isExist = adminProblemService.getProblemById(dto.getId());
            if(isExist==null){
                logger.error("Problem does not found if DB for this ID: {}",dto.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.PROBLEM_NOT_FOUND);
            }
            if(dto.getName()!=null){
                isExist.setName(dto.getName());
            }

            if(!dto.getLinks().isEmpty()){
                isExist.setLinks(dto.getLinks());
            }
            AdminProblems savedDetails = adminProblemService.addProblem(isExist);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Details updated Successfully",savedDetails));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
