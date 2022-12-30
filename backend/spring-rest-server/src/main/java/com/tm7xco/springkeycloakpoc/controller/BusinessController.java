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

    @GetMapping("/user/execute")
    public ResponseEntity<Void> executeUserContent() {
        log.info("Processing incoming GET request (/api/business/user/execute) started...");

        businessService.restrictedBusinessFunction();

        log.info("Processing incoming GET request (/api/business/user/execute) finished!");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/super-user/execute")
    public ResponseEntity<Void> executeSuperUserContent() {
        log.info("Processing incoming GET request (/api/business/super-user/execute) started...");

        businessService.restrictedBusinessFunction();

        log.info("Processing incoming GET request (/api/business/super-user/execute) finished!");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/execute")
    public ResponseEntity<Void> executeAdminContent() {
        log.info("Processing incoming GET request (/api/business/admin/execute) started...");

        businessService.restrictedBusinessFunction();

        log.info("Processing incoming GET request (/api/business/admin/execute) finished!");
        return ResponseEntity.ok().build();
    }

}
