package com.company.controllers.problem;

import com.company.constants.ErrorConstants;
import com.company.constants.SuccessConstants;
import com.company.dto.request.ProblemDTO;
import com.company.dto.response.SuccessResponse;
import com.company.entity.problems.Problem;
import com.company.entity.user.User;
import com.company.entity.problems.UserProblem;
import com.company.service.problem.ProblemService;
import com.company.service.user.UserService;
import com.company.service.problem.UserProblemService;
import com.company.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    public UserService userService;

    @Autowired
    public UserProblemService userProblemService;

    @Autowired
    public JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(ProblemController.class);


    @PostMapping("/create")
    public ResponseEntity<?> addProblem(@RequestBody ProblemDTO dto, HttpServletRequest request){
        try{

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);

            String email = jwtUtil.extractEmail(token);
            User user = userService.findByEmail(email);

            Problem problem = new Problem();
            problem.setTitle(dto.getTitle());
            problem.setDescription(dto.getDescription());
            problem.setLanguage(dto.getLanguage());
            problem.setDifficulty(dto.getDifficulty());
            problem.setLinks(dto.getLinks());

            Problem savedDetails =  problemService.addProblem(problem);

            UserProblem details = new UserProblem();
            details.setSolved(false);

            details.setProblem(savedDetails);
            details.setUser(user);
            int score = dto.getDifficulty().equals("EASY") ? 2 : dto.getDifficulty().equals("MEDIUM") ? 4 : 8;
            details.setPoints(score);

            userProblemService.addUserProblem(details);

            SuccessResponse response = new SuccessResponse("SUC-200","Details Added Successfully",savedDetails.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


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


    @GetMapping("/all")
    public ResponseEntity<?> getAllProblemsController(){
        try{
            List<Problem> list  = problemService.getProblems();
            return ResponseEntity.ok(new SuccessResponse("SUC-200","List Fetched Successfully",list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProblem(@NotNull(message = "Id can't be null") @RequestParam int id){
        try{
            Problem isExist = problemService.getProblemById(id);
            if(isExist==null){
                logger.error("Problem not found for this ID: {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.PROBLEM_NOT_FOUND);
            }

            userProblemService.findAndDeleteByProblemId(id);
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

    @GetMapping("/difficulty-level")
    public ResponseEntity<?> findByDifficultyLevel(@RequestParam String level){
        try{
            List<Problem> list = problemService.findProblemByDifficultyLevel(level);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Problems fetched by difficulty",list));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PutMapping("/edit")
    public ResponseEntity<?> editProblemController(@RequestParam int id, @RequestBody ProblemDTO dto){
        try{
            Problem isExist = problemService.getProblemById(id);
            if(isExist==null){
                logger.error("Problem Details not found for this ID: {}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorConstants.PROBLEM_NOT_FOUND);
            }
            if(dto.getDifficulty()!=null){
                isExist.setDifficulty(dto.getDifficulty());
            }

            if(dto.getTitle()!=null){
                isExist.setTitle(dto.getTitle());
            }

            if(dto.getLinks()!=null){
                isExist.setLinks(dto.getLinks());
            }

            if(dto.getLanguage()!=null){
                isExist.setLanguage(dto.getLanguage());
            }

            if(dto.getDescription()!=null){
                isExist.setDescription(dto.getDescription());
            }

            if(dto.getTopics()!=null){
                isExist.setTopics(dto.getTopics());
            }


            Problem updatedDetails = problemService.addProblem(isExist);

            return ResponseEntity.ok(new SuccessResponse("SUCCESS","Problem Details updated Successfully",updatedDetails));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
