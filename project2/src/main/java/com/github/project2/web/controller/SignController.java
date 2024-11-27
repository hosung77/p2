package com.github.project2.web.controller;

import com.github.project2.service.AuthService;
import com.github.project2.web.dto.auth.Login;
import com.github.project2.web.dto.auth.SignUp;


import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/api/sign")
public class SignController {

    private final AuthService authService;


    @PostMapping(value="/register")
    public String register(@RequestBody SignUp signUpRequest){
        boolean isSuccess  = authService.signUp(signUpRequest);
        return isSuccess ? "회원가입 성공하였습니다." : "회원가입 실패하였습니다.";
    }

    @PostMapping(value="/login")
    public String login(@RequestBody Login loginRequest, HttpServletResponse response){
        String token = authService.login(loginRequest);
        response.setHeader("X-AUTH-TOKEN", token);
        return "로그인이 성공하였습니다.";
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//        authService.logout(request); // AuthService의 로그아웃 처리 메서드 호출
//        return ResponseEntity.ok("로그아웃 성공");
//    }

}
