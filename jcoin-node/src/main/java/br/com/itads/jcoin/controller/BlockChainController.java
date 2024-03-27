package br.com.itads.jcoin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.itads.jcoin.common.Block;
import br.com.itads.jcoin.node.service.BlockChainService;

/**
 * 
 * @author marioromeu
 * @email mario.romeu@gmail.com
 *
 */
@Service
public class BlockChainController {

	@Autowired
	private BlockChainService blockChainService; 
	
	public Boolean isChainValid(Block currentBlock, Block previousBlock) {		
		return blockChainService.isChainValid(currentBlock, previousBlock);		
	}

	/**
	 * 
	 * @param newBlock
	 */
	public void addBlock(Block newBlock) {
		blockChainService.addBlock(newBlock);
	}

}