package br.com.itads.jcoin.util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;

import br.com.itads.jcoin.common.Transaction;

/**
 * 
 * @author marioromeu
 * @email mario.romeu@gmail.com
 *
 */
public class StringUtil {

	/**
	 * Applies Sha256 to a string and returns the result. 
	 * @param input
	 * @return
	 */
	public static String applySha256(String input){

		try {

			/**
			 * Applies sha256 to our input,
			 */
			MessageDigest digest = MessageDigest.getInstance("SHA-256");	        

			byte[] hash = digest.digest(input.getBytes("UTF-8"));

			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();

		} catch(Exception e) {

			throw new RuntimeException(e);

		}

	}	

	/**
	 * Applies ECDSA Signature and returns the result ( as bytes ).
	 * @param privateKey
	 * @param input
	 * @return
	 */
	public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
		Signature dsa;
		byte[] output = new byte[0];
		try {
			dsa = Signature.getInstance("ECDSA", "BC");
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = realSig;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return output;
	}

	/**
	 * Verifies a String signature 
	 * @param publicKey
	 * @param data
	 * @param signature
	 * @return
	 */
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	
	/**
	 * Tacks in array of transactions and returns a merkle root.
	 * @param transactions
	 * @return
	 */
	public static String getMerkleRoot(ArrayList<Transaction> transactions) {
		
			int count = transactions.size();
			
			ArrayList<String> previousTreeLayer = new ArrayList<String>();
			
			for(Transaction transaction : transactions) {
				previousTreeLayer.add(transaction.transactionId);
			}
			
			ArrayList<String> treeLayer = previousTreeLayer;
			
			while(count > 1) {
				treeLayer = new ArrayList<String>();
				for(int i=1; i < previousTreeLayer.size(); i++) {
					treeLayer.add(applySha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
				}
				count = treeLayer.size();
				previousTreeLayer = treeLayer;
			}
			
			String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
			
			return merkleRoot;

		}

	/**
	 * Create a string with difficulty * "0"
	 * @return
	 */
	public static String getDificultyString(int difficulty) {
		 
		return new String(new char[difficulty]).replace('\0', '0');
	}
	
}
