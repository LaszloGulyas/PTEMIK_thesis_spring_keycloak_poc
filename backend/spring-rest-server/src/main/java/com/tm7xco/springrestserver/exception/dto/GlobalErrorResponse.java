package com.tm7xco.springrestserver.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GlobalErrorResponse {

    private final String errorMessage;

}
