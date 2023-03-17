package br.com.itads.jcoin.node;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.itads.jcoin.common.Transaction;
import br.com.itads.jcoin.common.TransactionInput;
import br.com.itads.jcoin.common.TransactionOutput;
import br.com.itads.jcoin.common.Wallet;

/**
 * 
 * @author marioromeu
 * @email mario.romeu@gmail.com
 *
 */
public class BlockChainService {

	/**
	 * 
	 */
	public static ArrayList<BlockService> blockchain = new ArrayList<BlockService>();

	/**
	 * list of all unspent transactions.
	 */
	public static HashMap<String, TransactionOutputService> UTXOs = new HashMap<String, TransactionOutputService>();

	/**
	 * 
	 */
	public static int difficulty = 5;

	/**
	 * 
	 */
	public static float minimumTransaction = 0.1f;

	/**
	 * 
	 */
	public static Wallet walletA;

	/**
	 * 
	 */
	public static Wallet walletB;

	/**
	 * 
	 */
	public static Transaction genesisTransaction;
	
	/**
	 * 
	 * @return
	 */
	public static Boolean isChainValid() {
		
		/**
		 * 
		 */
		BlockService currentBlock;
		
		/**
		 * 
		 */
		BlockService previousBlock;
		
		/**
		 * 
		 */
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		/**
		 * A temporary working list of unspent transactions at a given block state.
		 */
		HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>();

		/**
		 * 
		 */
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

		/**
		 * Loop through blockchain to check hashes:
		 */
		for (int i = 1; i < blockchain.size(); i++) {

			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);
			// compare registered hash and calculated hash:
			if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
				System.out.println("#Current Hashes not equal");
				return false;
			}
			// compare previous hash and registered previous hash
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {
				System.out.println("#Previous Hashes not equal");
				return false;
			}
			// check if hash is solved
			if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
				System.out.println("#This block hasn't been mined");
				return false;
			}

			// loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for (int t = 0; t < currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);

				if (!currentTransaction.verifiySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false;
				}
				if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false;
				}

				for (TransactionInput input : currentTransaction.inputs) {
					tempOutput = tempUTXOs.get(input.transactionOutputId);

					if (tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}

					if (input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}

					tempUTXOs.remove(input.transactionOutputId);
				}

				for (TransactionOutput output : currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}

				if (currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if (currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}

			}

		}
		System.out.println("Blockchain is valid");
		return true;
	}

	/**
	 * 
	 * @param newBlock
	 */
	public static void addBlock(BlockService newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}

}