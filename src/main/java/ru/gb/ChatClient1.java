package ru.gb;

import java.io.*;
import java.net.*;

public class ChatClient1 {
    private static final String SERVER_ADDRESS = "localhost"; // Адрес сервера
    private static final int SERVER_PORT = 12345; // Порт сервера

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            // Поток для чтения сообщений с сервера
            new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Запрос имени пользователя
            System.out.print("Введите ваше имя: ");
            String name = stdIn.readLine();
            out.println(name); // Отправка имени пользователя на сервер

            // Основной поток для отправки сообщений на сервер
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
