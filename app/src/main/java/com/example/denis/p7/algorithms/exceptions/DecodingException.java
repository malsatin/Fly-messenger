package com.example.denis.p7.algorithms.exceptions;

/**
 * Exception of an unresolved mistake while decoding.
 * <p>
 * Created by Denis Chernikov on 2017-11-05
 */
public class DecodingException extends Exception {

    public DecodingException() {
        super();
    }

    public DecodingException(String message) {
        super(message);
    }

}
