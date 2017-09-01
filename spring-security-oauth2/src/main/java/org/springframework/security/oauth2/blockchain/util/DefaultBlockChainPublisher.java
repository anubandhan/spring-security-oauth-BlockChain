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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.blockchain.util.merkletree.BlockChainAuthorisedObj;

import java.util.ArrayList;

/**
 * Not in use, Tree builder we are using now.
 */
public class DefaultBlockChainPublisher implements BlockChainPublisherInterface {

    private static final Log logger = LogFactory.getLog(DefaultBlockChainPublisher.class);

    // ideally it needs to be handled by an interface and Factory Pattern.
    // So that Implementation needs to be changed.

    @Autowired
    PendingAuthorisationContainer pendingAuthorisationContainer;

    private Block block;

    /**
     * This is a block creation process and when created
     * This will be published to all peers
     * and block will be added to blockchain.
     *
     * @return
     */
    @Override
    public boolean addPendingAuthorisationtoBlock() {
        logger.info("DefaultBlockChainPublisher:: addPendingAuthorisationtoBlock method called with Client ID:");
        block = new Block();

        try{

            ArrayList<BlockChainAuthorisedObj>  pendingAuthorisationArrayList;
            synchronized (pendingAuthorisationContainer){
                pendingAuthorisationArrayList = pendingAuthorisationContainer.getPendingAccessTokens();
                logger.info("pendingAuthorisationArrayList Size: "+ pendingAuthorisationArrayList.size());
                pendingAuthorisationContainer.reset();
                logger.info("pendingAuthorisationArrayList Size post reset: "+ pendingAuthorisationArrayList.size());
            }

            // We need to order Merkle tree 1st before acually
            // Actual Merkel Tree implementation in Java. We probably will go with a less dramatic version for POC.


        }catch (Exception ex){
            logger.error(ex.getStackTrace());
        }

        return false;
    }


    /**
     * This methd will be implimented when we have created a block and it needs to be published
     * to partners.
     * @return
     */
    @Override
    public Block publishBlock() {
        return null;
    }

    /**
     * This method is called when a peer receives a Authentication request and wants it to be published to blockchain.
     * So it will broadcast that message.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean addNewAuthorisationFromPeers(BlockChainAuthorisedObj obj) {

        logger.info("DefaultBlockChainPublisher:: addNewAuthorisationFromPeers method called.");
        logger.info("Received addNewAuthorisationFromPeers from peer: "+ obj);
        return pendingAuthorisationContainer.addAccessToken(obj.getClientID(),
                obj.getUserID(), obj.getToken());
    }

    /**
     * This method is called once any peer publishes a valid block.
     * If its a valid block then add that to your
     * Key here is that if you block cretion is still in progress then let that go if below block is valid and
     * add that block instead of yours.
     *
     * @param block
     * @return
     */
    @Override
    public boolean isValidPublishedBlocksFromPeers(Block block) {
        // TODO: This implementation needs to be done, skipped for purpose of POC.
        // TODO: If time permits, I will try to wrap this up as well.
        return true;
    }
}
