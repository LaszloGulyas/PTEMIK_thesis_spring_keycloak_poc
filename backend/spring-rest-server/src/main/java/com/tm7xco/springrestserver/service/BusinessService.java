package com.tm7xco.springrestserver.service;

import com.tm7xco.springrestserver.controller.dto.BusinessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessService {

    public BusinessResponse restrictedBusinessFunction() {
        log.info("Doing some restricted content!");

        String response = "You have the following authorities: " +
                SecurityContextHolder.getContext().getAuthentication().getAuthorities() +
                "\n so you can access to this very secret content, coming from spring-rest-server!";

        return new BusinessResponse(response);
    }

}
