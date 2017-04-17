/*
 * Decompiled with CFR 0_121.
 */
package com.example.linalg.internal.multiply;

import com.example.linalg.internal.multiply.MultiplicationTaskGenerator;
import com.example.linalg.internal.multiply.Request;
import com.example.linalg.util.Pair;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RPCClient
implements Runnable {
    Socket clientSocket;
    Gson GSON = new Gson();

    public void stop() {
    }

    @Override
    public void run() {
        try {
            DataOutputStream output = new DataOutputStream(this.clientSocket.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            do {
                Request R = new Request("get", null, null);
                output.write(this.GSON.toJson(R).getBytes());
                output.write("\n".getBytes());
                output.flush();
                String messageFromServer = input.readLine();
                if (messageFromServer == null) continue;
                MultiplicationTaskGenerator.SubMatrixTask smt = this.GSON.fromJson(messageFromServer, MultiplicationTaskGenerator.SubMatrixTask.class);
                if (smt == null) break;
                LinkedList<Double> updateValues = new LinkedList<Double>();
                for (Pair<Integer, Integer> task : smt.tasks) {
                    double[] a = smt.submatrixA.get(task.getElement0());
                    double[] b = smt.submatrixB.get(task.getElement1());
                    double c = 0.0;
                    for (int i = 0; i < a.length; ++i) {
                        c += a[i] * b[i];
                    }
                    updateValues.add(c);
                }
                Request updateRequest = new Request("update", smt.tasks, updateValues);
                output.write(this.GSON.toJson(updateRequest).getBytes());
                output.write("\n".getBytes());
                output.flush();
            } while (true);
            input.close();
            output.close();
            this.clientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            try {
                this.clientSocket.close();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public RPCClient(String server, int port) {
        try {
            this.clientSocket = new Socket(server, port);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

