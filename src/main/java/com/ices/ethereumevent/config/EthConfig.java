package com.ices.ethereumevent.config;

import java.math.BigInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
public class EthConfig {

	@Bean
	public Web3j web3j() {
		log.info("connect web3j");
		return Web3j.build(new HttpService("http://localhost:7545"));
	}
	
	@Bean
	public Credentials credentials() {
		return Credentials.create("a59f5d45d1debed83966d8b3e54022ca24292b3699118d4d1ca340106c6c2817");
	}
	
	@Bean
	public TransactionManager TransactionManager(Web3j web3j) {
		return new ClientTransactionManager(web3j, "0x92588b0fE2049D12714D36b443BD6294af8Ee2e5");
	}
	
	@Bean
	public ContractGasProvider contractGasProvider() {
		return new StaticGasProvider(BigInteger.valueOf(20000000000L), BigInteger.valueOf(6721975L));
	}
	
}
