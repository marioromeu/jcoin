package br.com.itads.jcoin.node;

import java.util.ArrayList;
import java.util.Date;

import br.com.itads.jcoin.common.Transaction;
import br.com.itads.jcoin.util.StringUtil;

/**
 * 
 * @author marioromeu
 * @email mario.romeu@gmail.com
 *
 */
public class BlockService {

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
  public BlockService(String previousHash) {

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
    String calculatedhash = StringUtil.applySha256(
        previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
    return calculatedhash;
  }

  /**
   * 
   * @param difficulty
   */
  public void mineBlock2(int difficulty) {

    /**
     * Create a string with difficulty * "0"
     */
    String target = new String(new char[difficulty]).replace('\0', '0');

    while (!hash.substring(0, difficulty).equals(target)) {
      System.out.println("==>  " + difficulty + " - " + nonce + " - " + hash);
      nonce++;
      hash = calculateHash();
    }

    System.out.println("Block Mined!!! : " + hash);

  }

  /**
   * Increases nonce value until hash target is reached.
   * 
   * @param difficulty
   */
  public void mineBlock(int difficulty) {

    merkleRoot = StringUtil.getMerkleRoot(transactions);

    // Create a string with difficulty * "0"
    String target = StringUtil.getDificultyString(difficulty);

    while (!hash.substring(0, difficulty).equals(target)) {
      nonce++;
      hash = calculateHash();
    }

    System.out.println("Block Mined!!! : " + hash);

  }

  /**
   * Add transactions to this block
   * 
   * @param transaction
   * @return
   */
  public boolean addTransaction(Transaction transaction) {
    // process transaction and check if valid, unless block is genesis block then
    // ignore.
    if (transaction == null)
      return false;
    if ((previousHash != "0")) {
      if ((transaction.processTransaction() != true)) {
        System.out.println("Transaction failed to process. Discarded.");
        return false;
      }
    }
    transactions.add(transaction);
    System.out.println("Transaction Successfully added to Block");
    return true;
  }

}
