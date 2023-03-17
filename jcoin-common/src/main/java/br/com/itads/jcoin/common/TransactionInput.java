package br.com.itads.jcoin.common;

/**
 * 
 * @author mario.romeu@gmail.com * @email mario.romeu@gmail.com
 * @email mario.romeu@gmail.com
 *
 */
public class TransactionInput {
	
	/**
	 * Reference to TransactionOutputs -> transactionId
	 */
	public String transactionOutputId; 
	
	/**
	 * Contains the Unspent transaction output
	 */
	public TransactionOutput UTXO; 
	
	/**
	 * 
	 * @param transactionOutputId
	 */
	public TransactionInput(String transactionOutputId) {

		this.transactionOutputId = transactionOutputId;

	}

}
