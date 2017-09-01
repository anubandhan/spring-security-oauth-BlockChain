package org.springframework.security.oauth2.blockchain.util;

/**
 * The MIT License
 * Copyright (c) 2014-2017 Anubandhan Singh Sengar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.blockchain.util.merkletree.BlockChainAuthorisedObj;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * Only one Blockchain object is created per instance of the daemon. It keeps track of ALL possible chains, and internally handles chain reorganization.
 *
 *  The Blockchain has fork management integrated, and should be able to appropriately handle any unexpected circumstances.
 *
 * As the blockchain has the most up-to-date information about blockchain data, it makes perfect sense for the ledger, which is based purely on the blockchain,
 * to be managed by the Blockchain object. Initial plans were to have separate Blockchain objects for each fork in a chainbut the overhead of cloning Blockchain
 * objects seemed unwarranted. Significant optimization still needs to be done on the fork management--climbing all the way down the shorter chain and back up
 * the longer chain is NOT a permanant solution.
 *
 */


public class Blockchain
{

    private static final Log logger = LogFactory.getLog(Blockchain.class);


    // Ideally Blockchain will be a linked list if we are developing a chain based coin, ours is a virtual linked list.
    // But note as there is no transactional reference of other lists so its immaterial to have a linked list as its just a proof of agremment between cliena/ User and publisher.

    ArrayList<ArrayList<Block>> chains = new ArrayList<ArrayList<Block>>();

    private static Blockchain instance;

    private Blockchain(){
    }

    private boolean gotGenesisBlock = false;


    /**
     * We don't want anyone else to use this even by mistake.
     * @return
     */
    public static Blockchain getInstance(){

        if(instance==null){
            logger.info("Blockchain: New Blockchain object initialised");
            instance = new Blockchain();
        }
        logger.info("Blockchain: Blockchain object returned");
        return instance;
    }

    /**
     * Constructor for Blockchain object. A Blockchain object represents an entire chain of blocks. Only one is created
     * in the entire execution of the program. All blocks will be added individually and in-order.
     *
     * This blockchain object will handle all forks--it keeps a record of forks for ten blocks. After a fork falls more
     * than 10 blocks behind, it is deleted.
     *
     * Blocks are stored in a 2D ArrayList, where one dimension is the blockchain 'version' (forks), and the other dimension is the blocks.
     *@param dbFolder Folder for database to be contained inside
     */


    /**
     * Returns the length of the tallest tree
     *
     * @return int Length of longest tree in blockchain
     */
    public int getBlockchainLength()
    {

        int longestChain = 0;
        for (int i = 0; i < chains.size(); i++)
        {
            if (chains.get(i).size() > longestChain)
            {
                longestChain = chains.get(i).size();
            }
        }
        logger.info("Blockchain: Longest Chain Length is :"+ longestChain);
        return longestChain;
    }

    /**
     * Needs to be implemented. Difficulty needs to eb there for mining.
     *
     * @return long Curreny difficulty on longest chain
     */
    public long getDifficulty()
    {

        return 1000000;
    }


    /**
     * Retrieves the block at blockNum (starting from 0) from the longest chain.
     *
     * @param blockNum The block number to retrieve
     *
     * @return Block Block at blockNum in longest chain
     */
    public Block getBlock(int blockNum)
    {
        int longestChainLength = 0;
        int longestChain = 0;
        for (int i = 0; i < chains.size(); i++)
        {
            if (chains.get(i).size() > longestChainLength)
            {
                longestChain = i;
                longestChainLength = chains.get(i).size();
            }
        }
        if(chains.size()==0){
            chains.add(new ArrayList<Block>());
        }
        return chains.get(longestChain).get(blockNum);
    }

    /**
     * sends longest chain back.
     *
     *
     * @return Block Block at blockNum in longest chain
     */
    public ArrayList<Block> getLongestBlockChain()
    {
        int longestChainLength = 0;
        int longestChain = 0;
        for (int i = 0; i < chains.size(); i++)
        {
            if (chains.get(i).size() > longestChainLength)
            {
                longestChain = i;
                longestChainLength = chains.get(i).size();
            }
        }
        if(chains.size()==0){
            chains.add(new ArrayList<Block>());
        }
        return chains.get(longestChain);
    }

    /**
     * add Block to longest Chain. Ideally it will not return anything, but for logging purpose we will
     * return list (Final)
     *
     *
     * @return Block Block at blockNum in longest chain
     */
    public ArrayList<Block> addBlock(Block block)
    {
        logger.info("Blockchain:Entering addBlock method:");
        int longestChainLength = 0;
        int longestChain = 0;
        logger.info("Blockchain: Chain Size:"+chains.size());
        for (int i = 0; i < chains.size(); i++)
        {
            if (chains.get(i).size() > longestChainLength)
            {
                longestChain = i;
                longestChainLength = chains.get(i).size();
            }
        }
        // This is list of list, parent list was initialised but child lists needs to be initialised.
        if(chains.size()==0){
            chains.add(new ArrayList<Block>());
        }
        chains.get(longestChain).add(block);
        logger.info("Blockchain: Exiting addBlock method:");
        return chains.get(longestChain);
    }

