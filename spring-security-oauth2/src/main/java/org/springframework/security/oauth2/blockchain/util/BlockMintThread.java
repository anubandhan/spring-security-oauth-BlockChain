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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.blockchain.util.merkletree.BlockChainAuthorisedObj;
import org.springframework.security.oauth2.blockchain.util.merkletree.TokenTreeBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This is a block creation process and when created
 * This will be published to all peers
 * and block will be added to blockchain in a minute. Ideally bicoin uses 10 mins.
 *
 * Because its 1 minute Difficulty level will be lower compared to blockchain. and more transactions can be accomodated.
 *
 */
@Component("blockMintThread")
public class BlockMintThread {

    private static final Log logger = LogFactory.getLog(BlockMintThread.class);
    private boolean initialized = false;

    public void initializeJob() {

        if(!initialized){
            logger.info("BlockMintThread: Initialising Block Mint thread.");
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    execute();
                }
            }, 0, 1, TimeUnit.MINUTES);
            initialized=true;
        }else{
            logger.info("BlockMintThread: Block Mint thread already initialised.");
        }
    }

    @Scheduled(fixedRate = 1000)
    public void execute() {
        try {
            logger.info("BlockMintThread: Executing Block Mint thread. Current Time: "+ new Date());

            /**
             * Trying to create a new Merkel tree.
             */
            PendingAuthorisationContainer pendingAuthorisationContainer = PendingAuthorisationContainer.getInstance();
            if (pendingAuthorisationContainer.getPendingAccessTokens().size()>1 ){
                logger.info("BlockMintThread: Attempting creating a new block");
                ArrayList<BlockChainAuthorisedObj>  pendingAuthorisationArrayList = new ArrayList<BlockChainAuthorisedObj>();
                try{

                    synchronized (pendingAuthorisationContainer){
                        pendingAuthorisationArrayList = pendingAuthorisationContainer.getPendingAccessTokens();
                        logger.info("pendingAuthorisationArrayList Size: "+ pendingAuthorisationArrayList.size());
                        pendingAuthorisationContainer.reset();
                        logger.info("pendingAuthorisationContainer Size post reset: "+ pendingAuthorisationContainer.getPendingAccessTokens().size());
                        logger.info("pendingAuthorisationArrayList Size post reset: "+ pendingAuthorisationArrayList.size());
                    }

                    // We need to order Merkle tree 1st before acually
                    // Actual Merkel Tree implementation in Java. We probably will go with a less dramatic version for POC.

                }catch (Exception ex){
                    logger.error(ex.getStackTrace());
                    pendingAuthorisationContainer.addAccessTokens(pendingAuthorisationArrayList);
                    logger.info("BlockMintThread: Unable to copy data to new .");
                    return;
                }

                logger.info("BlockMintThread: Attempting new block");
                TokenTreeBuilder tokenTreeBuilder = new TokenTreeBuilder();
                Block block =  tokenTreeBuilder.mintAuthorisedTokenBlock(pendingAuthorisationArrayList);
                logger.info("BlockMintThread: New block created");

                logger.info("BlockMintThread: New block: "+ block.toString());

                logger.info("BlockMintThread: attempting to add block to blockchain");
                Blockchain blockchain = Blockchain.getInstance();
                int longestBlockChainSize = blockchain.getBlockchainLength();
                ArrayList<Block> blocks = blockchain.addBlock(block);

                // Trying to print Blockchain.
                logger.info("BlockMintThread: -----------------------------------------------------------------------------");
                logger.info("BlockMintThread: Blockchain after addition:" + blocks);
                logger.info("BlockMintThread: -----------------------------------------------------------------------------");
                if (longestBlockChainSize+1 == blocks.size()){
                    logger.info("BlockMintThread: Block chain successfully added.");

                }else{
                    logger.info("BlockMintThread: Issue while adding block. we need to add tokens back to pending ");
                    pendingAuthorisationContainer.addAccessTokens(pendingAuthorisationArrayList);
                }

                logger.info("BlockMintThread: Block chain successfully added, now pulishing to other nodes.");

                // Publishing code call to Peer Thread goes here.
                // Ideally publishing call should go before Block chain minting. because even milli second counts.

                // Now this is super critical but not part of this blockchain POC.
                // What if block was created successfully but was not part of longest chain after 6 minutes
                // (Ideally after 6 cycles if block couldn't made upto lohgest chain then its considered to be invalid)
                // My assumption is we should invalidate such tokens. OR we can retry, 1st one is ideal choice.

                // Invalidation code goes here.


                // This is critical again:
                // Only once we are sure that
                logger.info("BlockMintThread: Resetting.");
                pendingAuthorisationContainer.reset();

                logger.info("BlockMintThread: Blockchain thread exiting.");
            }else{

                logger.info("BlockMintThread: PendingAccessTokens size less than 2, blockchain can not be minted.");
            }



        } catch (Exception e){
            logger.error("Exception occurred while processing BlockMintThread:- ");
            logger.error(e);

        }
    }

}
