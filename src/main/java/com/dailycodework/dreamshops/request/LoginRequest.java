package com.dailycodework.dreamshops.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginRequest {
    @NotBlank //validate the input to make sure that the user does not submit empty fields
    private String email;
    @NotBlank
    private String password;
}
