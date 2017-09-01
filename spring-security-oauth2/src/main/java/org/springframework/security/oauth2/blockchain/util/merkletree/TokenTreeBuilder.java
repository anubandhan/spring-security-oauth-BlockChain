package org.springframework.security.oauth2.blockchain.util.merkletree;


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


import org.apache.commons.codec.Charsets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.blockchain.util.Block;
import org.springframework.security.oauth2.blockchain.util.Blockchain;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.zip.Adler32;


/**
 * This class will be used to create Block.
 */
public class TokenTreeBuilder {

    private static final Log logger = LogFactory.getLog(TokenTreeBuilder.class);

    public static final byte LEAF_SIG_TYPE = 0x0;
    public static final byte INTERNAL_SIG_TYPE = 0x01;

    public static MessageDigest md;
    private final Adler32 crc = new Adler32();

    private int depth;
    private int nnodes;
    //private TokenTree root;

    //TODO: Method that is calling Merkle Tree needs to check for Null Response and should not consume Objects from pendingTokenList.
    public Block mintAuthorisedTokenBlock(ArrayList<BlockChainAuthorisedObj> objList){

        logger.info("TokenTreeBuilder: Inside mintAuthorisedTokenBlock");
         // Define the message digest algorithm to use
         try
         {
             md = MessageDigest.getInstance("SHA-256");

         }catch (NoSuchAlgorithmException e)
         {
             logger.info("Error while creating Message Digest object :"+e.getMessage());
         }

        logger.info("TokenTreeBuilder: MessageDigest initialised ");
         // Sort Object so that all lists can be compared.
        Collections.sort(objList);
        logger.info("TokenTreeBuilder: Sorting of object completed ");
         /*
         Create a new block.
          */
         if (objList.size() <= 1) {
             logger.info("Must be at least two signatures to construct a Merkle tree Current Size: "+ objList.size());
             return null;
         }
        logger.info("TokenTreeBuilder: Size of array is more than 1, so continue");

         nnodes = objList.size();
             List<TokenTree> parents = bottomLevel(objList);
             nnodes += parents.size();
             depth = 1;

        logger.info("TokenTreeBuilder: Leafs created Number : "+ parents.size());

             while (parents.size() > 1) {
                 parents = internalLevel(parents);
                 depth++;
                 nnodes += parents.size();
             }
        logger.info("TokenTreeBuilder: All parents merged to one : "+ parents.size());

        //root = parents.get(0);// Root hash has been moved to Blockchain level.
        Blockchain blockchain = Blockchain.getInstance();
        //Blockchain blockchain = Blockchain.getInstance();

        logger.info("TokenTreeBuilder: Initializing block chain creation");
        Block block = new Block(new Date().getTime(), getCurrentBlockNumber(), parents.get(0).digest, getOldBlockHash(), objList);
         // /objList.get(1).getBytes(Charsets.UTF_8);
        logger.info("TokenTreeBuilder: Block chain created now returning to main thread.");
         return block;
    }

    private int getCurrentBlockNumber() {
        logger.info("TokenTreeBuilder: Entering getCurrentBlockNumber method");
        return  Blockchain.getInstance().getBlockchainLength() +1;
    }

    private String getOldBlockHash() {

        logger.info("TokenTreeBuilder: Entering getOldBlockHash method");
        if (Blockchain.getInstance().getBlockchainLength()<1){
            logger.info("TokenTreeBuilder: Returning gotGenesisBlock");
            return "GenesisBlock";
        }else {
            logger.info("TokenTreeBuilder: getOldBlockHash Returning:  "+ Blockchain.getInstance().getBlock(Blockchain.getInstance().getBlockchainLength()-1).blockHash);
            return Blockchain.getInstance().getBlock(Blockchain.getInstance().getBlockchainLength()-1).blockHash;
        }

    }

    public int getNumNodes() {
        return nnodes;
    }
//
//    public TokenTree getRoot() {
//        return root;
//    }

    public int getHeight() {
        return depth;
    }


