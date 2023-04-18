package giezz.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public void start(int port) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new Session(clientSocket)).start();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8086);
    }
}
