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

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.nio.charset.Charset;


/**
 * Object inside unpublished list and Block.
 */
public class BlockChainAuthorisedObj implements Comparable{
    private String ClientID;
    private String UserID;
    private String PublisherID; // This is for future, out of POC scope.
    private OAuth2AccessToken token;

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPublisherID() {
        return PublisherID;
    }

    public void setPublisherID(String publisherID) {
        PublisherID = publisherID;
    }

    public OAuth2AccessToken getToken() {
        return token;
    }

    public void setToken(OAuth2AccessToken token) {
        this.token = token;
    }

    public byte[] getBytes(Charset charset){
        return toString().getBytes(charset);
    }



    @Override
    public String toString() {
        return "BlockChainAuthorisedObj{" +
                "ClientID='" + ClientID + '\'' +
                ", UserID='" + UserID + '\'' +
                ", PublisherID='" + PublisherID + '\'' +
                ", token=" + token +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockChainAuthorisedObj)) return false;

        BlockChainAuthorisedObj that = (BlockChainAuthorisedObj) o;

        if (ClientID != null ? !ClientID.equals(that.ClientID) : that.ClientID != null) return false;
        if (UserID != null ? !UserID.equals(that.UserID) : that.UserID != null) return false;
        if (PublisherID != null ? !PublisherID.equals(that.PublisherID) : that.PublisherID != null) return false;
        return token != null ? token.equals(that.token) : that.token == null;

    }

    @Override
    public int hashCode() {
        int result = ClientID != null ? ClientID.hashCode() : 0;
        result = 31 * result + (UserID != null ? UserID.hashCode() : 0);
        result = 31 * result + (PublisherID != null ? PublisherID.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Object o) {
        return token.getValue().compareTo(((BlockChainAuthorisedObj)o).getToken().getValue().toString());
    }
}