    /**
     * Constructs an internal level of the tree
     */
    List<TokenTree> internalLevel(List<TokenTree> children) {
        logger.info("TokenTreeBuilder: Entering internalLevel method");
        List<TokenTree> parents = new ArrayList<TokenTree>(children.size() / 2);

        for (int i = 0; i < children.size() - 1; i += 2) {
            TokenTree child1 = children.get(i);
            TokenTree child2 = children.get(i+1);

            TokenTree parent = constructInternalNode(child1, child2);
            parents.add(parent);
        }

        if (children.size() % 2 != 0) {
            TokenTree child = children.get(children.size()-1);
            TokenTree parent = constructInternalNode(child, null);
            parents.add(parent);
        }
        logger.info("TokenTreeBuilder: Exiting internalLevel method");

        return parents;
    }


    /**
     * Constructs the bottom part of the tree - the leaf nodes and their
     * immediate parents.  Returns a list of the parent nodes.
     */
    List<TokenTree> bottomLevel(ArrayList<BlockChainAuthorisedObj> objs) {
        logger.info("TokenTreeBuilder: entering bottomlevel method");
        List<TokenTree> parents = new ArrayList<TokenTree>(objs.size() / 2);

        for (int i = 0; i < objs.size() - 1; i += 2) {
            TokenTree leaf1 = constructLeafNode(objs.get(i).toString());
            TokenTree leaf2 = constructLeafNode(objs.get(i+1).toString());

            TokenTree parent = constructInternalNode(leaf1, leaf2);
            parents.add(parent);
        }

        // if odd number of leafs, handle last entry
        if (objs.size() % 2 != 0) {
            TokenTree leaf = constructLeafNode(objs.get(objs.size() - 1).toString());
            TokenTree parent = constructInternalNode(leaf, null);
            parents.add(parent);
        }

        logger.info("TokenTreeBuilder: Exiting bottomlevel method");
        return parents;
    }

    private TokenTree constructInternalNode(TokenTree child1, TokenTree child2) {
        logger.info("TokenTreeBuilder: Entering constructInternalNode method");
        TokenTree parent = new TokenTree(md);
        parent.type = INTERNAL_SIG_TYPE;

        if (child2 == null) {
            parent.digest = child1.digest;
        } else {
            parent.digest = DatatypeConverter.printHexBinary(internalHash(child1.digest.getBytes(Charsets.UTF_8)
                    , child2.digest.getBytes(Charsets.UTF_8)));
        }

        parent.setLeftTree(child1);
        parent.setLeftTree(child2);
        logger.info("TokenTreeBuilder: Exiting constructInternalNode method");
        return parent;
    }

    private static TokenTree constructLeafNode(String signature) {
        logger.info("TokenTreeBuilder: Entering constructLeafNode method");
        TokenTree leaf = new TokenTree(md);
        leaf.type = LEAF_SIG_TYPE;
        // We were initially using this field to capture Data but we will be capturing that as part of sorted list.
        // Else we can just keep value of object in leaf node.
        // Currently Hash is set
        leaf.digest = DatatypeConverter.printHexBinary(signature.getBytes(Charsets.UTF_8));
        logger.info("TokenTreeBuilder:  constructLeafNode method");
        return leaf;
    }

    byte[] internalHash(byte[] leftChildSig, byte[] rightChildSig) {
        logger.info("TokenTreeBuilder: Entering internalHash method");
        crc.reset();
        crc.update(leftChildSig);
        crc.update(rightChildSig);
        logger.info("TokenTreeBuilder: Exiting internalHash method");
        return longToByteArray(crc.getValue());

    }

    /**
     * Big-endian conversion
     */
    public static byte[] longToByteArray(long value) {
        logger.info("TokenTreeBuilder: Entering longToByteArray method");
        return new byte[] {
                (byte) (value >> 56),
                (byte) (value >> 48),
                (byte) (value >> 40),
                (byte) (value >> 32),
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
        };
    }
}
