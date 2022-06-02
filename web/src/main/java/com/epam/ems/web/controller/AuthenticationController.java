package com.epam.ems.web.controller;

import com.epam.ems.service.UserService;
import com.epam.ems.service.dto.UserDto;
import com.epam.ems.web.security.AuthRequest;
import com.epam.ems.web.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtProvider jwtProvider,
                                    UserService userService,
                                    PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication;

        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));

        return jwtProvider.generateToken((UserDetails) authentication.getPrincipal());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody @Valid UserDto userDto) {
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        userService.insert(userDto);

        Authentication authentication;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(),
                        userDto.getPassword()));

        return jwtProvider.generateToken((UserDetails) authentication.getPrincipal());
    }

}
