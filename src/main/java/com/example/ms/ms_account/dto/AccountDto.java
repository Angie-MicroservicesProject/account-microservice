package com.example.ms.ms_account.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountDto {

    @NotEmpty(message = "AccountNumber can not be a null or empty")
    @Size(min = 1)
    private String  accountNumber;

    @NotEmpty(message = "Balance can not be a null or empty")
    @Size(min = 1)
    private double balance;

    @NotEmpty(message = "AccountType can not be a null or empty")
    @Size(min = 1)
    private String accountType ;

    @NotEmpty(message = "Client ID can not be a null or empty")
    @Size(min = 1)
    private String customerId;
}
