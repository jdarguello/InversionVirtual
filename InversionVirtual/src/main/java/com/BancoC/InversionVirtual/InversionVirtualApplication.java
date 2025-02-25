package com.BancoC.InversionVirtual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InversionVirtualApplication {

	public static void main(String[] args) {
		SpringApplication.run(InversionVirtualApplication.class, args);
	}

}
