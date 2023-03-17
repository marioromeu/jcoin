package br.com.itads.jcoin.common;

import java.util.ArrayList;
import java.util.Date;

import br.com.itads.jcoin.util.StringUtil;

/**
 * 
 * @author marioromeu
 * @email mario.romeu@gmail.com
 *
 */
public class Block {

	/**
	 * 
	 */
	public String hash;

	/**
	 * 
	 */
	public String previousHash;

	/**
	 * 
	 */
	public String merkleRoot;

	/**
	 * 
	 */
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

	/**
	 * as number of milliseconds since 1/1/1970.
	 */
	private long timeStamp;

	/**
	 * 
	 */
	private int nonce;

	/**
	 * Block Constructor.
	 * 
	 * @param previousHash
	 */
	public Block(String previousHash) {

		this.previousHash = previousHash;

		this.timeStamp = new Date().getTime();

		this.hash = calculateHash(); // Making sure we do this after we set the other values.
	}

	/**
	 * Calculate new hash based on blocks contents
	 * 
	 * @return
	 */
	public String calculateHash() {
		String calculatedhash = StringUtil
				.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
		return calculatedhash;
	}

}
