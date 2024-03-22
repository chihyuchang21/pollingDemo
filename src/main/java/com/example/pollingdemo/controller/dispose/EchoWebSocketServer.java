package com.example.pollingdemo.controller.dispose;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoWebSocketServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port); // 監聽6666端口
            System.out.println("EchoWebSocketServer started. Listening on port " + port);
            clientSocket = serverSocket.accept(); // 等待客戶端連接
            System.out.println("Client connected.");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) { // 循環讀取client的msg
                if (".".equals(inputLine)) {
                    out.println("good bye"); // client sent"."，then reply "good bye"
                    break;
                }
                out.println(inputLine); // 收到的東西送回client
            }
        } catch (IOException e) {
            System.err.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.err.println(e.getMessage());
        } finally {
            stop(); // clean
        }
    }

    public void stop() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            System.out.println("EchoWebSocketServer stopped.");
        } catch (IOException e) {
            System.err.println("Error closing the connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        EchoWebSocketServer server = new EchoWebSocketServer();
        server.start(6666); // 6666 port
    }
}
