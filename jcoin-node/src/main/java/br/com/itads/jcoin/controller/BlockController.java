package br.com.itads.jcoin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.itads.jcoin.common.Transaction;
import br.com.itads.jcoin.node.service.BlockService;

/**
 * 
 * @author marioromeu
 * @email mario.romeu@gmail.com
 *
 */
@RestController
public class BlockController {

	@Autowired	
	private BlockService blockService;

	/**
	 * Calculate new hash based on blocks contents
	 * 
	 * @return
	 */
	@GetMapping
	public String calculateHash() {
		return blockService.calculateHash();
	}

	/**
	 * 
	 * @param difficulty
	 */
	@GetMapping
	public void mineBlock2(int difficulty) {
		blockService.mineBlock2(difficulty);
	}
	
	@GetMapping
	public void mineBlock(int difficulty) {
		blockService.mineBlock(difficulty);
	}

	/**
	 * Add transactions to this block
	 * 
	 * @param transaction
	 * @return
	 */
	@PostMapping
	public boolean addTransaction(@RequestBody Transaction transaction) {
		return blockService.addTransaction(transaction);
	}

}
