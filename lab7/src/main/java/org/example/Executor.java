package org.example;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor implements Watcher, Runnable {
    private final DataMonitor dataMonitor;
    private final ZooKeeper zooKeeper;
    private final String zNode = "/z";
    private final String appPath;
    private Process childProcess;

    public Executor(String hostPort, String appPath) throws IOException {
        this.appPath = appPath;
        String address = "127.0.0.1";
        zooKeeper = new ZooKeeper(address + ":" + hostPort, 20000, this);
        dataMonitor = new DataMonitor(zooKeeper, zNode, this);
    }

    @Override
    public void process(WatchedEvent event) {
        dataMonitor.process(event);
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Enter 'tree' to see the tree and 'stop' to stop:");
            String line = scanner.nextLine();
            switch (line) {
                case "tree" -> printTree(zNode, zNode, 0);
                case "stop" -> {
                    running = false;
                    close();
                }
            }
        }
    }

    private void printTree(String child, String path, int i) {
        try {
            List<String> children = zooKeeper.getChildren(path, dataMonitor);
            IntStream.rangeClosed(0, i).forEach(k -> System.out.print("  "));
            if (children.isEmpty()) {
                System.out.print("'-");
            } else {
                System.out.print("|-");
            }
            System.out.println(child);
            children.forEach(c -> printTree(c, path + "/" + c, i + 1));
        } catch (InterruptedException | KeeperException e) {
            System.out.println("No node for /z");
        }
    }

    public void executeApp() {
        try {
            System.out.println("Executing graphical app...");
            childProcess = Runtime.getRuntime().exec(appPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        synchronized (this) {
            System.out.println("Stopping graphical app...");
            if (childProcess != null) {
                childProcess.destroy();
            }
        }
    }
}