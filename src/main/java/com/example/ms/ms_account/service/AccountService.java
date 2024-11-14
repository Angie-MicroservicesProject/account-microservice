package com.example.ms.ms_account.service;

import com.example.ms.ms_account.dto.AccountDto;
import com.example.ms.ms_account.model.entity.Account;

import java.util.List;

public interface AccountService {

    void createAccount(AccountDto accountDto);
    public List<Account> getAllAccounts();
    AccountDto getAccount(String id);
    boolean updateAccount(AccountDto accountDto);
    boolean deleteAccount(String id);



}
