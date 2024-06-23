package com.voting.challenge.app.service;

import com.voting.challenge.app.repository.MemberRepository;
import com.voting.challenge.domain.Member;
import com.voting.challenge.domain.payload.*;
import com.voting.challenge.exception.UserAlreadyExistsException;
import com.voting.challenge.extension.TestContainerExtension;
import com.voting.challenge.infra.configuration.JwtTokenValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

public class MemberServiceTest extends TestContainerExtension {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void create() {
        final TokenResponse response = memberService.create(new CreateUserRequest("foo", "123", new CPF("22963812000")));
        final Member foo = memberRepository.findOneByCpf("22963812000");
        Assertions.assertThat(JwtTokenValidator.isValidToken(response.token(), foo)).isTrue();
    }

    @Test
    public void createWithCpfDot() {
        final TokenResponse response = memberService.create(new CreateUserRequest("foo", "123", new CPF("229.638.120-00")));
        final Member foo = memberRepository.findOneByCpf("22963812000");
        Assertions.assertThat(JwtTokenValidator.isValidToken(response.token(), foo)).isTrue();
    }

    @Test
    public void login() {
        memberService.create(new CreateUserRequest("foo", "123", new CPF("22963812000")));
        final TokenResponse response = memberService.login(new LoginRequest(new CPF("22963812000"), "123"));
        final Member foo = memberRepository.findOneByCpf("22963812000");
        Assertions.assertThat(JwtTokenValidator.isValidToken(response.token(), foo)).isTrue();
        Assertions.assertThat(response.name()).isEqualTo("foo'");
    }

    @Test
    public void invalidLogin() {
        memberService.create(new CreateUserRequest("foo", "123", new CPF("22963812000")));
        Assertions.assertThatThrownBy(() -> memberService.login(new LoginRequest(new CPF("22963812000"), "1234")))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Bad credentials");
    }

    @Test
    public void createRepeatedUser() {
        memberService.create(new CreateUserRequest("foo", "123", new CPF("22963812000")));
        Assertions.assertThatThrownBy(() -> memberService.create(new CreateUserRequest("foo", "123", new CPF("22963812000"))))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("CPF Already exists");
    }

    @Test
    public void info() {
        memberService.create(new CreateUserRequest("foo", "123", new CPF("22963812000")));
        final MemberInfo info = memberService.info("22963812000");
        Assertions.assertThat(info.name()).isEqualTo("foo");
    }

}