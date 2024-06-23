package com.voting.challenge.infra.resource;

import com.voting.challenge.app.interfaces.AuthManager;
import com.voting.challenge.domain.payload.CreateUserRequest;
import com.voting.challenge.domain.payload.LoginRequest;
import com.voting.challenge.domain.payload.MemberInfo;
import com.voting.challenge.domain.payload.TokenResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthResource {

    private final AuthManager authManager;

    @PostMapping("/new")
    @ApiResponse(responseCode = "200", description = "User created successfully",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"name\": \"Eliana\",\n  \"password\": \"1496\",\n  \"cpf\": \"43945649803\"\n}")))
    public ResponseEntity<TokenResponse> create(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.ok(authManager.create(request));
    }

    @PostMapping("/login")
    @ApiResponse(responseCode = "200", description = "User created successfully",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\n  \"password\": \"1496\",\n  \"cpf\": \"43945649803\"\n}")))
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authManager.login(loginRequest));
    }

    @GetMapping("/info")
    public ResponseEntity<MemberInfo> info() {
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authManager.info(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

}
