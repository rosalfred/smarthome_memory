/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.Node;

import org.rosbuilding.common.BaseSimpleNode;
import org.rosbuilding.memory.tsdb.InfluxManager;
import org.rosbuilding.memory.tsdb.TimeSerieManager;
import org.rosbuilding.memory.watcher.NodeWatcher;
import org.rosbuilding.memory.watcher.TopicWatcher;

/**
 * Memory ROS Node.
 *
 * @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class MemoryNode extends BaseSimpleNode<MemoryConfig> {

    public static final String NAME = "/memory";

    /** Cached manager. */
    private CachedManager cachedManager;

    /** Watch lifecycle Topics */
    private TopicWatcher topicWatcher;

    /** Watch lifecycle Nodes */
    private NodeWatcher nodeWatcher;

    @Override
    public void onStart(Node connectedNode) {
        super.onStart(connectedNode);

        TimeSerieManager timeSerieManager = new InfluxManager(this, this.configuration);
        this.cachedManager = new CachedManager(this.getConnectedNode(), timeSerieManager);

        // Watcher of Topics. Detecte if new or destroy topics.
        this.topicWatcher = new TopicWatcher(this.cachedManager);
        this.topicWatcher.start();

        this.nodeWatcher = new NodeWatcher(this.cachedManager);
        this.nodeWatcher.start();
    }

    @Override
    public void onShutdown() {
        this.nodeWatcher.stop();
        this.topicWatcher.stop();

        this.cachedManager.clear();

        super.onShutdown();
    }

    @Override
    protected MemoryConfig makeConfiguration() {
        return new MemoryConfig(this.getConnectedNode());
    }

    public static void main(String[] args) throws InterruptedException {
        RCLJava.rclJavaInit();

        final MemoryNode memory = new MemoryNode();
        final Node node = RCLJava.createNode(NAME);

        memory.onStart(node);
        memory.onStarted();

        RCLJava.spin(node);

        memory.onShutdown();
        memory.onShutdowned();

        RCLJava.shutdown();
    }
}
