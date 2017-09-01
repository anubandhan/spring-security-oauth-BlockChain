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

import java.net.*;

/**
 * Class handles all networking after a socket is accepted. Delegates work into two separate threads,
 * one for incoming data, and one for outgoing data, so data in one direction doesn't block data in
 * the other.
 */
public class PeerThread extends Thread
{
    private Socket socket;
    public InputThread inputThread;
    public OutputThread outputThread;
    /**
     * Constructor sets socket
     *
     * @param socket Socket with peer
     */
    public PeerThread(Socket socket)
    {
        this.socket = socket;
    }

    /**
     * As the name might suggest, each PeerThread runs on its own thread. Additionally, each child network IO thread
     * runs on its own thread.
     */
    public void run()
    {
        System.out.println("Got connection from " + socket.getInetAddress() + ".");
        inputThread = new InputThread(socket);
        inputThread.start();
        outputThread = new OutputThread(socket);
        outputThread.start();
    }

    /**
     * Used to send data to a peer. Passthrough to outputThread.send()
     *
     * @param data String of data to send
     */
    public void send(String data)
    {
        if (outputThread == null)
        {
            System.out.println("Couldn't send " + data + " !!!!");
        }
        else
        {
            outputThread.write(data);
        }
    }
}