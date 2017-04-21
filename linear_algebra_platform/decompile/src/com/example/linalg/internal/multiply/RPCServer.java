/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.multiply;

import com.example.linalg.internal.multiply.MultiplicationTaskGenerator;
import com.example.linalg.internal.multiply.RPCClientHandler;
import com.example.linalg.util.Pair;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class RPCServer
implements Runnable {
    BlockingQueue<MultiplicationTaskGenerator.SubMatrixTask> tasks;
    ConcurrentHashMap<Pair<Integer, Integer>, Double> MATRIX_C;
    int serverPort = 8082;
    ServerSocket serverSocket = null;
    boolean isStopped = false;
    Thread runningThread = null;
    LinkedList<RPCClientHandler> clients = new LinkedList();

    @Override
    public void run() {
        this.runningThread = Thread.currentThread();
        this.openServerSocket();
        while (!this.isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            }
            catch (IOException e) {
                if (this.isStopped()) {
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            Thread rpcH = new Thread(new RPCClientHandler(this.tasks, clientSocket, this));
            rpcH.setName("RPCClientHandler-" + rpcH.getId());
            rpcH.start();
        }
        System.out.println("Server Stopped.");
    }

    private boolean isStopped() {
        return this.isStopped;
    }

    public void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
        for (RPCClientHandler client : this.clients) {
            if (client == null) continue;
            try {
                client.stop();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }

    public RPCServer(Collection<MultiplicationTaskGenerator.SubMatrixTask> c, ConcurrentHashMap<Pair<Integer, Integer>, Double> MAT, int port) {
        this.MATRIX_C = MAT;
        this.serverPort = port;
        this.tasks = new LinkedBlockingQueue<MultiplicationTaskGenerator.SubMatrixTask>();
        for (MultiplicationTaskGenerator.SubMatrixTask t : c) {
            this.tasks.add(t);
        }
    }

    public ConcurrentHashMap<Pair<Integer, Integer>, Double> getResult() {
        return this.MATRIX_C;
    }

    public boolean hasTask() {
        return !this.tasks.isEmpty();
    }

    public MultiplicationTaskGenerator.SubMatrixTask getTask() {
        try {
            if (!this.tasks.isEmpty()) {
                return this.tasks.take();
            }
            return null;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}

