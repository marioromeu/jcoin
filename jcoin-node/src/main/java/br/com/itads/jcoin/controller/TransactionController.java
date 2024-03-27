package br.com.itads.jcoin.controller;

import java.security.PrivateKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.itads.jcoin.node.service.TransactionService;

/**
 * 
 * @author marioromeu
 * @email mario.romeu@gmail.com
 *
 */
@RestController
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	/**
	 * Signs all the data we dont wish to be tampered with.
	 * 
	 * @param privateKey
	 * 
	 * @TODO isso é um prolema, pois a chave privada nao pode trafegar
	 */
	@PostMapping
	public void generateSignature(PrivateKey privateKey) {
		transactionService.generateSignature(privateKey);
	}

	/**
	 * Verifies the data we signed hasnt been tampered with
	 * 
	 * @return
	 */
	@GetMapping
	public boolean verifiySignature() {
		return transactionService.verifiySignature();
	}

	/**
	 * Returns true if new transaction could be created.
	 * 
	 * @return
	 */
	@PostMapping
	public boolean processTransaction() {
		return transactionService.processTransaction();
	}

	/**
	 * returns sum of inputs(UTXOs) values
	 * 
	 * @return
	 */
	@GetMapping
	public float getInputsValue() {
		return transactionService.getInputsValue();
	}

	/**
	 * returns sum of outputs:
	 * 
	 * @return
	 */
	@GetMapping
	public float getOutputsValue() {
		return transactionService.getOutputsValue();
	}

}