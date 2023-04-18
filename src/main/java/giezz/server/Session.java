package giezz.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

public class Session implements Runnable {

    private final Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("client is connected");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!socket.isClosed()) {
                int[] clientRequestData = new int[6];
                for (int i = 0; i < 6; i++) {
                    clientRequestData[i] = Integer.parseInt(reader.readLine());
                    System.out.print(clientRequestData[i] + " ");
                }
                System.out.println();
                writer.write(checkResults(clientRequestData));
                writer.flush();
            }
        } catch (IOException e) {
            if (e instanceof SocketException socketException && socketException.getMessage().equals("Connection reset")) {
                System.out.println("client disconnected");
            } else {
                e.printStackTrace();
            }
        }
    }

    private String checkResults(int[] data) {
        if (Arrays.stream(data).sum() == 30 * 6) return "winner";
        if (Arrays.stream(data).anyMatch(x -> x == 30) && Arrays.stream(data).allMatch(x -> x >= 20)) return "second place";
        if (Arrays.stream(data).allMatch(x -> x == 20)) return "third place";
        return "looser";
    }
}
