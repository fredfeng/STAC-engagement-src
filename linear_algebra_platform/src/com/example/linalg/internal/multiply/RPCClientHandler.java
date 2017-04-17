/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.multiply;

import com.example.linalg.internal.multiply.MultiplicationTaskGenerator;
import com.example.linalg.internal.multiply.RPCServer;
import com.example.linalg.internal.multiply.Request;
import com.example.linalg.util.Pair;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class RPCClientHandler
implements Runnable {
    BlockingQueue<MultiplicationTaskGenerator.SubMatrixTask> tasks;
    protected Socket clientSocket = null;
    RPCServer callback;
    Gson gson = new Gson();

    public void stop() {
        try {
            this.clientSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String messageFromServer;
            InputStream input = this.clientSocket.getInputStream();
            OutputStream output = this.clientSocket.getOutputStream();
            BufferedReader bIn = new BufferedReader(new InputStreamReader(input));
            while ((messageFromServer = bIn.readLine()) != null) {
                Request req = this.gson.fromJson(messageFromServer, Request.class);
                if (req.call.equalsIgnoreCase("GET")) {
                    MultiplicationTaskGenerator.SubMatrixTask smt = this.callback.getTask();
                    String resp = this.gson.toJson(smt);
                    output.write(resp.getBytes());
                    output.write("\n".getBytes());
                    output.flush();
                }
                if (!req.call.equalsIgnoreCase("update")) continue;
                assert (req.updatePoints.size() == req.updates.size());
                for (int i = 0; i < req.updatePoints.size(); ++i) {
                    try {
                        Pair<Integer, Integer> p = req.updatePoints.get(i);
                        Double f = req.updates.get(i);
                        this.callback.MATRIX_C.put(p, f);
                        continue;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            output.close();
            input.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RPCClientHandler(BlockingQueue<MultiplicationTaskGenerator.SubMatrixTask> tasks, Socket socket, RPCServer callback) {
        this.clientSocket = socket;
        this.tasks = tasks;
        this.callback = callback;
    }
}

