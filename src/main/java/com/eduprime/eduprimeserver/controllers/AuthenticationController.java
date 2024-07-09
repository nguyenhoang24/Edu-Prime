package com.eduprime.eduprimeserver.controllers;

import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.UserDto;
import com.eduprime.eduprimeserver.dtos.request.LoginRequest;
import com.eduprime.eduprimeserver.services.AuthenticationService;
import com.eduprime.eduprimeserver.services.LogoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Authentication Controller")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final LogoutService logoutService;

    @Operation(summary = "Login", description = "Dang nhap")
    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse> loginUser(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(this.authenticationService.loginUser(request));
    }

    @Operation(summary = "Register", description = "Dang ky")
    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(this.authenticationService.register(userDto));
    }

//    @Operation(summary = "logout", description = "Dang xuat")
//    @PostMapping("/logout")
//    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        this.logoutService.logout(request, response, authentication);
//    }

    @Operation(summary = "logout", description = "Dang xuat")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return ResponseEntity.ok(this.authenticationService.logout(request, response, authentication));
    }
}
