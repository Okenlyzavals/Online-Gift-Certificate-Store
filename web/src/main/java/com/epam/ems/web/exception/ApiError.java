package com.epam.ems.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;
import java.util.ResourceBundle;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {

    private static final String BUNDLE_NAME = "locale";
    private String errorMessage;
    private int errorCode;


    public static String getLocalizedMessage(String messageKey, Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(messageKey);
    }

}
