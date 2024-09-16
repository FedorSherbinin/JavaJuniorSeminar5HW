package ru.gb;

import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5555)) {
            System.out.println("Подключено к серверу!");

            // Поток для чтения сообщений от сервера
            new Thread(new ReceiveMessages(socket)).start();

            // Поток для отправки сообщений на сервер
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            String message;
            while ((message = consoleReader.readLine()) != null) {
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Поток для приёма сообщений от сервера
    static class ReceiveMessages implements Runnable {
        private Socket socket;

        public ReceiveMessages(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Сообщение: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
