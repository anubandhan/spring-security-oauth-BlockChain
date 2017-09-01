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

/**
 * End leaf object that will hold actual values.
 * This we are not using any more. we are reusing Token Tree element
 */

public class TokenLeaf {

    // The data to be stored in this node
    private final BlockChainAuthorisedObj obj;

    /**
     * Initialises the leaf node, which consists
     * of the specified block of data.
     *
     * @param obj Data block to be placed in the leaf node
     */
    public TokenLeaf(final BlockChainAuthorisedObj obj)
    {
        this.obj = obj;
    }

    /**
     * @return The data block associated with this leaf node
     */
    public BlockChainAuthorisedObj getLeafContent()
    {
        return (obj);
    }

}
