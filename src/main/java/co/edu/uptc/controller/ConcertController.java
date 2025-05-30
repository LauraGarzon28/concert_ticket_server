package co.edu.uptc.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import co.edu.uptc.connection.ServerThread;
import co.edu.uptc.model.ConcertManager;

public class ConcertController {

    private ServerSocket serverSocket;
    private ConcertManager concertManager;
    
    public ConcertController(int port) {
        concertManager = new ConcertManager();
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado en el puerto " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Cliente Conectado: " + socket.getInetAddress());
            new ServerThread(socket, concertManager).start();
        }
    }

    public static int getPort() {
        try (InputStream input = new FileInputStream("./config.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            return Integer.parseInt(properties.getProperty("port"));
        } catch (IOException e) {
            e.printStackTrace();
            return 1234;
        }
    }

    public static void main(String[] args) throws Exception {
        new ConcertController(getPort()).start();
    }

}
