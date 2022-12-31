package com.tm7xco.springrestserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessService {

    public void restrictedBusinessFunction() {
        log.info("Doing some restricted content!");
    }

}
