package com.BancoC.InversionVirtual;

import org.springframework.boot.SpringApplication;

public class TestInversionVirtualApplication {

	public static void main(String[] args) {
		SpringApplication.from(InversionVirtualApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
