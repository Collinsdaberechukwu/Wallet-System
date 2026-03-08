package com.collins.Wallet.System.dtos.ResquestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequestDto {

    @NotEmpty(message = "Name can not be null or empty")
    @Size(min = 5,max = 30,message = "The length of the customer name should be between 5 and 30")
    private String fullName;

    @NotEmpty(message = "Email address can not be null or empty")
    @Email(message = "Email address should be a valid value")
    private String email;
}
