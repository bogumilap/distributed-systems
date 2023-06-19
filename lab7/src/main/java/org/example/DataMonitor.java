package org.example;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.data.Stat;

public class DataMonitor implements Watcher, StatCallback {
    private final ZooKeeper zooKeeper;
    private final String monitoredNode;
    private final Executor executor;

    public DataMonitor(ZooKeeper zooKeeper, String monitoredNode, Executor executor) {
        this.zooKeeper = zooKeeper;
        this.monitoredNode = monitoredNode;
        this.executor = executor;
        zooKeeper.exists(monitoredNode, true, this, null);
        try {
            zooKeeper.getChildren(monitoredNode, this);
        } catch (KeeperException | InterruptedException e) {
            System.out.println("No child nodes were found for " + monitoredNode);
        }
    }

    private long countDescendants(String path) {
        try {
            List<String> children = zooKeeper.getChildren(path, this);
            return children.size() + children.stream().mapToLong(child -> countDescendants(path + "/" + child)).sum();
        } catch (KeeperException | InterruptedException e) {
            System.out.println("No child nodes were found for " + monitoredNode);
            return -1;
        }
    }

    private void printDescendantsNumber() {
        long descendantsNumber = countDescendants(monitoredNode);
        if (descendantsNumber != -1) {
            String message = "The number of descendants is " + descendantsNumber;
            String separator = "";
            for (int i=0; i<message.length() + 4; i++) {
                separator = separator.concat("─");
            }
            System.out.println("\n┌" + separator + "┐\n|  " + message + "  |\n└" + separator + "┘\n");
        }
    }

    @Override
    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.NodeCreated) {
            try {
                zooKeeper.getChildren(monitoredNode, this);
            } catch (KeeperException | InterruptedException e) {
                System.out.println("No child nodes were found for " + monitoredNode);
            }
        }
        if (event.getType() == Event.EventType.NodeChildrenChanged) {
            printDescendantsNumber();
        }
        else if (event.getType() == Event.EventType.None && event.getState() == Event.KeeperState.Expired) {
            executor.close();
        }
        // if the change related to the followed znode path, check its stat
        else if (path != null && path.equals(monitoredNode)) {
            zooKeeper.exists(monitoredNode, true, this, null);
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        switch (Code.get(rc)) {
            case OK -> executor.executeApp();
            case NONODE, SESSIONEXPIRED, NOAUTH -> executor.close();
            default -> zooKeeper.exists(monitoredNode, true, this, null);
        }
    }
}