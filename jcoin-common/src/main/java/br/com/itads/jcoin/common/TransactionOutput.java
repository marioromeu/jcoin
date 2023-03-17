package br.com.itads.jcoin.common;

import java.security.PublicKey;

/**
 * 
 * @author mario.romeu@gmail.com * @email mario.romeu@gmail.com
 * @email mario.romeu@gmail.com
 *
 */
public class TransactionOutput {
	
	/**
	 * 
	 */
	public String id;
	
	/**
	 * also known as the new owner of these coins.
	 */
	public PublicKey reciepient;
	
	/**
	 * the amount of coins they own
	 */
	public float value;
	
	/**
	 * the id of the transaction this output was created in
	 */
	public String parentTransactionId;
	
	/**
	 * Constructor
	 * @param reciepient
	 * @param value
	 * @param parentTransactionId
	 */
	public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
		
		this.reciepient = reciepient;
		
		this.value = value;
		
		this.parentTransactionId = parentTransactionId;
		
		this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient)+Float.toString(value)+parentTransactionId);
		
	}
	
	/**
	 * Check if coin belongs to you
	 * @param publicKey
	 * @return
	 */
	public boolean isMine(PublicKey publicKey) {
		
		return (publicKey == reciepient);
		
	}
	
}
