package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Executor implements Watcher, Runnable {
    private DataMonitor dataMonitor;
    private final ZooKeeper zooKeeper;
    private final String zNode = "/z";
    private final String appPath;
    private Process childProcess;

    public Executor(String hostPort, String appPath) throws IOException {
        this.appPath = appPath;
        String address = "127.0.0.1";
        zooKeeper = new ZooKeeper(address + ":" + hostPort, 15000, this);
        if (dataMonitor == null) {
            dataMonitor = new DataMonitor(zooKeeper, zNode, this);
        }
    }

    private void printTree(String child, String path, int i) {
        try {
            List<String> children = zooKeeper.getChildren(path, dataMonitor);
            IntStream.rangeClosed(0, i).forEach(k -> System.out.print("  "));
            if (children.isEmpty()) {
                System.out.print("└ ");
            } else {
                System.out.print("├ ");
            }
            System.out.println(child);
            children.forEach(c -> printTree(c, path + "/" + c, i + 1));
        } catch (InterruptedException | KeeperException e) {
            System.out.println(zNode + " node doesn't exist.");
        }
    }

    public void executeApp() {
        try {
            if (childProcess == null) {
                System.out.println("Executing graphical app...");
                childProcess = Runtime.getRuntime().exec(appPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        synchronized (this) {
            if (childProcess != null) {
                System.out.println("Stopping graphical app...");
                childProcess.destroy();
                childProcess = null;
            }
        }
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean running = true;
        while (running) {
            System.out.println("Enter 'tree' to print tree structure or 'stop' to stop executor:");
            try {
                String line = reader.readLine();
                switch (line) {
                    case "tree" -> printTree(zNode, zNode, 0);
                    case "stop" -> {
                        running = false;
                        close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if (dataMonitor == null) {
            dataMonitor = new DataMonitor(zooKeeper, zNode, this);
        }
        dataMonitor.process(event);
    }
}