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
@AllArgsConstructor


/**
 * Used when An error occurred in processing the request, i.e. an exception was thrown (usually internal server errors 500)
 */

public class ApiErrorResponse implements Serializable {

    private final String status = "error";
    private String message;


}
