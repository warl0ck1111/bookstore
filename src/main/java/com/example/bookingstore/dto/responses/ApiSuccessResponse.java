package com.example.bookingstore.dto.responses;

import lombok.*;

import java.io.Serializable;

/**
 * @author Okala Bashir .O.
 * created at 18/10/2020
 */

@Getter
@Setter
@ToString
@Builder

/**
 * used when All went well, and (usually) some data was returned.(2XX)
 *
 */
public class ApiSuccessResponse implements Serializable {

    private final String status = "success";

    private Object data;
    private String message;

    public ApiSuccessResponse(Object data, String message) {
        this.data = data;
        this.message = message;
    }


    public ApiSuccessResponse(String message) {
        this.message = message;
    }
}
