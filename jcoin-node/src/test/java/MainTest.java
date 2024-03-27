
import java.security.Security;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import br.com.itads.jcoin.common.Block;
import br.com.itads.jcoin.common.BlockChain;
import br.com.itads.jcoin.common.Transaction;
import br.com.itads.jcoin.common.TransactionOutput;
import br.com.itads.jcoin.common.Wallet;

public class MainTest {

	/**
	 * 
	 */
	public BlockChain blockchain;

	/**
	 * list of all unspent transactions.
	 */
	public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

	/**
	 * 
	 */
	public int difficulty = 5;

	/**
	 * 
	 */
	public float minimumTransaction = 0.1f;

	/**
	 * 
	 */
	public Wallet walletA;

	/**
	 * 
	 */
	public Wallet walletB;

	/**
	 * 
	 */
	public Transaction genesisTransaction;
	
	/**
	 * 
	 * @param args
	 */
	@Test
	void executeAnTransaction() {

		/**
		 *  add our blocks to the blockchain ArrayList:
		 *  
		 *  Setup Bouncey castle as a Security Provider
		 */
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 

		/**
		 * Create wallets:
		 */
		walletA = new Wallet();
		walletB = new Wallet();
		Wallet coinbase = new Wallet();

		/**
		 * Create genesis transaction, which sends 1000 NoobCoin to walletA:
		 */
		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 1000f, null);

		blockchain = new BlockChain( genesisTransaction );
		
		walletA.linkBlockChain(blockchain);
		walletB.linkBlockChain(blockchain);
		coinbase.linkBlockChain(blockchain);
		
		/**
		 * Manually sign the genesis transaction
		 */
		genesisTransaction.generateSignature(coinbase.privateKey);
		
		/**
		 * Manually set the transaction id
		 */
		genesisTransaction.transactionId = "0"; 
		
		/**
		 *  Manually add the Transactions Output
		 */
		genesisTransaction.outputs.add(
				new TransactionOutput(
						genesisTransaction.reciepient,
						genesisTransaction.value,
						genesisTransaction.transactionId
				)
		);
		
		/**
		 * its important to store our first transaction in the UTXOs list.
		 */
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); 

		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		blockchain.addBlock(genesis);

		// testing
		Block block1 = new Block(genesis.hash);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		blockchain.addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		Block block2 = new Block(block1.hash);
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		blockchain.addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		Block block3 = new Block(block2.hash);
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20));
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());

		blockchain.isChainValid();

	}
	
}
