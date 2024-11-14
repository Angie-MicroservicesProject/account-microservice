package com.example.ms.ms_account.mapper;


import com.example.ms.ms_account.dto.AccountDto;
import com.example.ms.ms_account.model.entity.Account;

public class AccountMapper {

    public static AccountDto mapToAccountDto(Account account, AccountDto accountDto) {
       accountDto.setAccountNumber(account.getAccountNumber());
       accountDto.setBalance(account.getBalance());
       accountDto.setAccountType(account.getAccountType());
       accountDto.setCustomerId(account.getCustomerId());
        return accountDto;
    }

    public static Account mapToAccount(AccountDto accountDto, Account account) {
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setBalance(accountDto.getBalance());
        account.setAccountType(accountDto.getAccountType());
        account.setCustomerId(accountDto.getCustomerId());
        return account;
    }

}