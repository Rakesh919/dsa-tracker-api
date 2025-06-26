package com.company.controllers.user;

import com.company.constants.ErrorConstants;
import com.company.dto.request.UserDTO;
import com.company.dto.response.SuccessResponse;
import com.company.entity.otp.OTP;
import com.company.entity.user.User;
import com.company.repository.user.UserRepository;
import com.company.service.email.EmailService;
import com.company.service.otp.OtpService;
import com.company.service.user.UserService;
import com.company.utils.JwtUtil;
import com.company.utils.PasswordUtil;
import com.company.utils.Util;
import jakarta.validation.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    private final EmailService emailService;

    private final Util util;

    private final PasswordUtil passwordUtil;

    private final JwtUtil jwtUtil;

    private final OtpService otpService;

    UserController(UserService userService,EmailService emailService, Util util,PasswordUtil passwordUtil, JwtUtil jwtUtil,OtpService otpService){
        this.userService = userService;
        this.emailService = emailService;
        this.util = util;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
        this.otpService = otpService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup-otp")
    public ResponseEntity<?> signupOtp(@RequestParam @Email(message = "Please enter a valid email") String email){
        try{
            if(email==null) {
                logger.error("Email not found in params");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.EMAIL_IS_REQUIRED);
            }

            boolean isExists = userService.isEmailExists(email);
            if(isExists){
                logger.error("Email already Exists in DB");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.EMAIL_ALREADY_EXISTS);
            }
            int otp = util.generateRandomNumber();

            otpService.updateOtp(email,otp);
            emailService.sendEmail(email,"Registration OTP",String.valueOf(otp));
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","OTP sent successfully",otp));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> addUserController(@RequestBody UserDTO dto){
        try{
            boolean isExists = userService.isEmailExists(dto.getEmail());
            if(isExists){
                logger.error("Email already Exists : {}",dto.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.EMAIL_ALREADY_EXISTS);
            }
            int otp = dto.getOtp();

            boolean isMatch = otpService.isMatch(dto.getEmail(),otp);
            if(!isMatch){
                logger.error("OTP didn't match for Email ID: {}",dto.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.OTP_NOT_MATCH);
            }

            String hashedPassword = passwordUtil.hashPassword(dto.getPassword());

            User details = new User();
            details.setEmail(dto.getEmail());
            details.setPassword(hashedPassword);
            details.setName(dto.getName());
            details.setScore(dto.getScore());

            User savedDetails = userService.addUserService(details);
            String user_id = String.valueOf(savedDetails.getId());
            String token = jwtUtil.generateToken(user_id,savedDetails.getEmail());
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","User Added Successfully",token));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> loginUserController(@RequestParam String email, @RequestParam String password){
        try{
            if(email==null){
                logger.error("Email is Required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.EMAIL_IS_REQUIRED);
            }

            if(password==null){
                logger.error("Password is Required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.PASSWORD_IS_REQUIRED);
            }

            boolean isMatch = userService.isEmailExists(email);
            if(!isMatch){
                logger.error("No record found for this Email {}",email);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.NO_RECORD_FOUND_FOR_EMAIL);
            }

            User isExists = userService.findByEmail(email);
            String hashed_password = isExists.getPassword();

            boolean isPasswordMatch = passwordUtil.matchPassword(password,hashed_password);
            if(!isPasswordMatch){
                logger.error("Password do not match for this Email {}",email);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.PASSWORD_DO_NOT_MATCH);
            }

            String username = String.valueOf(isExists.getId());
            String token = jwtUtil.generateToken(username,email);
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","User Logged In Successfully",token));
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }


    @PostMapping("/reset-otp")
    public ResponseEntity<?> resetPasswordOtp(@RequestParam @Email(message = "Please enter a valid email") String email){
        try{
            if(email==null) {
                logger.error("Email is Required, Not found in params");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.EMAIL_IS_REQUIRED);
            }

            boolean isExists = userService.isEmailExists(email);
            if(!isExists){
                logger.error("User Not found with this Email");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorConstants.NO_RECORD_FOUND_FOR_EMAIL);
            }
            int otp = util.generateRandomNumber();

            otpService.updateOtp(email,otp);
            emailService.sendEmail(email,"OTP for password reset",String.valueOf(otp));
            return ResponseEntity.ok(new SuccessResponse("SUCCESS","OTP sent successfully",otp));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public ResponseEntity<?> resetPasswordController(@RequestParam String email, int otp){
//        try{
//
//            if(email==null || !otp){
//
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}
