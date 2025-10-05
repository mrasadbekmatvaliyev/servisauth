package com.example.user_auth.Service;

import com.example.user_auth.Model.OtpCode;
import com.example.user_auth.Model.User;
import com.example.user_auth.Repository.OtpCodeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {
    private final OtpCodeRepository otpCodeRepository;
    private final Random random = new Random();

    public OtpService(OtpCodeRepository otpCodeRepository) {
        this.otpCodeRepository = otpCodeRepository;
    }

    public OtpCode generateOtp(User user) {
        String code = String.format("%05d", random.nextInt(100000));
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(2);
        OtpCode otp = new OtpCode(user, code, expiresAt);
        return otpCodeRepository.save(otp);
    }
}
