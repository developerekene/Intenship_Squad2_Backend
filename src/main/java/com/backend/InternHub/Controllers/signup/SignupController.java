package com.backend.InternHub.Controllers.signup;

import com.backend.InternHub.Entities.user.UserEntity;
import com.backend.InternHub.responses.Response;
import com.backend.InternHub.exceptions.EtAuthException;
import com.backend.InternHub.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/users")
public class SignupController {
    @Autowired
    UserServiceImpl userServiceImpl;


    @PostMapping("/signup")
    public ResponseEntity<Response> signUp(@RequestBody UserEntity user){
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            userServiceImpl.registerUser(user);
            Response successful = new Response("User successfully registered");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(successful);
        } catch (EtAuthException ex) {
            // Handle the exception and return an appropriate response
            Response errorResponse = new Response(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
        }


    }
}