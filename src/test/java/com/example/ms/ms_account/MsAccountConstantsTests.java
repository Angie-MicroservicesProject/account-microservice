package com.example.ms.ms_account;


import com.example.ms.ms_account.constants.AccountConstants;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;


class MsAccountConstantsTests {


	@Test
	void testPrivateConstructorThrowsException() {
		Constructor<?>[] constructors = AccountConstants.class.getDeclaredConstructors();

		for (Constructor<?> constructor : constructors) {
			constructor.setAccessible(true); // Permite el acceso al constructor privado

			assertThrows(InvocationTargetException.class, () -> {
				constructor.newInstance();
			}, "Instantiating AccountConstants should throw an exception");
		}
	}


	@Test
	void testConstantValues() {
		assertEquals("201", AccountConstants.STATUS_201);
		assertEquals("Account created successfully", AccountConstants.MESSAGE_201);

		assertEquals("200", AccountConstants.STATUS_200);
		assertEquals("Request processed successfully", AccountConstants.MESSAGE_200);

		assertEquals("400", AccountConstants.STATUS_400);
		assertEquals("Account already exists with given ID", AccountConstants.MESSAGE_400);

		assertEquals("417", AccountConstants.STATUS_417);
		assertEquals("Update operation failed. Please try again or contact Dev team", AccountConstants.MESSAGE_417_UPDATE);
		assertEquals("Delete operation failed. Please try again or contact Dev team", AccountConstants.MESSAGE_417_DELETE);

		assertEquals("500", AccountConstants.STATUS_500);
		assertEquals("An error occurred. Please try again or contact Dev team", AccountConstants.MESSAGE_500);
	}



}
