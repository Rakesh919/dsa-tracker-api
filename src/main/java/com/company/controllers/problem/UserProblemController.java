package com.company.controllers.problem;

import com.company.constants.ErrorConstants;
import com.company.dto.response.SuccessResponse;
import com.company.entity.problems.UserProblem;
import com.company.entity.user.User;
import com.company.service.problem.UserProblemService;
import com.company.service.user.UserService;
import com.company.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user")
@RestController
public class UserProblemController {

    private static final Logger logger = LoggerFactory.getLogger(UserProblemController.class);

    @Autowired
    private UserProblemService userProblemService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @GetMapping("/user_problems")
    public ResponseEntity<?> getUserProblems(HttpServletRequest request){
        try{
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            String email = jwtUtil.extractEmail(token);

            User details  = userService.findByEmail(email);
            if(details ==null){
                logger.error("User not found for this Email {}",email);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.USER_NOT_FOUND);
            }

            int user_id = details.getId();
            List<UserProblem> list = userProblemService.userProblems(user_id);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","User Problems fetched Successfully",list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/user_solved")
    public ResponseEntity<?> getSolvedUnsolvedProblems(@RequestParam boolean status,HttpServletRequest request){
        try{
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            String email = jwtUtil.extractEmail(token);

            User details  = userService.findByEmail(email);
            int user_id = details.getId();

          List<UserProblem> list = userProblemService.findProblemBySolvedStatus(user_id,status);
          return ResponseEntity.ok(new SuccessResponse("SUCCESS","Problems fetched successfully",list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete-by-userId")
    public ResponseEntity<?> findAndDeleteUserProblems(HttpServletRequest request){
        try{
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            String email = jwtUtil.extractEmail(token);

            User details  = userService.findByEmail(email);
            int user_id = details.getId();

           List<UserProblem> list =  userProblemService.userProblems(user_id);

           if(list.isEmpty()){
               logger.error("No Problem found for this User ID : {}",user_id);
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.NO_PROBLEM_FOUND);
           }
           userProblemService.findAndDeleteByUserId(user_id);
           return ResponseEntity.ok(new SuccessResponse("SUCCESS","All Problems has been deleted for this user",true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
