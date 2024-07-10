package com.service.psychologists.auth.controllers;

import com.service.psychologists.auth.domain.dto.ClientDto;
import com.service.psychologists.auth.services.RegistrationService;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.PublicClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {

    final private Mapper<ClientDto, Client> clientDtoToClientMapper;

    final private Mapper<Client, PublicClient> clientToPublicClientMapper;

    final private RegistrationService<Client> clientRegistrationService;

    public AuthController(final Mapper<ClientDto, Client> clientDtoToClientMapper,
                          final Mapper<Client, PublicClient> clientToPublicClientMapper,
                          final RegistrationService<Client> clientRegistrationService) {
        this.clientDtoToClientMapper = clientDtoToClientMapper;
        this.clientToPublicClientMapper = clientToPublicClientMapper;
        this.clientRegistrationService = clientRegistrationService;
    }

    @PostMapping(path = "auth/login")
    public void login() {

    }

    @PostMapping(path = "auth/register/client")
    public ResponseEntity<PublicClient> registerClient(@RequestBody ClientDto clientDto) {
        boolean isUserAlreadyRegistered = clientRegistrationService.checkIsUserNotRegistered(clientDto.getEmail());

        if(isUserAlreadyRegistered) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        Client client = clientDtoToClientMapper.mapTo(clientDto);
        Client registeredClient = clientRegistrationService.register(client);

        return new ResponseEntity<>(clientToPublicClientMapper.mapTo(registeredClient), HttpStatus.CREATED);
    }

    @PostMapping(path = "auth/register/psychologist")
    public void registerPsychologist() {

    }
}
