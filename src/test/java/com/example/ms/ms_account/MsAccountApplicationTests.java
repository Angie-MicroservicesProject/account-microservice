package com.example.ms.ms_account;

import com.example.ms.ms_account.dto.AccountDto;
import com.example.ms.ms_account.exception.AccountAlreadyExistsException;
import com.example.ms.ms_account.exception.ResourceNotFoundException;
import com.example.ms.ms_account.model.entity.Account;
import com.example.ms.ms_account.repository.AccountRepository;
import com.example.ms.ms_account.service.AccountService;
import com.example.ms.ms_account.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MsAccountApplicationTests {
	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountServiceImpl accountService;


	@BeforeEach
	void setUp(){
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateAccount_Success() {

		AccountDto accountDto = new AccountDto("123456789", 1000.0, "Savings", "CUST001");
		when(accountRepository.findByCustomerId(accountDto.getAccountNumber())).thenReturn(Optional.empty());

		assertDoesNotThrow(() -> accountService.createAccount(accountDto));

		verify(accountRepository, times(1)).save(any(Account.class));
	}

	@Test
	void testCreateAccount_AlreadyExists() {
		AccountDto accountDto = new AccountDto("123456789", 1000.0, "Savings", "CUST001");
		Account existingAccount = new Account();
		when(accountRepository.findByCustomerId(accountDto.getAccountNumber())).thenReturn(Optional.of(existingAccount));

		assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(accountDto));
		verify(accountRepository, never()).save(any(Account.class));
	}

	@Test
	void testGetAccount_Success() {

		String customerId = "CUST001";
		Account account = new Account(1L, "123456789", 1000.0, "Savings", customerId);
		when(accountRepository.findByCustomerId(customerId)).thenReturn(Optional.of(account));

		AccountDto result = accountService.getAccount(customerId);

		assertNotNull(result);
		assertEquals("123456789", result.getAccountNumber());
		verify(accountRepository, times(1)).findByCustomerId(customerId);
	}

	@Test
	void testGetAccount_NotFound() {
		// Arrange
		String customerId = "CUST001";
		when(accountRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> accountService.getAccount(customerId));
	}


	@Test
	void testUpdateAccount_Success() {
		// Arrange
		AccountDto accountDto = new AccountDto("987654321", 2000.0, "Checking", "CUST001");
		Account existingAccount = new Account(1L, "123456789", 1000.0, "Savings", "CUST001");
		when(accountRepository.findByCustomerId(accountDto.getCustomerId())).thenReturn(Optional.of(existingAccount));

		// Act
		boolean result = accountService.updateAccount(accountDto);

		// Assert
		assertTrue(result);
		verify(accountRepository, times(1)).save(existingAccount);
	}

	@Test
	void testUpdateAccount_NotFound() {
		// Arrange
		AccountDto accountDto = new AccountDto("987654321", 2000.0, "Checking", "CUST001");
		when(accountRepository.findByCustomerId(accountDto.getCustomerId())).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(AccountAlreadyExistsException.class, () -> accountService.updateAccount(accountDto));
	}

	@Test
	void testDeleteAccount_Success() {
		// Arrange
		String customerId = "CUST001";
		Account account = new Account(1L, "123456789", 1000.0, "Savings", customerId);
		when(accountRepository.findByCustomerId(customerId)).thenReturn(Optional.of(account));

		// Act
		boolean result = accountService.deleteAccount(customerId);

		// Assert
		assertTrue(result);
		verify(accountRepository, times(1)).deleteById(account.getAccountId());
	}

	@Test
	void testDeleteAccount_NotFound() {
		// Arrange
		String customerId = "CUST001";
		when(accountRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccount(customerId));
	}

}
