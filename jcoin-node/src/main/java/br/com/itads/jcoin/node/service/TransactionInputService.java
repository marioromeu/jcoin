package br.com.itads.jcoin.node.service;

import org.springframework.stereotype.Service;

/**
 * 
 * @author mario.romeu@gmail.com * @email mario.romeu@gmail.com
 * @email mario.romeu@gmail.com
 *
 */
@Service
public class TransactionInputService {
	
	/**
	 * Reference to TransactionOutputs -> transactionId
	 */
	public String transactionOutputId; 
	
	/**
	 * Contains the Unspent transaction output
	 */
	public TransactionOutputService UTXO; 
	
	/**
	 * 
	 * @param transactionOutputId
	 */
	public TransactionInputService(String transactionOutputId) {

		this.transactionOutputId = transactionOutputId;

	}

}
