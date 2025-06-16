package com.company.service.otp;

import com.company.entity.otp.OTP;
import com.company.repository.opt.OtpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final OtpRepository otpRepository;

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    OtpService(OtpRepository otpRepository){
        this.otpRepository = otpRepository;
    }

    public OTP saveOtp(OTP dto){
        try{
            return otpRepository.save(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmailExists(String email){
        try{
            return otpRepository.existsByEmail(email);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isMatch(String email,int otp){
        try{
            OTP isExists = otpRepository.findByEmail(email);
            return isExists != null && isExists.getOtp() == otp;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public OTP updateOtp(String email,int otp){
        try{
            OTP existingOtp = otpRepository.findByEmail(email);
            OTP details = new OTP();
            if(existingOtp==null){
                logger.info("OTP Details not found, Create a new record");
                details.setEmail(email);
                details.setOtp(otp);
                return otpRepository.save(details);
            }
          else{
                existingOtp.setOtp(otp);
                return otpRepository.save(existingOtp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
