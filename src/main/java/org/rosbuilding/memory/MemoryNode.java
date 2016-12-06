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

/**
 * Memory ROS Node.
 *
 * @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class MemoryNode extends BaseSimpleNode<MemoryConfig> {

    /** Time Serie DataBase */
    private TimeSerieManager timeSerieManager;

    /** Subscription manager. */
    private SubscriberManager subscriberManager;

    /** Watch lifecycle Topics */
    private TopicWatcher topicWatcher;

    @Override
    public void onStart(Node connectedNode) {
        super.onStart(connectedNode);

        this.timeSerieManager = new InfluxManager(this, this.configuration);
        this.subscriberManager = new SubscriberManager(this.getConnectedNode(), this.timeSerieManager);

        // Watcher of Topics. Detecte if new or destroy topics.
        this.topicWatcher = new TopicWatcher(this.getConnectedNode(), this.subscriberManager);
        this.topicWatcher.start();
    }

    @Override
    public void onShutdown(Node node) {
        this.topicWatcher.stop();
        this.subscriberManager.clear();

        super.onShutdown(node);
    }

    @Override
    protected MemoryConfig makeConfiguration() {
        return new MemoryConfig(this.getConnectedNode());
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize RCL
        RCLJava.rclJavaInit();

        // Let's create a Node
        Node node = RCLJava.createNode("memory");

        MemoryNode memory = new MemoryNode();
        memory.onStart(node);

        RCLJava.spin(node);

        memory.onShutdown(node);
        node.dispose();
        RCLJava.shutdown();
    }
}
