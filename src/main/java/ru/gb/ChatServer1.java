package ru.gb;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer1 {
    private static Set<Socket> clientSockets = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            System.out.println("Сервер запущен на порту 5555...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новое подключение: " + clientSocket.getInetAddress());
                clientSockets.add(clientSocket);

                // Запуск нового потока для обработки клиента
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для рассылки сообщений всем клиентам
    public static void broadcast(String message, Socket excludeSocket) {
        for (Socket socket : clientSockets) {
            if (socket != excludeSocket) {
                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Обработчик клиента
    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Сообщение от клиента: " + message);
                    // Рассылка сообщения всем остальным клиентам
                    ChatServer1.broadcast(message, clientSocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clientSockets.remove(clientSocket);
                System.out.println("Клиент отключен.");
            }
        }
    }
}
