package com.pkg.se2einzelphase.misc;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class socketThread extends Thread {
    private String serverResponse;
    private final String serverRequest;
    private final String serverAddress;
    private final int serverPort;
    private final TextView textView;
    private static final int READ_TIMEOUT = 5000;

    public socketThread(String serverRequest, String serverAddress, int serverPort, TextView textView) throws IllegalArgumentException {
        if (serverRequest == null || serverAddress == null || serverPort < 0 || textView == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        this.serverRequest = serverRequest;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.textView = textView;
    }

    @Override
    public void run() {
        try(Socket socket = new Socket(serverAddress, serverPort)) {
            socket.setSoTimeout(READ_TIMEOUT);
            socket.getOutputStream().write((serverRequest + "\n").getBytes());
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            serverResponse = bufferedReader.readLine();
        } catch (Exception e) {
            serverResponse = e.getMessage();
        }
        textView.post(() -> textView.setText(serverResponse));
    }
}
