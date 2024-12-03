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

		AccountDto accountDto = new AccountDto("123456789", 1000.0, "Savings", "1");
		when(accountRepository.findByCustomerId(accountDto.getAccountNumber())).thenReturn(Optional.empty());

		assertDoesNotThrow(() -> accountService.createAccount(accountDto));

		verify(accountRepository, times(1)).save(any(Account.class));
	}

	@Test
	void testCreateAccount_AlreadyExists() {
		AccountDto accountDto = new AccountDto("123456789", 1000.0, "Savings", "1");
		Account existingAccount = new Account();
		when(accountRepository.findByCustomerId(accountDto.getAccountNumber())).thenReturn(Optional.of(existingAccount));

		assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(accountDto));
		verify(accountRepository, never()).save(any(Account.class));
	}


	@Test
	void testGetAccount_Success() {
		// Configurar datos de prueba
		String accountId = "1";
		Account account = new Account(1L, "123456789", 1000.0, "Savings", "Customer123");

		// Mockear comportamiento del repositorio
		when(accountRepository.findById(Long.parseLong(accountId))).thenReturn(Optional.of(account));

		// Ejecutar el método
		AccountDto result = accountService.getAccount(accountId);

		// Verificar resultados
		assertNotNull(result, "El resultado no debe ser nulo");
		assertEquals("123456789", result.getAccountNumber(), "El número de cuenta debe coincidir");
		assertEquals(1000.0, result.getBalance(), 0.001, "El saldo debe coincidir");
		assertEquals("Savings", result.getAccountType(), "El tipo de cuenta debe coincidir");

		// Verificar que el repositorio fue llamado correctamente
		verify(accountRepository, times(1)).findById(Long.parseLong(accountId));
	}



	@Test
	void testGetAccount_NotFound() {
		// Arrange
		String customerId = "1";
		when(accountRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> accountService.getAccount(customerId));
	}


	@Test
	void testUpdateAccount_Success() {
		// Arrange
		AccountDto accountDto = new AccountDto("987654321", 2000.0, "Checking", "1");
		Account existingAccount = new Account(1L, "123456789", 1000.0, "Savings", "1");
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
		AccountDto accountDto = new AccountDto("987654321", 2000.0, "Checking", "1");
		when(accountRepository.findByCustomerId(accountDto.getCustomerId())).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(AccountAlreadyExistsException.class, () -> accountService.updateAccount(accountDto));
	}

	@Test
	void testDeleteAccount_Success() {
		// Arrange
		String customerId = "1";
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
		String customerId = "1";
		when(accountRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccount(customerId));
	}


	@Test
	void testDepositAccount_Success() {
		// Configurar datos de prueba
		String accountId = "1";
		double depositAmount = 500.0;
		Account account = new Account(1L, "123456789", 1000.0, "Savings", "Customer123");

		// Mockear comportamiento del repositorio
		when(accountRepository.findById(Long.parseLong(accountId))).thenReturn(Optional.of(account));
		when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Ejecutar el método
		boolean result = accountService.depositAccount(accountId, depositAmount);

		// Verificar resultados
		assertTrue(result, "El depósito debe ser exitoso");
		assertEquals(1500.0, account.getBalance(), 0.001, "El saldo debe actualizarse correctamente");

		// Verificar interacciones con el repositorio
		verify(accountRepository, times(1)).findById(Long.parseLong(accountId));
		verify(accountRepository, times(1)).save(account);
	}

	@Test
	void testDepositAccount_AccountNotFound() {
		String accountId = "1";
		double depositAmount = 500.0;

		when(accountRepository.findById(Long.parseLong(accountId))).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(
				ResourceNotFoundException.class,
				() -> accountService.depositAccount(accountId, depositAmount)
		);

		verify(accountRepository, times(1)).findById(Long.parseLong(accountId));
		verify(accountRepository, never()).save(any(Account.class));
	}

	@Test
	void testWithdrawalAccount_Success() {
		// Configurar datos de prueba
		String accountId = "1";
		double withdrawalAmount = 200.0;
		Account account = new Account(1L, "123456789", 1000.0, "Savings", "Customer123");

		// Mockear comportamiento del repositorio
		when(accountRepository.findById(Long.parseLong(accountId))).thenReturn(Optional.of(account));
		when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Ejecutar el método
		boolean result = accountService.withdrawalAccount(accountId, withdrawalAmount);

		// Verificar resultados
		assertTrue(result, "El retiro debe ser exitoso");
		assertEquals(800.0, account.getBalance(), 0.001, "El saldo debe actualizarse correctamente");

		// Verificar interacciones con el repositorio
		verify(accountRepository, times(1)).findById(Long.parseLong(accountId));
		verify(accountRepository, times(1)).save(account);
	}


	@Test
	void testWithdrawalAccount_InsufficientFunds_SavingsAccount() {
		// Configurar datos de prueba
		String accountId = "1";
		double withdrawalAmount = 1200.0; // Mayor al saldo actual
		Account account = new Account(1L, "123456789", 1000.0, "Savings", "Customer123");

		// Mockear el comportamiento del repositorio
		when(accountRepository.findById(Long.parseLong(accountId))).thenReturn(Optional.of(account));

		// Ejecutar el método y capturar la excepción
		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> accountService.withdrawalAccount(accountId, withdrawalAmount)
		);

		// Verificar el mensaje de error
		assertEquals("Withdrawal would leave the account with a negative balance", exception.getMessage());

		// Verificar interacciones con el repositorio
		verify(accountRepository, times(1)).findById(Long.parseLong(accountId));
		verify(accountRepository, never()).save(any(Account.class));
	}

	@Test
	void testWithdrawalAccount_OverdraftLimitExceeded_CurrentAccount() {
		// Configurar datos de prueba
		String accountId = "2";
		double withdrawalAmount = 1600.0;
		Account account = new Account(2L, "987654321", 500.0, "Current", "Customer456");

		// Mockear comportamiento del repositorio
		when(accountRepository.findById(Long.parseLong(accountId))).thenReturn(Optional.of(account));

		// Ejecutar el método y capturar excepción
		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> accountService.withdrawalAccount(accountId, withdrawalAmount)
		);

		// Verificar mensaje de error
		assertEquals("Overdraft limit exceeded", exception.getMessage());

		// Verificar interacciones con el repositorio
		verify(accountRepository, times(1)).findById(Long.parseLong(accountId));
		verify(accountRepository, never()).save(any(Account.class));
	}


}
