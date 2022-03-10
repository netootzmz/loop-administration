package com.smart.ecommerce.administration.service.impl;

public class test {


    public static void main(String[] args) {
        org.springframework.security.crypto.password.PasswordEncoder encoder
                = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String pass = encoder.encode("K12345");
        System.out.println("   *******+" + pass);
    }
}
