package com.dailycodework.shoppingcart.request;


import lombok.Data;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}