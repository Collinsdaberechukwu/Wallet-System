package com.collins.Wallet.System.dtos.ResquestDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FundAccountRequestDto {

    @Pattern(regexp = "^[0-9]{10}$", message = "Account number must be 10 digits")
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;
}
