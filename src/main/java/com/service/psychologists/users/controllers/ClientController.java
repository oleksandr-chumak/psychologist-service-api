package com.service.psychologists.users.controllers;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.PublicClient;
import com.service.psychologists.users.services.ClientService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping(path = "/clients")
public class ClientController {

    private final ClientService clientService;

    private final Mapper<Client, PublicClient> publicClientMapper;

    public ClientController(final ClientService clientService, Mapper<Client, PublicClient> publicClientMapper) {
        this.clientService = clientService;
        this.publicClientMapper = publicClientMapper;
    }

    @GetMapping(path = "/me")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<PublicClient> show(Principal principal) {
        System.out.println(principal);
        Client client = clientService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        return new ResponseEntity<>(publicClientMapper.mapTo(client), HttpStatus.OK);
    }

    @PatchMapping(path = "/me")
    public String update() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Method not implemented");
    }
}
