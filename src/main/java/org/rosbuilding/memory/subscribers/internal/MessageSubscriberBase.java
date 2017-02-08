/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.subscribers.internal;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.ros2.rcljava.internal.message.Message;
import org.rosbuilding.common.StateDataComparator;

import com.google.common.base.Strings;

/**
*
* @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
* @author Mickael Gaillard <mick.gaillard@gmail.com>
*/
public abstract class MessageSubscriberBase<T extends Message> {

    public static final String TAG_WORLD = "world";
    public static final String TAG_ZONE = "zone";
    public static final String TAG_NODE = "node";
    public static final String SEPARATOR = "/";
    public static final String STATEDATA = SEPARATOR + "statedata";

    private final String topic;
    private final Class<T> messageClass;
    private final String measurement;
    private final StateDataComparator<T> comparator;

    private T lastMessage;

    protected MessageSubscriberBase(
            String topic,
            Class<T> messageClass,
            String measurement) {
        this(topic, messageClass, measurement, null);
    }

    protected MessageSubscriberBase(
            String topic,
            Class<T> messageClass,
            String measurement,
            StateDataComparator<T> comparator) {
        this.topic = topic;
        this.messageClass = messageClass;
        this.measurement = measurement;
        this.comparator = comparator;
    }

    public final String getTopic() {
        return this.topic;
    }

    public final Class<T> getType() {
        return this.messageClass;
    }

    public final String getMeasurement() {
        return this.measurement;
    }

    public final boolean mustInsert(T message) {
        boolean result = false;

        if (this.comparator == null
                || this.lastMessage == null || !this.comparator.isEquals(this.lastMessage, message)) {
            this.lastMessage = message;
            result = true;
        }

        return result;
    }

    public abstract DateTime getMessageDate(T message);
    public abstract Map<String, Object> getMessageFields(T message);

    public Map<String, String> getMessageTags(T message) throws BadMessageException {
        Map<String, String> result = new HashMap<>();
        StringBuilder zone  = new StringBuilder();
        String world = "";
        String node  = "";

        // /home/simulator/light1/statedata
        String topic = this.getTopic().replace(STATEDATA, "");

        String[] splitTopic = topic.substring(1, topic.length()).split(SEPARATOR);
        int splitTopicCount = splitTopic.length -1;

        for (int i = splitTopicCount; i >= 0; i--) {
            if (i == splitTopicCount) {
                node = splitTopic[i];
            }
            if (i == 0 && splitTopicCount > 0) {
                world = splitTopic[i];
            }
            if (i > 0 && i < splitTopicCount) {
                if (zone.length() > 0) {
                    zone.insert(0, splitTopic[i] + SEPARATOR);
                } else {
                    zone.insert(0, splitTopic[i]);
                }
            }
        }

        if (Strings.isNullOrEmpty(node)) {
            throw new BadMessageException();
        }

        result.put(TAG_WORLD, world);
        result.put(TAG_ZONE,  zone.toString());
        result.put(TAG_NODE,  node);

        return result;
    }
}
