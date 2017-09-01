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
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * This class offers basic functionality for storing authorisedTokenList until they make it into a block.
 * It could be just an ArrayList<String> inside of MainClass, however it seemed easier and more OOP-ish to give it its own object.
 * Adding future functionality to pending transaction pool management is much easier when it has its own object.
 */
@Component
public class PendingAuthorisationContainer
{
    private static final Log logger = LogFactory.getLog(PendingAuthorisationContainer.class);

    public ArrayList<BlockChainAuthorisedObj> pendingAccessTokens;

    //ArrayList holding objects that pair addresses with their pending tokens, so authorisedTokenList above an account's spendable balance are rejected.
    // public ArrayList<StringLongPair> accountBalanceDeltaTables;
    /**
     * Constructor for PendingAuthorisationContainer sets up required ArrayList for holding authorisedTokenList.
     */
    private PendingAuthorisationContainer()
    {
        this.pendingAccessTokens = new ArrayList<BlockChainAuthorisedObj>();
        //this.accountBalanceDeltaTables = new ArrayList<StringLongPair>();
    }

    private static PendingAuthorisationContainer instance;


    //static block initialization for exception handling
    static{
        try{
            instance = new PendingAuthorisationContainer();
        }catch(Exception e){
            throw new RuntimeException("Exception occured in creating singleton instance");
        }
    }

    public static PendingAuthorisationContainer getInstance(){
        return instance;
    }


    /**
     * Adds a AccessToken to the pending AccessToken list if it is formatted correctly and accompanied by a correct signature. Does not check User authentication.
     * Rejects duplicate AccessToken.
     * Additional work in the future on this method will include keeping track of signature indexes and prioritizing lower-index authorisedTokenList.
     *
     *
     * @param clientID
     * @param userID
     * @param token
     *
     * @return boolean Whether adding the token was valid
     */
    public boolean addAccessToken(String clientID, String userID, OAuth2AccessToken token)
    {
        logger.info("PendingAuthorisationContainer:: addNewAuthorisationFromPeers method called: "+ token.getValue());
        try
        {
            for (int i = 0; i < pendingAccessTokens.size(); i++)
            {
                if (pendingAccessTokens.get(i).getToken().getValue().equals(token.getValue()))
                {
                    return false;
                }
            }
            pendingAccessTokens.add(createBlockChainAuthorisedObj(clientID, userID, token));
            logger.info("addAccessToken add token new Size: "+ pendingAccessTokens.size());
            new BlockMintThread().initializeJob();
            // Note: Minting thread will actualy clear Access tokens. because if there are only 1 token in a minute minting will not happen.
            logger.info("aStarting new Thread: "+ pendingAccessTokens.size());


        } catch (Exception e)
        {
            System.out.println("An exception has occurred...");
            e.printStackTrace();
            return false;
            //e.printStackTrace();
        }
        return true;
    }

    private BlockChainAuthorisedObj createBlockChainAuthorisedObj(String clientID, String userID, OAuth2AccessToken token) {
        BlockChainAuthorisedObj obj = new BlockChainAuthorisedObj();
        obj.setClientID(clientID);
        obj.setUserID(userID);
        obj.setToken(token);
        return obj;
    }

    private String createNewAccessTokenMerkelTree(String clientID, String userID, OAuth2AccessToken token) {

        return null;
    }

    /**
     * Self-explanatory method called whenever the daemon desires to reset the pending token pool to be blank.
     */
    public void reset()
    {
        pendingAccessTokens = new ArrayList<BlockChainAuthorisedObj>();
        logger.info("pendingAccessTokens Size Post reset: "+ pendingAccessTokens.size());
    }

    public ArrayList<BlockChainAuthorisedObj> getPendingAccessTokens()
    {
        return pendingAccessTokens;
    }



    /**
     * Removes an identical token from the pending token pool
     *
     * @param token The token to remove
     *
     * @return boolean Whether removal was successful
     */

    // TODO: This method will not be called for our POC purpose, but can be used to check if Token was already generated and made that to block then we
    // TODO: need to remove it from our pendingAccessTokens list.
    // TODO: Will be called from Peer Incoming connection.
    public boolean removeToken(OAuth2AccessToken token)
    {
        for (int i = 0; i < pendingAccessTokens.size(); i++)
        {
            if (pendingAccessTokens.get(i).getToken().getValue().equals(token.getValue())) //TODO: Same logic has to be here : as addAccessToken method
            {
                pendingAccessTokens.remove(i);
                return true;
            }
        }
        return false; //Transaction was not found in pending transaction pool
    }

    /**
     * In case minting was not successful list need to be repopulated.
     * @param pendingAuthorisationArrayList
     * @return
     */
    public boolean addAccessTokens(ArrayList<BlockChainAuthorisedObj> pendingAuthorisationArrayList) {

        return pendingAccessTokens.addAll(pendingAuthorisationArrayList);

    }
}