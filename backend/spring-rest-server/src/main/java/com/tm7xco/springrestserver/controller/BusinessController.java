package com.tm7xco.springrestserver.controller;

import com.tm7xco.springrestserver.controller.dto.BusinessResponse;
import com.tm7xco.springrestserver.service.BusinessService;
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
    public ResponseEntity<BusinessResponse> executeUserContent() {
        log.info("Processing incoming GET request (/api/business/user/execute) started...");

        BusinessResponse response = businessService.restrictedBusinessFunction();

        log.info("Processing incoming GET request (/api/business/user/execute) finished!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/super-user/execute")
    public ResponseEntity<BusinessResponse> executeSuperUserContent() {
        log.info("Processing incoming GET request (/api/business/super-user/execute) started...");

        BusinessResponse response = businessService.restrictedBusinessFunction();

        log.info("Processing incoming GET request (/api/business/super-user/execute) finished!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/execute")
    public ResponseEntity<BusinessResponse> executeAdminContent() {
        log.info("Processing incoming GET request (/api/business/admin/execute) started...");

        BusinessResponse response = businessService.restrictedBusinessFunction();

        log.info("Processing incoming GET request (/api/business/admin/execute) finished!");
        return ResponseEntity.ok(response);
    }

}
