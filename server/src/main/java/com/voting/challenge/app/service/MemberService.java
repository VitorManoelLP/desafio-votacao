package com.voting.challenge.app.service;

import com.voting.challenge.app.interfaces.AuthManager;
import com.voting.challenge.app.repository.MemberRepository;
import com.voting.challenge.domain.Member;
import com.voting.challenge.domain.payload.CreateUserRequest;
import com.voting.challenge.domain.payload.LoginRequest;
import com.voting.challenge.domain.payload.MemberInfo;
import com.voting.challenge.domain.payload.TokenResponse;
import com.voting.challenge.exception.UserAlreadyExistsException;
import com.voting.challenge.infra.configuration.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberService implements AuthManager {

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;

    @Override
    public TokenResponse create(CreateUserRequest request) {
        verifyUserAlreadyExists(request);
        final Member member = memberRepository.save(Member.of(request));
        Object principal = getAuthenticate(member, request.password()).getPrincipal();
        return new TokenResponse(member.getName(), JwtTokenValidator.generateToken((UserDetails) principal));
    }

    @Override
    public TokenResponse login(final LoginRequest loginRequest) {
        final Member member = memberRepository.findOneByCpf(loginRequest.cpf().value());
        final Authentication authenticated = getAuthenticate(member, loginRequest.password());
        return new TokenResponse(member.getName(), JwtTokenValidator.generateToken((UserDetails) authenticated.getPrincipal()));
    }

    @Override
    public MemberInfo info(String cpf) {
        return new MemberInfo(memberRepository.findOneNameByCpf(cpf));
    }

    private Authentication getAuthenticate(Member member, String loginRequest) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getUsername(), loginRequest));
    }

    private void verifyUserAlreadyExists(CreateUserRequest request) {
        if (memberRepository.alreadyExists(request.cpf().value())) {
            throw new UserAlreadyExistsException("CPF Already exists");
        }
    }

}
