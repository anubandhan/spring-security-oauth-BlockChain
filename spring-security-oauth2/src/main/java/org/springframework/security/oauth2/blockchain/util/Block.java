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

import org.springframework.security.oauth2.blockchain.util.merkletree.BlockChainAuthorisedObj;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * A block contains:
 * -Timestamp (Unix Epoch)
 * -Block number
 * -Previous block hash
 * -Certificate
 * -Difficulty
 * -Winning nonce
 * -Token list
 */
public class Block
{
    public long timestamp;
    public int blockNum;
    public String previousBlockHash;
    public String blockHash;
    // public Certificate certificate;// TODO: We will not be using this field for POC purpose.
    // public long difficulty;// TODO: We will not be using this field for POC purpose.
    // public int winningNonce;// Currently we are assuming that the Hash that we generated is the winning nounce. else if nounce is greater than difficulty then ledgerHash needs toe be changed.// TODO: We will not be using this field for POC purpose.
   // public String ledgerHash;// TODO: We will not be using this field for POC purpose.
    public ArrayList<BlockChainAuthorisedObj> authorisedTokenList;
    //public String minerSignature; // TODO: We will not be using this field for POC purpose.
    //public long minerSignatureIndex; // TODO: We will not be using this field for POC purpose.



    public Block() {
    }

    /**
     * Constructor for Block object.
     *
     * @param timestamp Timestamp originally set into the block by the miner
     * @param blockNum The block number
     * @param previousBlockHash The hash of the previous block
     * @param difficulty The difficulty at the time this block was mined
     * @param winningNonce The nonce selected by a miner to create the block
     * @param ledgerHash The hash of the ledger as it existed before this block's authorisedTokenList occurred
     * @param authorisedTokenList ArrayList<String> of all the authorisedTokenList included in the block
     * @param minerSignature Miner's signature of the block
     * @param minerSignatureIndex Miner's signature index used when generating minerSignature
     */
    public Block(long timestamp, int blockNum, String blockHash, String previousBlockHash,  ArrayList<BlockChainAuthorisedObj> authorisedTokenList)
    {
        this.timestamp = timestamp;
        this.blockNum = blockNum;
        this.previousBlockHash = previousBlockHash;
        this.blockHash = blockHash;
        //this.certificate = certificate;
        //this.difficulty = difficulty;
        //this.winningNonce = winningNonce;
        //this.ledgerHash = ledgerHash;
        this.authorisedTokenList = authorisedTokenList;
        //this.minerSignature = minerSignature;
        //this.minerSignatureIndex = minerSignatureIndex;
        try
        {
            String tokenString = "";

            for (int i = 0; i < authorisedTokenList.size(); i++)
            {
                    tokenString += authorisedTokenList.get(i).toString() + "*";
            }
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            tokenString = tokenString.substring(0, tokenString.length() - 1);
            String blockData = "{" + timestamp + ":" + blockNum + ":" + previousBlockHash + ":"  + "},{" + tokenString + "},";
            this.blockHash = DatatypeConverter.printHexBinary(md.digest(blockData.getBytes("UTF-8")));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return "Block{" +
                "timestamp=" + timestamp +
                ", blockNum=" + blockNum +
                ", previousBlockHash='" + previousBlockHash + '\'' +
                ", blockHash='" + blockHash + '\'' +
                ", authorisedTokenList=" + authorisedTokenList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;

        Block block = (Block) o;

        if (timestamp != block.timestamp) return false;
        if (blockNum != block.blockNum) return false;
        if (previousBlockHash != null ? !previousBlockHash.equals(block.previousBlockHash) : block.previousBlockHash != null)
            return false;
        if (blockHash != null ? !blockHash.equals(block.blockHash) : block.blockHash != null) return false;
        return authorisedTokenList != null ? authorisedTokenList.equals(block.authorisedTokenList) : block.authorisedTokenList == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + blockNum;
        result = 31 * result + (previousBlockHash != null ? previousBlockHash.hashCode() : 0);
        result = 31 * result + (blockHash != null ? blockHash.hashCode() : 0);
        result = 31 * result + (authorisedTokenList != null ? authorisedTokenList.hashCode() : 0);
        return result;
    }


}
