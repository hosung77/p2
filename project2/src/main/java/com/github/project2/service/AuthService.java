package com.github.project2.service;


import com.github.project2.config.security.JwtTokenProvider;
import com.github.project2.repository.roles.RoleRepository;
import com.github.project2.repository.roles.Roles;
import com.github.project2.repository.user.UserEntity;
import com.github.project2.repository.user.UserRepository;
import com.github.project2.repository.userPrincipal.UserPrincipal;
import com.github.project2.repository.userPrincipal.UserPrincipalRepository;
import com.github.project2.repository.userPrincipal.UserPrincipalRoles;
import com.github.project2.repository.userPrincipal.UserPrincipalRolesRepository;
import com.github.project2.service.exceptions.NotAcceptException;
import com.github.project2.service.exceptions.NotFoundException;
import com.github.project2.web.dto.auth.Login;
import com.github.project2.web.dto.auth.SignUp;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserPrincipalRepository userPrincipalRepository;
    private final RoleRepository roleRepository;
    private final UserPrincipalRolesRepository userPrincipalRolesRepository;


    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional(transactionManager = "tm")
    public boolean signUp(@Valid SignUp signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String username = signUpRequest.getName();
        String address = signUpRequest.getAddress();
        String phoneNum = signUpRequest.getPhoneNum();
        String gender = signUpRequest.getGender();



        if (userPrincipalRepository.existsByEmail(email)) {
            return false;
        }
        // 2. 유저가 있으면 ID만 등록 아니면 유저도 만들기
        UserEntity userFound = userRepository.findByUserName(username)
                .orElseGet(() -> userRepository.save(UserEntity.builder()
                                                    .userName(username)
                        .address(address != null ? address : "기본 주소")
                        .phoneNum(phoneNum != null ? phoneNum : "000-0000-0000")
                        .gender(gender)
                        .build()));

        // 3. UserName Password 등록, 기본 ROLE_USER
        Roles roles = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("ROLE_USER를 찾을 수가 없습니다."));
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .email(email)
                .user(userFound)
                .password(passwordEncoder.encode(password))
                .build();

        userPrincipalRepository.save(userPrincipal);
        userPrincipalRolesRepository.save(UserPrincipalRoles.builder()
                        .roles(roles)
                        .userPrincipal(userPrincipal)
                        .build());
        return true;
    }

    public String login(Login loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserPrincipal userPrincipal = userPrincipalRepository.findByEmailFetchJoin(email)
                    .orElseThrow(() -> new NotFoundException("유저 정보를 찾을 수 없습니다."));

            List<String> roles = userPrincipal.getUserPrincipalRoles()
                    .stream().map(UserPrincipalRoles::getRoles)
                    .map(Roles::getName)
                    .collect(Collectors.toList());

            return jwtTokenProvider.createToken(email, roles);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotAcceptException("로그인 할 수 없습니다.");
        }
    }

//
//    public void logout(HttpServletRequest request) {
//        String token = jwtTokenProvider.resolveToken(request); // 토큰 추출
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            // JWT 토큰이 유효한 경우 로그아웃 처리 (블랙리스트에 추가 등)
//            jwtTokenProvider.blacklistToken(token); // 블랙리스트 처리 메서드 호출
//        }
//    }
}
