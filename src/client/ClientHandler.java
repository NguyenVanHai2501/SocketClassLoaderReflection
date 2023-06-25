package client;

import classLoader.CustomClassLoader;
import exception.InputNotSupportException;
import type.MathType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private Integer calculator = null;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            writer.println("Welcome to Math Server!");
            writer.println("Enter 'quit' to exit.");

            do {
                inputLine = reader.readLine();
                if (inputLine.trim().equalsIgnoreCase("quit")) {
                    break;
                }
                System.out.println("Client: " + inputLine);
                try {
                    calculator = doMath(inputLine);
                    if (calculator == null) {
                        continue;
                    }
                } catch (Exception e) {
                    writer.println(e.getMessage());
                    e.printStackTrace();
                    continue;
                }
                System.out.println("Result: " + calculator);
                writer.println(calculator);

            } while (true);

            close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void close() throws IOException {
        clientSocket.close();
        System.out.println("Close connect to " + clientSocket.getInetAddress().getHostAddress());
    }

    private Integer doMath(String input) throws Exception {
        List<String> listInput = new ArrayList<>(List.of(input.split(",")));
        if (listInput.size() != 3) {
            throw new InputNotSupportException("Input not support: " + input + ". Enter with format: MATH, INTEGER, INTEGER");
        }
        MathType mathType = convert2MathType(listInput.get(0));
        CustomClassLoader customClassLoader = new CustomClassLoader();
        try {
            Object obj;
            obj = customClassLoader.findClass("math." + mathType).newInstance();
            System.out.println(obj);
            Method method = obj.getClass().getDeclaredMethod("calculator",int.class, int.class);
            Integer result = (Integer) method.invoke(obj, Integer.parseInt(listInput.get(1)), Integer.parseInt(listInput.get(2)));
            return result;

        } catch (ClassFormatError | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private MathType convert2MathType(String math) {
        switch (math.trim().toUpperCase()) {
            case "ADD":
                return MathType.Add;
            case "SUB":
                return MathType.Sub;
            case "MUL":
                return MathType.Mul;
            case "DIV":
                return MathType.Div;
            case "POW":
                return MathType.Pow;
            default:
                throw new IllegalArgumentException("Not support with math: " + math);
        }
    }
}
