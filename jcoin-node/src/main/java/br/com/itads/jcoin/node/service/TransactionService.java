package br.com.itads.jcoin.node.service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import br.com.itads.jcoin.util.StringUtil;

/**
 * 
 * @author marioromeu
 * @email mario.romeu@gmail.com
 *
 */
@Service
public class TransactionService {

	/**
	 * 
	 */
	public String transactionId; // this is also the hash of the transaction.

	/**
	 * 
	 */
	public PublicKey sender; // senders address/public key.

	/**
	 * 
	 */
	public PublicKey reciepient; // Recipients address/public key.

	/**
	 * 
	 */
	public float value;

	/**
	 * 
	 */
	public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

	/**
	 * 
	 */
	public ArrayList<TransactionInputService> inputs = new ArrayList<TransactionInputService>();

	/**
	 * 
	 */
	public ArrayList<TransactionOutputService> outputs = new ArrayList<TransactionOutputService>();

	/**
	 * 
	 */
	private static int sequence = 0; // a rough count of how many transactions have been generated. 



	/**
	 *  Constructor: 
	 * @param from
	 * @param to
	 * @param value
	 * @param inputs
	 */
	public TransactionService(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInputService> inputs) {
		this.sender = from;
		this.reciepient = to;
		this.value = value;
		this.inputs = inputs;
	}



	/**
	 * This Calculates the transaction hash (which will be used as its Id)
	 * @return
	 */
	private String calulateHash() {
		sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
		return StringUtil.applySha256(
				StringUtil.getStringFromKey(sender) +
				StringUtil.getStringFromKey(reciepient) +
				Float.toString(value) + sequence
				);
	}

	/**
	 * Signs all the data we dont wish to be tampered with.
	 * @param privateKey
	 */
	public void generateSignature(PrivateKey privateKey) {

		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;

		signature = StringUtil.applyECDSASig(privateKey,data);

	}

	/**
	 * Verifies the data we signed hasnt been tampered with
	 * @return
	 */
	public boolean verifiySignature() {

		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;

		return StringUtil.verifyECDSASig(sender, data, signature);

	}

	/**
	 * Returns true if new transaction could be created.	
	 * @return
	 */
	public boolean processTransaction() {

		if(verifiySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}

		/**
		 * gather transaction inputs (Make sure they are unspent):
		 */
		for(TransactionInputService i : inputs) {
			i.UTXO = BlockChainService.UTXOs.get(i.transactionOutputId);
		}

		/**
		 * check if transaction is valid:
		 */
		if(getInputsValue() < BlockChainService.minimumTransaction) {
			System.out.println("#Transaction Inputs to small: " + getInputsValue());
			return false;
		}

		/**
		 * generate transaction outputs:
		 */
		float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
		transactionId = calulateHash();
		outputs.add(new TransactionOutputService( this.reciepient, value,transactionId)); //send value to recipient
		outputs.add(new TransactionOutputService( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		

		/**
		 * add outputs to Unspent list
		 */
		for(TransactionOutputService o : outputs) {
			BlockChainService.UTXOs.put(o.id , o);
		}

		/**
		 * remove transaction inputs from UTXO lists as spent:
		 */
		for(TransactionInputService i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			BlockChainService.UTXOs.remove(i.UTXO.id);
		}

		return true;
	}

	/**
	 * returns sum of inputs(UTXOs) values
	 * @return
	 */
	public float getInputsValue() {
		float total = 0;
		for(TransactionInputService i : inputs) {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			total += i.UTXO.value;
		}
		return total;
	}

	/**
	 * returns sum of outputs:
	 * @return
	 */
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutputService o : outputs) {
			total += o.value;
		}
		return total;
	}


}
