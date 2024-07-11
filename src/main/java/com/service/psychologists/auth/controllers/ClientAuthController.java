package com.service.psychologists.auth.controllers;

import com.service.psychologists.auth.domain.dto.RegisterClientDto;
import com.service.psychologists.auth.services.AuthService;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.PublicClient;
import jakarta.validation.Valid;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(path = "/auth")
public class ClientAuthController {

    final private Mapper<RegisterClientDto, Client> registerClientDtoToClientMapper;

    final private Mapper<Client, PublicClient> clientToPublicClientMapper;

    final private AuthService<Client> authService;


    public ClientAuthController(
            final Mapper<RegisterClientDto, Client> registerClientDtoToClientMapper,
            final Mapper<Client, PublicClient> clientToPublicClientMapper,
            final AuthService<Client> authService
    ) {
        this.registerClientDtoToClientMapper = registerClientDtoToClientMapper;
        this.clientToPublicClientMapper = clientToPublicClientMapper;
        this.authService = authService;
    }

    @PostMapping(path = "/register/client")
    public ResponseEntity<PublicClient> register(@Valid @RequestBody RegisterClientDto registerClientDto) {
        boolean isUserAlreadyRegistered = authService.checkIsUserNotRegistered(registerClientDto.getEmail());

        if (isUserAlreadyRegistered) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        Client client = registerClientDtoToClientMapper.mapTo(registerClientDto);
        Client registeredClient = authService.register(client);

        return new ResponseEntity<>(clientToPublicClientMapper.mapTo(registeredClient), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login/client")
    public void login() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Method not implemented");
    }
}
