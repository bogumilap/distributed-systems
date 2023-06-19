package org.example;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Expected 2 arguments: <port> <path to graphical application>.");
            System.exit(2);
        }
        String hostPort = args[0];
        String appPath = args[1];
        try {
            new Executor(hostPort, appPath).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
