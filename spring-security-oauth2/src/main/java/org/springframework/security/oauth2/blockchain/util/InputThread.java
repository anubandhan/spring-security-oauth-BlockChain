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

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * InputThread only reads data from a peer, and never sends data to prevent blocking and waiting, or some terrible constant back-and-forth keepalive.
 * All data read in is stored in an ArrayList<String>, with each line stored independently.
 * Data is accessed through a passthrough all the way through PeerNetwork.
 */
public class InputThread extends Thread
{
    private Socket socket;

    //Private instead of public so that object can control calls to receivedData. Acts as a buffer... the same data shouldn't be read more than once.
    private ArrayList<String> receivedData = new ArrayList<String>();

    /**
     * Constructor to set class socket variable
     */
    public InputThread(Socket socket)
    {
        this.socket = socket;
    }

    /**
     * Constantly reads from the input stream of the socket, and saves any received data to the ArrayList<St
     */
    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            while ((input = in.readLine()) != null)
            {
                receivedData.add(input);
                //System.out.println("RUN(): " + input);
                //System.out.println("size: " + receivedData.size());
            }
        } catch (Exception e)
        {
            System.out.println("Peer " + socket.getInetAddress() + " disconnected.");
        }
    }

    /**
     * Doesn't actually 'read data' as that's done asynchronously in the threadded run function.
     * However, readData is an easy way to think about it--as receivedData acts as a buffer, holding received data until the daemon is ready to handle it.
     * Generally, the size of receivedData will be small. However, in some instances (like when downloading many blocks), it can grow quickly.
     *
     * @return ArrayList<String> Data pulled from receivedData
     */
    @SuppressWarnings("unused")
    public ArrayList<String> readData()
    {
        //System.out.println("readData() called!");
        //System.out.println("We have " + receivedData.size() + " pieces!");
        //Don't want to mess with the ArrayList while run() is modifying it.
        ArrayList<String> inputBuffer = new ArrayList<String>(receivedData);
        if (inputBuffer == null)
        {
            inputBuffer = new ArrayList<String>();
        }
        receivedData = new ArrayList<String>(); //Resets 'buffer'
        return inputBuffer;
    }
}