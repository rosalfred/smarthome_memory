/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ros2.rcljava.internal.message.Message;
import org.ros2.rcljava.node.Node;

import org.rosbuilding.memory.MemoryConfig;

import org.rosbuilding.memory.concept.internal.BadInfoException;
import org.rosbuilding.memory.concept.internal.MessageInfoBase;

import org.rosbuilding.memory.tsdb.TimeSerieFactory;
import org.rosbuilding.memory.tsdb.TimeSerieRepository;

import org.rosbuilding.memory.watcher.NodeWatcher;
import org.rosbuilding.memory.watcher.TopicWatcher;

public class MemoryManager {

    // Memory level.
    private ShortTimeMemory shortTimeMemory;
    private LongTimeMemory longTimeMemory;

    // Watcher level.
    /** Watch lifecycle Topics */
    private TopicWatcher topicWatcher;

    /** Watch lifecycle Nodes */
    private NodeWatcher nodeWatcher;

    private RosManager rosManager;

    public MemoryManager(final MemoryConfig config, Node connectedNode) {
        // Initialize ROS manager.
        this.rosManager = new RosManager(this, connectedNode);

     // Make DB provider of TimeSerie from Config
        TimeSerieRepository timeSerieManager = TimeSerieFactory.makeRepository(config);

        // Initialize Memory.
        this.shortTimeMemory = new ShortTimeMemory(this);
        this.longTimeMemory = new LongTimeMemory(this, timeSerieManager);

        // Watcher of Nodes. Detecte if nodes is alive.
        this.nodeWatcher = new NodeWatcher(this);


        // Watcher of Topics. Detecte if new or destroy topics.
        this.topicWatcher = new TopicWatcher(this);

    }

    public void start() {
        this.nodeWatcher.start();
        this.topicWatcher.start();
    }

    public void stop() {
        this.nodeWatcher.stop();
        this.topicWatcher.stop();

        this.longTimeMemory.close();
        this.shortTimeMemory.close();

        this.rosManager.clearTopics();
    }

    public boolean newSensorDetected(String topic, List<String> messageTypes) {
        return this.rosManager.registerTopic(topic, messageTypes);
    }

    public void lostSensor(String topic) {
        this.rosManager.unregisterTopic(topic);
    }

    public <T extends Message> void keep(final MessageInfoBase<T> messageInfo, final T message) throws BadInfoException {
        this.longTimeMemory.insert(
                messageInfo.getMessageDate(),
                messageInfo.getMeasurement(),
                messageInfo.getMessageTags(message),
                messageInfo.getMessageFields(message));

    }

    public void refreshNodes() {
        // Get from ROS.
        List<String> detectedNodes = this.rosManager.getAvailableNodeNames();

        // Update Short-Time Memory.
        this.shortTimeMemory.update(detectedNodes);
        this.longTimeMemory.update(detectedNodes);
    }

    public void refreshTopics() {
        // Get from ROS.
        Map<String, List<String>> topicsTypes = this.rosManager.getAvailabaleTopicNamesAndTypes();

        // Update Short-Time Memory.
        this.shortTimeMemory.update(topicsTypes);
    }
}
