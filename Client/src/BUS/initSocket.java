package BUS;

import javax.swing.*;
import java.net.Socket;

public class initSocket {
    private final int port = 8000;
    private final String host = "localhost";

    public initSocket() {
        try {
            Socket socket = new Socket(host, port);
            new ClientSideThread(socket).start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection refused! Please check the server status.");
        }
    }
}
