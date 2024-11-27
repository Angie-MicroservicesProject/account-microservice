package com.example.ms.ms_account;

import com.example.ms.ms_account.constants.AccountConstants;
import com.example.ms.ms_account.controller.AccountController;
import com.example.ms.ms_account.dto.AccountDto;
import com.example.ms.ms_account.model.entity.Account;
import com.example.ms.ms_account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MsAccountControllerTests {

	@InjectMocks
	private AccountController accountController;

	@Mock
	private AccountService accountService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
	}

	@Test
	void testCreateAccount() throws Exception {

		AccountDto accountDto = new AccountDto("123456789", 1000.0, "Savings", "CUST001");
		doNothing().when(accountService).createAccount(accountDto);

		mockMvc.perform(post("/api/accounts")
						.contentType("application/json")
						.content("{\"accountNumber\":\"123456789\",\"balance\":1000.0,\"accountType\":\"Savings\",\"customerId\":\"CUST001\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.statusCode").value(AccountConstants.STATUS_201))
				.andExpect(jsonPath("$.statusMsg").value(AccountConstants.MESSAGE_201));

		verify(accountService, times(1)).createAccount(accountDto);
	}



	@Test
	void testGetAllAccounts() throws Exception {

		Account account = new Account(1L, "123456789", 1000.0, "Savings", "CUST001");
		when(accountService.getAllAccounts()).thenReturn(List.of(account));

		mockMvc.perform(get("/api/accounts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].accountNumber").value("123456789"))
				.andExpect(jsonPath("$[0].balance").value(1000.0))
				.andExpect(jsonPath("$[0].accountType").value("Savings"));

		verify(accountService, times(1)).getAllAccounts();
	}

	@Test
	void testGetAccountById() throws Exception {
		// Arrange
		String id = "CUST001";
		AccountDto accountDto = new AccountDto("123456789", 1000.0, "Savings", id);
		when(accountService.getAccount(id)).thenReturn(accountDto);

		// Act & Assert
		mockMvc.perform(get("/api/accounts/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accountNumber").value("123456789"))
				.andExpect(jsonPath("$.balance").value(1000.0))
				.andExpect(jsonPath("$.accountType").value("Savings"));

		verify(accountService, times(1)).getAccount(id);
	}


	@Test
	void testUpdateAccount() throws Exception {
		// Arrange
		String id = "CUST001";
		AccountDto accountDto = new AccountDto("123456789", 2000.0, "Checking", id);
		when(accountService.updateAccount(accountDto)).thenReturn(true);

		// Act & Assert
		MvcResult result = mockMvc.perform(put("/api/accounts/{id}", id)
						.contentType("application/json")
						.content("{\"accountNumber\":\"123456789\",\"balance\":2000.0,\"accountType\":\"Checking\",\"customerId\":\"CUST001\"}"))
				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.statusCode").value("200"))
//				.andExpect(jsonPath("$.statusMsg").value("Account updated successfully"))
				.andReturn();

		// Print response for debugging
		System.out.println(result.getResponse().getContentAsString());

		verify(accountService, times(1)).updateAccount(accountDto);
	}


	@Test
	void testDeleteAccount() throws Exception {
		// Arrange
		String id = "CUST001";
		when(accountService.deleteAccount(id)).thenReturn(true);

		// Act & Assert
		mockMvc.perform(delete("/api/accounts")
						.param("id", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.statusCode").value(AccountConstants.STATUS_200))
				.andExpect(jsonPath("$.statusMsg").value(AccountConstants.MESSAGE_200));

		verify(accountService, times(1)).deleteAccount(id);
	}



}
