package com.backend.globeonclick.authentication;

import com.backend.globeonclick.authentication.request.AuthenticationRequest;
import com.backend.globeonclick.authentication.request.RegisterRequest;
import com.backend.globeonclick.authentication.response.AuthenticationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (
            @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (
            @RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthenticationResponse> adminLogin(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.adminLogin(request));
    }
}
