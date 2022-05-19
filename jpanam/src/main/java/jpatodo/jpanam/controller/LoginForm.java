package jpatodo.jpanam.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class LoginForm {

    @NotEmpty(message = "이메일은 필수입력사항 입니다.")
    private String email;

    @NotEmpty(message = "패스워드은 필수입력사항 입니다.")
    private String password;
}

