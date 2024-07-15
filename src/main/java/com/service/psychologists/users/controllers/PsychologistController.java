package com.service.psychologists.users.controllers;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/psychologists")
public class PsychologistController {

    @GetMapping(path = "/")
    public String index() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Method not implemented");
    }


    @GetMapping(path = "/{id}")
    public String show(@PathVariable String id) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Method not implemented");
    }

    @GetMapping(path = "/me")
    public String showMe() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Method not implemented");
    }

    @PatchMapping(path = "/me")
    public String update() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Method not implemented");
    }


}
