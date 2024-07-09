package com.eduprime.eduprimeserver.services;


import com.eduprime.eduprimeserver.dtos.BaseResponse;
import com.eduprime.eduprimeserver.dtos.UserDto;
import com.eduprime.eduprimeserver.dtos.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {

    BaseResponse<?> loginUser(LoginRequest request);

    BaseResponse<?> register(UserDto userDto);

    BaseResponse<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
