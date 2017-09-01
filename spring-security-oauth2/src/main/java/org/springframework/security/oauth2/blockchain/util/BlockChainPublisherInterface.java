package org.springframework.security.oauth2.blockchain.util;

import org.springframework.security.oauth2.blockchain.util.merkletree.BlockChainAuthorisedObj;

/**
 * Created by anubandhans on 27/08/17.
 */
public interface BlockChainPublisherInterface {

    // Critical task.
    // Need to ensure we are not writing to PendingAuthorisation List when we are creating a block.
    public  boolean addPendingAuthorisationtoBlock();

    // Publish bock to publisher. This will be caseed form Endpoint.
    public Block publishBlock();

    // All unpublished Authorisation requests will be added to pending Authorisation list
    public boolean addNewAuthorisationFromPeers(BlockChainAuthorisedObj obj);

    // this method will return false if PublishedBlocksFromPeers is not valid.
    // It will not be added to blockchain then.
    public boolean isValidPublishedBlocksFromPeers(Block block);

}

