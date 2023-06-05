package com.example.sena.Configuration.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CodigoNotAvaliableException extends IllegalStateException {

    private String message;

    public CodigoNotAvaliableException(String message){
        super(message);
        this.message = message;
    }

}
