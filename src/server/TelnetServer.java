package server;

import client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TelnetServer {
    private final int port = 55555;

    private TelnetServer(){}
    private static volatile TelnetServer instance = null;

    public static TelnetServer getInstance() {
        if (instance == null) {
            synchronized (TelnetServer.class) {
                if (instance == null) {
                    instance = new TelnetServer();
                }
            }
        }
        return instance;
    }
    public void runServer() {
        Thread clientThread = null;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server connected and listen to port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connect from" + clientSocket.getInetAddress().getHostAddress());

                clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert clientThread != null;
            clientThread.stop();
        } finally {
            assert clientThread != null;
            clientThread.stop();
        }
    }
}
