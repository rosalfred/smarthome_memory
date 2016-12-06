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

/**
*
* @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
* @author Mickael Gaillard <mick.gaillard@gmail.com>
*/
public abstract class MessageSubscriberBase<T extends Message> {
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

    public Map<String, String> getMessageTags(T message) {
        Map<String, String> result = new HashMap<>();

        result.put("node", this.getTopic());

        return result;
    }
}
