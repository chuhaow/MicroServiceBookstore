package com.cwen.cart_service.domain;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getLoginUsername(){
        return "user";
    }

}
