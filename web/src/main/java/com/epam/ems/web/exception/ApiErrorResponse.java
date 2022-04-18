package com.epam.ems.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {

    private int errorCode;
    private String errorMessage;

}
