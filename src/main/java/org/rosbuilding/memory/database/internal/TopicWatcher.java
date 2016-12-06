/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.database.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ros2.rcljava.node.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

/**
 * Topics watcher.<p>
 * Detect new/destroy Topics.
 */
public class TopicWatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TopicWatcher.class);

    /** Main node. */
    private final Node node;

    /** Topics Manager. */
    private final TopicManager topicManager;

    /** List of Topics and types. */
    private final Map<String, String> topicsTypes = new HashMap<String, String>();

    // Thread component.
    private volatile Thread blinker;
    boolean threadSuspended;

    /**
     * Constuctor of TopicWatcher.
     * @param node
     * @param topicManager
     */
    public TopicWatcher(final Node node, final TopicManager topicManager) {
        this.node = node;
        this.topicManager = topicManager;
    }

    /** Start the watcher */
    public void start() {
        logger.debug("Start watcher...");
        this.blinker = new Thread(this);
        this.blinker.start();
    }

    /** Stop the watcher */
    public synchronized void stop() {
        logger.debug("Stop watcher...");
        this.blinker = null;
        this.notify();
    }

    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();

        while (blinker == thisThread) {
            try {
                Thread.sleep(5000);

                synchronized(this) {
                    while (threadSuspended && blinker==thisThread)
                        wait();
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            TopicWatcher.this.checkTopics();
        }
    }

    private void checkTopics() {
        logger.debug("Check Topics available...");
//        List<String> nodes = this.node.getNodes();
        HashMap<String, String> topicsTypes = this.node.getTopicNamesAndTypes();

        MapDifference<String, String> diff = Maps.difference(this.topicsTypes, topicsTypes);
        Map<String, String> removed = diff.entriesOnlyOnLeft();
        Map<String, String> added   = diff.entriesOnlyOnRight();

        // TODO Remove from detected node, not only from topic (because is subscribed)
        // Remove removed topic.
        for (Entry<String, String> topic : removed.entrySet()) {
            this.topicsTypes.remove(topic.getKey());
            this.topicManager.remove(topic.getKey());
        }

        // Add added topic.
        for (Entry<String, String> topic : added.entrySet()) {
            this.topicsTypes.put(topic.getKey(), topic.getValue());
            this.topicManager.add(topic.getKey(), topic.getValue());
        }
    }
}