    /**
     * Add Block functionality moved to Tree builder.
     *
     * @param block
     * @param fromBlockchainFile
     * @return
     */
    public boolean addBlock(Block block, boolean fromBlockchainFile)
    {
        System.out.println("Attempting to add block " + block.blockNum + " with hash " + block.blockHash);
        try
        {
            boolean isPOS = false;// TODO: Remove this line.
            // Check block Difficulty here.
            if (false) // 2.0.0a4 Hard-coded PoS difficulty.
            {
                return false;
            }
            // Validate block here
            if(false){
                return false; //Block is not a valid block. Don't add it!
            }


            //A bit of cleanup--remove chains that are more than 10 blocks shorter than the largest chain.
            int largestChainLength = 0;
            ArrayList<Block> largestChain = new ArrayList<Block>();
            String largestChainLastBlockHash = "";
            for (int i = 0; i < chains.size(); i++)
            {
                if (chains.get(i).size() > largestChainLength)
                {
                    largestChain = chains.get(i);
                    largestChainLength = chains.get(i).size();
                    largestChainLastBlockHash = chains.get(i).get(chains.get(i).size() - 1).blockHash;
                }
            }
            //Now we have the longest chain, we remove any chains that are less than largestChainLength - 10 in length.
            for (int i = 0; i < chains.size(); i++)
            {
                if (chains.get(i).size() < largestChainLength - 10)
                {
                    chains.remove(i);
                    i--; //Compensate for resized ArrayList!
                }
            }

            //Block looks fine on its own--we don't know how it's going to play with the chain. If the block's number is larger than the largest chain + 1, we'll put the block in a queue to attempt to add later.
            //Block numbering starts at 0.
            if (block.blockNum > largestChainLength)
            {
                //Add it to the queue.
                //blockQueue.add(block);
                /*
                 * In the future, the addBlock() method may be changed to return an int, with values representing things like block above existing heights, validation error, block not on any chains, etc.
                 * For now, the boolean indicates simply whether immediate addition of the block to some internal blockchain was successful.
                 */
                System.out.println("Block " + block.blockNum + " with starting hash " + block.blockHash.substring(0, 20) + " added to queue...");
                System.out.println("LargestChainLength: " + largestChainLength);
                System.out.println("block.blockNum: " + block.blockNum);
                return false; //Block wasn't added onto any chain (yet)
            }
            //If no chains exist and this is the first block, we'll create our first chain:
            if (!gotGenesisBlock)
            {
                gotGenesisBlock = true;
                chains.add(new ArrayList<Block>());
                chains.get(0).add(block);
                largestChain = chains.get(0);
                largestChainLastBlockHash = block.blockHash;

                if (!fromBlockchainFile)
                {
                    writeBlockToFile(block);
                }
                return true;
            }
            //Initially, check for duplicate blocks
            for (int i = 0; i < chains.size(); i++)
            {
                if (chains.get(i).get(chains.get(i).size() - 1).blockHash.equals(block.blockHash))
                {
                    //Duplicate block; block has already been added. This happens all the time, as multiple peers all broadcast the same block.
                    System.out.println("Duplicate block received from peer");
                    return false;
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (!fromBlockchainFile)
        {
            writeBlockToFile(block);
        }
        return true;
    }

    /**
     * Writes a block to the blockchain file, Not part of POC
     *
     * @return boolean Whether write was successful
     */
    public boolean writeBlockToFile(Block block)
    {
        System.out.println("Writing a block to file...");

        /*try (FileWriter fileWriter = new FileWriter(dbFolder + "/blockchain.dta", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter out = new PrintWriter(bufferedWriter))
        {
            out.println(block.getRawBlock());
        } catch (Exception e)
        {
            System.out.println("ERROR: UNABLE TO SAVE BLOCK TO DATABASE!");
            e.printStackTrace();
            return false;
        }*/
        return true;
    }

    /**
     * Saves entire blockchain to a file, useful to save the state of the blockchain so it doesn't have to be redownloaded later.
     * Blockchain is stored to a file called "blockchain.dta" inside the provided dbFolder, Not part of POC
     *
     * @param dbFolder Folder to save blockchain file in
     *
     * @return boolean Whether saving to file was successful.
     */
    public boolean saveToFile(String dbFolder)
    {
        // TODO:  saving to file is not supported now.
        return true;
    }

    /**
     * Calls getAllTokenAgainstUser() on all Block objects in the current Blockchain to get all relevant authorisedTokenList.
     *
     * @param userId Address to search through all block transaction pools for
     *
     * @return ArrayList<String> All authorisedTokenList in simplified form blocknum:sender:amount:receiver of
     */
    public ArrayList<BlockChainAuthorisedObj> getAllTokenAgainstUser(String userId)
    {
        if(StringUtils.isEmpty(userId)){
            return new ArrayList<BlockChainAuthorisedObj>();
        }
        int longestChainLength = 0;
        int longestChainNum = -1;
        for (int i = 0; i < chains.size(); i++)
        {
            if (chains.get(i).size() > longestChainLength)
            {
                longestChainNum = i;
                longestChainLength = chains.get(i).size();
            }
        }
        ArrayList<Block> longestChain = chains.get(longestChainNum);

        ArrayList<BlockChainAuthorisedObj> allTransactions = new ArrayList<BlockChainAuthorisedObj>();

        for (int i = 0; i < longestChain.size(); i++)
        {
            ArrayList<BlockChainAuthorisedObj> tokensFromBlock = longestChain.get(i).authorisedTokenList;
            for (int j = 0; j < tokensFromBlock.size(); j++)
            {
                if(userId.equals(tokensFromBlock.get(j).getUserID())){
                    allTransactions.add(tokensFromBlock.get(j)) ;
                }

            }
        }
        return allTransactions;
    }


}