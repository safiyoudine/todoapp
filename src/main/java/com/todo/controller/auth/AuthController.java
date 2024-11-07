package com.todo.controller.auth;

import com.todo.config.JwtTokenProvider;
import com.todo.dto.request.AuthRequest;
import com.todo.dto.request.SignupRequest;
import com.todo.dto.response.AuthDto;
import com.todo.dto.response.UserDto;
import com.todo.service.auth.AuthService;
import com.todo.service.user.CustomUserDetails;
import com.todo.service.user.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        logger.info("signupRequest - {}", signupRequest);
        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("User Allready Exist with this email: " + signupRequest.getEmail());
        }
        UserDto createdDto = authService.signupUser(signupRequest);
        if (createdDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Created!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthDto> signin(@RequestBody AuthRequest authRequest) {
        String username = authRequest.getEmail();
        String password = authRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String userRole = "";
        for (GrantedAuthority authority : authorities) {
            userRole = authority.getAuthority();
        }
        AuthDto authDto = new AuthDto(token, userRole, userId, true);

        return new ResponseEntity<AuthDto>(authDto, HttpStatus.OK);

    }

    private Authentication authenticate(String username, String password) {
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
              throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
             throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
