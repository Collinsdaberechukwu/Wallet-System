package com.collins.Wallet.System.dtos.ResquestDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DoTransDto {

    @Pattern(regexp = "^[0-9]{10}$", message = "Account number must be 10 digits")
    @NotBlank(message = "Source account must not be empty")
    private String sourceAccount;

    @NotBlank(message = "Destination account must not be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Destination account must be exactly 10 digits")
    private String destinationAccount;

    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;
}
