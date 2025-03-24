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
 * Used when There was a problem with the data submitted, or some pre-condition of the API call wasn't satisfied (usually a client error 400)
 */
public class ApiFailedResponse implements Serializable {

    private final String status = "fail";

    private String message;


}
