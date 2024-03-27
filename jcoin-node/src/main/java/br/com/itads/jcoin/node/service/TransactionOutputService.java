package br.com.itads.jcoin.node.service;

import java.security.PublicKey;

import org.springframework.stereotype.Service;

import br.com.itads.jcoin.util.StringUtil;

/**
 * 
 * @author mario.romeu@gmail.com * @email mario.romeu@gmail.com
 * @email mario.romeu@gmail.com
 *
 */
@Service
public class TransactionOutputService {
	
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
	public TransactionOutputService(PublicKey reciepient, float value, String parentTransactionId) {
		
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
