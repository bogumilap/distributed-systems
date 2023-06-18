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
            System.out.println(monitoredNode + " node doesn't exist.");
        }
    }

    @Override
    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.None && event.getState() == Event.KeeperState.Expired) {
            executor.close();
        }
        // if new children was created or deleted, print the new number of children
        else if (event.getType() == Event.EventType.NodeChildrenChanged) {
            long descendantsNumber = countDescendants(monitoredNode);
            if (descendantsNumber != -1) {
                System.out.println("The number of descendants is " + descendantsNumber);
            }
        }
        // if the change related to the followed znode path, check its stat
        else if (path != null && path.equals(monitoredNode)) {
            zooKeeper.exists(monitoredNode, true, this, null);
        }
    }

    private long countDescendants(String path) {
        try {
            List<String> children = zooKeeper.getChildren(path, this);
            return children.size() + children.stream().mapToLong(child -> countDescendants(path + "/" + child)).sum();
        } catch (KeeperException | InterruptedException e) {
            System.out.printf(path + " node doesn't exist.");
            return -1;
        }
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        switch (Code.get(rc)) {
            // if everything is ok, inform the listener about it
            case OK -> executor.executeApp();
            // if there is no node, session expired or no auth, inform listener about closing
            case NONODE, SESSIONEXPIRED, NOAUTH -> executor.close();
            // check the watched znode stat
            default -> zooKeeper.exists(monitoredNode, true, this, null);
        }
    }
}