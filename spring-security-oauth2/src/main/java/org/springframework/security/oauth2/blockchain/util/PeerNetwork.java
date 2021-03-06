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
import java.util.*;

/**
 * This thread listens on a provided port (8015 by default) for incoming connections, and attempts to make connections to external peers based on guidance from MainClass.
 * It needs a bit of help with memory management and resource deallocation, but otherwise it works. Good enough for 2.0.0a1.
 *
 *
 * Incoming and outgoing Threads. Sample threads from Coin is in project.
 *
 */


public class PeerNetwork extends Thread
{
    public int listenPort;
    public boolean shouldRun = true;
    public ArrayList<PeerThread> peerThreads;

    public ArrayList<String> newPeers;
    /**
     * Default settings constructor
     */
    public PeerNetwork()
    {
        this.listenPort = 8015;
        this.peerThreads = new ArrayList<PeerThread>();
        this.newPeers = new ArrayList<String>();
    }

    /**
     * Attempts a connection to an external peer
     *
     * @param peer Peer to connect to
     * @param port Port on peer to connect to
     */
    public void connectToPeer(String peer, int port)
    {
        try
        {
            Socket socket = new Socket(peer, port);
            String remoteHost = socket.getInetAddress() + "";
            remoteHost = remoteHost.replace("/", "");
            remoteHost = remoteHost.replace("\\", "");
            int remotePort = socket.getPort();
            newPeers.add(remoteHost + ":" + remotePort);
            peerThreads.add(new PeerThread(socket));
            peerThreads.get(peerThreads.size() - 1).start();
        } catch (Exception e)
        {
            System.out.println("Unable to connect to " + peer + ":" + port);
        }
    }

    /**
     * Optional, currently-unused constructor for a non-default port selection
     *
     * @param port Port to listen on
     */
    public PeerNetwork(int port)
    {
        this.listenPort = port;
        this.peerThreads = new ArrayList<PeerThread>();
    }

    /**
     * Runs as a separate thread, constantly listening for peer connections.
     */
    public void run()
    {
        try
        {
            ServerSocket listenSocket = new ServerSocket(listenPort);
            while (shouldRun) //Doesn't actually quit right when shouldRun is changed, as while loop is pending.
            {
                peerThreads.add(new PeerThread(listenSocket.accept()));
                peerThreads.get(peerThreads.size() - 1).start();
            }
            listenSocket.close();
        } catch (Exception e)
        {
            e.printStackTrace(); //Most likely tripped by the inability to bind the listenPort.
        }
    }

    /**
     * Announces the same message to all peers simultaneously. Useful when re-broadcasting messages.
     *
     * @param toBroadcast String to broadcast to peers
     */
    public void broadcast(String toBroadcast)
    {
        for (int i = 0; i < peerThreads.size(); i++)
        {
            System.out.println("Sent:: " + toBroadcast);
            peerThreads.get(i).send(toBroadcast);
        }
    }

    /**
     * Announces the same message to all peers except the ignored one simultaneously. Useful when re-broadcasting messages.
     * Peer ignored as it's the peer that sent you info.
     *
     * @param toBroadcast String to broadcast to peers
     * @param peerToIgnore Peer to not send broadcast too--usually the peer who sent information that is being rebroadcast
     */
    public void broadcastIgnorePeer(String toBroadcast, String peerToIgnore)
    {
        for (int i = 0; i < peerThreads.size(); i++)
        {
            peerThreads.get(i).send(toBroadcast);
        }
    }
}