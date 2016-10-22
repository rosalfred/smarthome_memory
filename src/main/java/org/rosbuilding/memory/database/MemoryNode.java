/**
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.Node;
import org.rosbuilding.common.BaseSimpleNode;
import org.rosbuilding.memory.database.internal.InfluxDb;
import org.rosbuilding.memory.database.internal.StateDataWatcher;
import org.rosbuilding.memory.database.internal.TopicWatcher;

/**
 * Memory ROS Node.
 *
 * @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
 *
 */
public class MemoryNode extends BaseSimpleNode<MemoryConfig> {

    private InfluxDb influxDb;
    private StateDataWatcher stateDataManager;
    private TopicWatcher topicWatcher;

    public MemoryNode() {
        super("memory");
    }

    @Override
    public void onStart(Node connectedNode) {
        super.onStart(connectedNode);

        this.initialize();
    }

    @Override
    public void onShutdown(Node node) {
        this.stateDataManager.removeAllTopics();
        super.onShutdown(node);
    }

    @Override
    protected MemoryConfig getConfig() {
        return new MemoryConfig(this.getConnectedNode());
    }

    private void initialize() {

        this.influxDb = new InfluxDb(this, this.configuration);
        this.stateDataManager = new StateDataWatcher(this.getConnectedNode(), this.influxDb);
        this.topicWatcher = new TopicWatcher(this.influxDb);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    MemoryNode.this.watchTopics();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }).start();
    }

    private void watchTopics() {
        HashMap<String, String> topicsTypes = this.getConnectedNode().getTopicNamesAndTypes();

        this.topicWatcher.checkTopics(topicsTypes);

        if (topicsTypes.size() > 0) {
            for (Entry<String, String> entity : topicsTypes.entrySet()) {
                this.stateDataManager.addTopic(entity.getKey(), entity.getValue());
            }
        }else {
            this.logI("Empty topics !");
        }
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
