package com.sum.chatter.error;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;


public class SignInException extends HttpStatusCodeException {

    /***
     * This Exception send to HttpSatausCode 777.
     * if 777 code is received to client, client is redirect to sign in page
     */
    public SignInException(String statusText) {
        super(HttpStatusCode.valueOf(777), statusText);
    }

}
