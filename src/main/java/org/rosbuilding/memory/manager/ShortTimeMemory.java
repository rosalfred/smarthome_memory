/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.rosbuilding.memory.MemoryNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

/**
 * Manager of Topics subscriptions.
 *
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class ShortTimeMemory {

    private static final Logger logger = LoggerFactory.getLogger(ShortTimeMemory.class);

    // Cache instance of Nodes and Topics.
    private final List<String> cachedNodes = new ArrayList<String>();
    private final Map<String, List<String>> cachedTopics = new HashMap<String, List<String>>();

    private final MemoryManager memory;

    public ShortTimeMemory(MemoryManager memory) {
        this.memory = memory;
    }

    /**
     * New topic created.
     * @param topic
     * @param messageType
     * @return
     */
    public boolean add(String topic, List<String> messageTypes) {
        logger.info("Add topic : " + topic);

        this.cachedTopics.put(topic, messageTypes);
        return this.memory.newSensorDetected(topic, messageTypes);

    }

    /**
     * Remove topic removed.
     * @param topic
     */
    public synchronized void remove(String topic) {
        logger.info("Remove topic : " + topic);

        this.cachedTopics.remove(topic);
        this.memory.lostSensor(topic);
    }

    public void close() {
        this.cachedTopics.clear();
        this.cachedNodes.clear();
    }

    /**
     * @return the topicCaches
     */
    public synchronized final Map<String, List<String>> getTopicCaches() {
        return new HashMap<String, List<String>>(cachedTopics);
    }

    public void update(List<String> detectedNodes) {
        // Remove himself node.
        detectedNodes.remove(MemoryNode.NAME);

        // Reset list of node.
        this.cachedNodes.clear();
        this.cachedNodes.addAll(detectedNodes);
    }

    public void update(Map<String, List<String>> topicsTypes) {
        MapDifference<String, List<String>> diff = Maps.difference(this.getTopicCaches(), topicsTypes);
        Map<String, List<String>> removed = diff.entriesOnlyOnLeft();
        Map<String, List<String>> added   = diff.entriesOnlyOnRight();

        // TODO Remove from detected node, not only from topic (because is subscribed)
        // Remove removed topic.
        for (Entry<String, List<String>> topic : removed.entrySet()) {
            this.remove(topic.getKey());
        }

        // Add added topic.
        for (Entry<String, List<String>> topic : added.entrySet()) {
            this.add(topic.getKey(), topic.getValue());
        }
    }

}
