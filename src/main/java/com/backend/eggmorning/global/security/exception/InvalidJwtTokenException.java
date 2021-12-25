package com.backend.eggmorning.global.security.exception;

import com.backend.eggmorning.global.security.exception.abstraction.JwtRelatedException;

public class InvalidJwtTokenException extends JwtRelatedException {
    public InvalidJwtTokenException(){
        super("Token is invalid");
    }
}
