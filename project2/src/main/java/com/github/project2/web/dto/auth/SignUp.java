package com.github.project2.web.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUp {

    @NotBlank(message = "이메일을 입력하세요")
    @Size(max = 50, message = "이메일은 최대 50자까지 입력 가능합니다")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
            message = "비밀번호는 영문자와 숫자를 포함하여 8자 이상 20자 이하로 작성해야 합니다."
    )
    private String password;

    @NotBlank(message = "이름을 입력하세요")
    @Size(max = 20, message = "이름은 최대 20자까지 입력 가능합니다")
    private String name;

    @NotBlank(message = "주소를 입력하세요")
    @Size(max = 100, message = "주소는 최대 100자까지 입력 가능합니다")
    private String address;

    @Pattern(
            regexp = "^[MF]$",  // M 또는 F만 허용
            message = "성별은 'M' 또는 'F'만 입력 가능합니다."
    )
    private String gender;

    @NotBlank(message = "전화번호를 입력하세요")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 잘못되었습니다")
    private String phoneNum;
}
