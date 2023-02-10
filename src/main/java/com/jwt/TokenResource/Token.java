package com.jwt.TokenResource;

import com.jwt.TokenResource.Utils.Authentication.AuthenticationRequest;
import com.jwt.TokenResource.Utils.Authentication.AuthenticationResponse;
import com.jwt.TokenResource.Utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class Token {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @RequestMapping("/Hi")
    public String getNewToken(){
        return "HelloToken";
    }
    @RequestMapping(value ="/Authenticate",method = RequestMethod.POST)
    public ResponseEntity<?> createAutheticationtoken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
       try{
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
       }catch (BadCredentialsException e){
           throw  new Exception("incorrect username or password",e);
       }


        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
