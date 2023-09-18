package com.backend.InternHub.Controllers.Login;


import com.backend.InternHub.Entities.user.UserEntity;
import com.backend.InternHub.Jwt.JwtUtil;
import com.backend.InternHub.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class UserLoginController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        UserEntity user = userRepository.findByEmail(email);
        log.info("password from front:: {}", password);

        if (user == null) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message","user does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        log.info("password from DB:: {}", user.getPassword());

        boolean passwordCheck = new BCryptPasswordEncoder().matches(password, user.getPassword());


        if (passwordCheck) {
            String token = jwtUtil.generateToken(email);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}