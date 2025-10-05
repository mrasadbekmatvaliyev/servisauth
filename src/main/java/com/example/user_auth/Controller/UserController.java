package com.example.user_auth.Controller;

import com.example.user_auth.Model.User;
import com.example.user_auth.Model.OtpCode;
import com.example.user_auth.Service.UserService;
import com.example.user_auth.Service.OtpService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.user_auth.Repository.OtpCodeRepository;
import com.example.user_auth.Repository.UserRepository;
import com.example.user_auth.Service.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final OtpService otpService;
    private final OtpCodeRepository otpRepository;
    private final UserRepository userRepository;

    public UserController(UserService userService, OtpService otpService, OtpCodeRepository otpRepository, UserRepository userRepository) {
        this.userService = userService;
        this.otpService = otpService;
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
    }

    // ✅ JWT bilan himoyalangan API
    @GetMapping("/users")
    public List<User> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        // Header: "Bearer eyJhbGciOi..."
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        // Tokenni tekshirish
        if (!JwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        // Token ichidan foydalanuvchi raqamini olish
        String phone = JwtUtil.extractUsername(token);

        // ✅ Barcha foydalanuvchilarni qaytarish
        return userRepository.findAll();
    }

    @PostMapping("/add")
    public Map<String, Object> addUser(@RequestBody Map<String, String> request) {
        Long telegramId = Long.valueOf(request.get("telegram_id"));
        String firstName = request.get("first_name");
        String lastName = request.getOrDefault("last_name", null);
        String phone = request.get("phone");

        User user = userService.addOrGetUser(telegramId, firstName, lastName, phone);
        OtpCode otp = otpService.generateOtp(user);

        Map<String, Object> res = new HashMap<>();
        res.put("status", "ok");
        res.put("otp", otp.getCode());
        res.put("expires_at", otp.getExpiresAt());
        return res;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> body) {
        String code = body.get("code");

        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "OTP code is required"));
        }

        var otpOpt = otpRepository.findTopByCodeAndIsUsedFalseOrderByIdDesc(code);

        if (otpOpt.isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("error", "Invalid or already used code"));
        }

        OtpCode otp = otpOpt.get();
        

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(400).body(Map.of("error", "OTP code expired"));
        }

        User user = otp.getUser();

        // 🔹 JWT token yaratamiz
        String token = JwtUtil.generateToken(user.getPhone());

        // Belgilaymiz: bu kod ishlatildi
        otp.setUsed(true);
        otpRepository.save(otp);

        return ResponseEntity.ok(Map.of(
            "message", "OTP verified successfully",
            "user_id", otp.getUser().getId(),
            "token", token,
            "telegram_id", otp.getUser().getTelegramId()
        ));
    }
}
