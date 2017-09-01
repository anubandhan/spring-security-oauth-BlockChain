package org.springframework.security.oauth2.blockchain.util.merkletree;

import java.security.MessageDigest;

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
 * All notes and root element will be of type TokenTree
 */

// Traditionally a Merkel tree will be used. This is for representation purpose,
// Later we need to remove this implementation with a more sofisticated one.
// Ideal tree will also have Depth and Signatures and Signature definition constant.
// TODO: Serialization needs to be implemented.
public class TokenTree {

    // Child trees
    private TokenTree leftTree = null;
    private TokenTree rightTree = null;

    // Child leaves, INSTEAD OF DEDICATED CHILD LEAVES, WE WILL reuse TokenTree implementation only.
    // private TokenLeaf leftTokenLeaf = null;
    // private TokenLeaf rightTokenLeaf = null;
    public byte type;

    // The hash value of this node, Signature.
    public String digest;

    // The digest algorithm
    // We can skip this part.
    private MessageDigest md;


    public TokenTree getLeftTree() {
        return leftTree;
    }

    public void setLeftTree(TokenTree leftTree) {
        this.leftTree = leftTree;
    }

    public TokenTree getRightTree() {
        return rightTree;
    }

    public void setRightTree(TokenTree rightTree) {
        this.rightTree = rightTree;
    }

    /**
     * Initialises an empty Merkle Tree using the specified
     * digest algorithm.
     *
     * @param md The message digest algorithm to be used by the tree
     */
    public TokenTree(MessageDigest md)
    {
        this.md = md;
    }


}
