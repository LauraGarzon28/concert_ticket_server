package co.edu.uptc.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
    }

    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
        output.flush();
    }

    public Object receiveObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    public void disconnect() throws IOException {
        input.close();
        output.close();
        socket.close();
    }
}
