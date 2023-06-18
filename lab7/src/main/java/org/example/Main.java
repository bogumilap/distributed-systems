package org.example;

public class Main {
    public static void main(String[] args) {
        // read the arguments and create the proper executor
        if (args.length < 2) {
            System.out.println("The required arguments are: <port> <path to graphical application>.");
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
