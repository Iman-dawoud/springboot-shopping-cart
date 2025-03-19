package com.dailycodework.shoppingcart.response;


//this is class is to return data to frontend

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse {
    private String message;
    private Object data;



}
