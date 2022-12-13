package com.tm7xco.springkeycloakpoc.controller;

import com.tm7xco.springkeycloakpoc.service.BusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business")
@Slf4j
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping("/execute")
    public ResponseEntity<Void> execute() {
        log.info("Processing incoming GET request (/api/business/execute) started...");

        businessService.restrictedBusinessFunction();

        log.info("Processing incoming GET request (/api/business/execute) finished!");
        return ResponseEntity.ok().build();
    }

}
