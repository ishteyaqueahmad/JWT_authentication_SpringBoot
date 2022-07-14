package com.learning.jwt.controller;

import com.learning.jwt.model.JwtRequest;
import com.learning.jwt.model.JwtResponse;
import com.learning.jwt.service.UserService;
import com.learning.jwt.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(){
        return "Welcome to Spring Security Learning.";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest)throws Exception{
         try {
             authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(
                             jwtRequest.getUserName(),
                             jwtRequest.getPassword()
                     )
             );
         }catch (BadCredentialsException e){
                throw  new Exception("INVALID CREDENTIAL",e);
         }
         final UserDetails userDetails=userService.loadUserByUsername(jwtRequest.getUserName());
         final String token= jwtUtility.generateToken(userDetails);
         return new JwtResponse(token);
    }
}
