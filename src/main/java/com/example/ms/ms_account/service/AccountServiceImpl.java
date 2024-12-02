package com.example.ms.ms_account.service;


import com.example.ms.ms_account.dto.AccountDto;
import com.example.ms.ms_account.exception.AccountAlreadyExistsException;
import com.example.ms.ms_account.exception.ResourceNotFoundException;
import com.example.ms.ms_account.model.entity.Account;
import com.example.ms.ms_account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import com.example.ms.ms_account.mapper.AccountMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService    {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public void createAccount(AccountDto accountDto) {

        Account account= AccountMapper.mapToAccount(accountDto, new Account());
        if (account == null) {
            throw new IllegalArgumentException("Customer mapping failed.");
        }
        Optional<Account> optionalAccount=accountRepository.findByCustomerId(accountDto.getAccountNumber());
        if(optionalAccount.isPresent()){
            throw new AccountAlreadyExistsException("Account already registered with given ID "+accountDto.getAccountNumber());
        }
        account.setCreatedAt(LocalDateTime.now());
        account.setCreatedBy("Anonymus");
        accountRepository.save(account);

    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();

    }


    @Override
    public AccountDto getAccount(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        Account account = accountRepository.findById(Long.parseLong(id)).orElseThrow(
                () -> new ResourceNotFoundException("Account", "id ", id)
        );
        AccountDto accountDto= AccountMapper.mapToAccountDto(account, new AccountDto());
        return accountDto;
    }


    @Override
    public boolean updateAccount(AccountDto accountDto) {
        Optional<Account> optionalAccount = accountRepository.findByCustomerId(accountDto.getCustomerId());

        if (!optionalAccount.isPresent()) {
            throw new AccountAlreadyExistsException("Customer not found with DNI: " + accountDto.getCustomerId());
        }
        Account account = optionalAccount.get();

        account.setAccountNumber(accountDto.getAccountNumber());
        account.setBalance(accountDto.getBalance());
        account.setAccountType(accountDto.getAccountType());
        account.setCustomerId(accountDto.getCustomerId());

        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy("Anonymous");

        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean deleteAccount(String id) {
        Account account = accountRepository.findByCustomerId(id).orElseThrow(
                () -> new ResourceNotFoundException("Account", "id", id)
        );
        accountRepository.deleteById(account.getAccountId());

        return true;
    }

    @Override
    public boolean depositAccount(String id, double monto) {

        Account account = accountRepository.findById(Long.parseLong(id)).orElseThrow(
            () -> new ResourceNotFoundException("Account", "id ", id)
    );

        double saldoActual = account.getBalance();
        account.setBalance(saldoActual + monto);
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy("Anonymous");
        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean withdrawalAccount(String id, double monto) {
        Account account = accountRepository.findById(Long.parseLong(id)).orElseThrow(
                () -> new ResourceNotFoundException("Account", "id ", id)
        );

        double saldoActual = account.getBalance();
        account.setBalance(saldoActual - monto);
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy("Anonymous");
        accountRepository.save(account);

        return true;
    }


}
