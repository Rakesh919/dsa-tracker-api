package com.company.repository.opt;

import com.company.entity.otp.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OTP,Integer> {
 public OTP findByEmail(String email);
 public boolean existsByEmail(String email);
}
