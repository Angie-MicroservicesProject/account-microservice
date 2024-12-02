package com.example.ms.ms_account.controller;


import com.example.ms.ms_account.constants.AccountConstants;
import com.example.ms.ms_account.dto.AccountDto;
import com.example.ms.ms_account.dto.ResponseDto;
import com.example.ms.ms_account.model.entity.Account;
import com.example.ms.ms_account.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class AccountController {

    private AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody AccountDto accountDto) {
        accountService.createAccount(accountDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountConstants.STATUS_201,AccountConstants.MESSAGE_201));
    }

    @GetMapping("/accounts")
    public List<Account> getCustomers(@RequestHeader Map<String, String> headers) {
        return accountService.getAllAccounts();
    }


    @GetMapping("/accounts/{id}")
    public  ResponseEntity<AccountDto> getAccount(@RequestHeader Map<String, String> headers, @PathVariable String id) {
        AccountDto accountDto = accountService.getAccount(id);
        return ResponseEntity.status(HttpStatus.OK).body(accountDto);
    }




    @PutMapping("/accounts/{id}")
    public ResponseEntity<ResponseDto> updateAccount(@PathVariable String id, @RequestBody AccountDto accountDto) {
        boolean isUpdated = accountService.updateAccount(accountDto);

        if (isUpdated) {
            ResponseDto responseDto = new ResponseDto("200", "Client updated successfully");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(responseDto);
        } else {
            ResponseDto responseDto = new ResponseDto("417", "Failed to update client");
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(responseDto);
        }

    }

    @RequestMapping(value = "/accounts",method=RequestMethod.DELETE)
    public ResponseEntity<ResponseDto> deleteAccount(@RequestHeader Map<String, String> headers, @RequestParam String id) {
        boolean isDeleted = accountService.deleteAccount(id);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_UPDATE));
        }
    }

    @PutMapping("/accounts/{id}/depositar")
    public ResponseEntity<ResponseDto> depositAccount(@PathVariable String id, @RequestBody Double monto) {
        boolean isUpdated = accountService.depositAccount(id, monto);

        if (isUpdated) {
            ResponseDto responseDto = new ResponseDto("200", "Deposit successful");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(responseDto);
        } else {
            ResponseDto responseDto = new ResponseDto("417", "Failed deposit");
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(responseDto);
        }

    }

    @PutMapping("/accounts/{id}/retirar")
    public ResponseEntity<ResponseDto> withdrawalAccount(@PathVariable String id, @RequestBody Double monto) {
        boolean isUpdated = accountService.withdrawalAccount(id, monto);

        if (isUpdated) {
            ResponseDto responseDto = new ResponseDto("200", "Withdrawal successful");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(responseDto);
        } else {
            ResponseDto responseDto = new ResponseDto("417", "Failed deposit");
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(responseDto);
        }

    }






}
