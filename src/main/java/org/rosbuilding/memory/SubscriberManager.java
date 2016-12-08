/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;

import org.ros2.rcljava.internal.message.Message;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.topic.SubscriptionCallback;
import org.ros2.rcljava.node.topic.Subscription;

import org.rosbuilding.memory.subscribers.CommSubscriber;
import org.rosbuilding.memory.subscribers.MediaSubscriber;
import org.rosbuilding.memory.subscribers.HeaterSubscriber;
import org.rosbuilding.memory.subscribers.LightSubscriber;
import org.rosbuilding.memory.subscribers.internal.MessageSubscriberBase;
import org.rosbuilding.memory.tsdb.TimeSerieManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager of Topics subscriptions.
 *
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class SubscriberManager {

    private static final Logger logger = LoggerFactory.getLogger(SubscriberManager.class);

    private final TimeSerieManager influx;
    private final Node node;

    private final static ConcurrentHashMap<Class<? extends Message>, Class<?>> SUBSCRIBER_MAPPING = new ConcurrentHashMap<Class<? extends Message>, Class<?>>() {
        /** Serial Id */
        private static final long serialVersionUID = 1L;
        {
            put(smarthome_comm_msgs.msg.Command.class,                          CommSubscriber.class);
            put(smarthome_media_msgs.msg.StateData.class,                       MediaSubscriber.class);
            put(smarthome_heater_msgs.msg.SensorTemperatureStateData.class,     HeaterSubscriber.class);
            put(smarthome_light_msgs.msg.LightingStateData.class,               LightSubscriber.class);
        }
    };

    /** List of Topics and types. */
    private final Map<String, String> topicCaches = new HashMap<String, String>();
    private final Map<String, MessageSubscriberBase<? extends Message>> stateDataSubscribers = new HashMap<>();
    private final Map<String, Subscription<? extends Message>> subscribers = new HashMap<>();

    public SubscriberManager(Node node, TimeSerieManager influx) {
        this.node = node;
        this.influx = influx;
    }

    /**
     * New topic created.
     * @param topic
     * @param messageType
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean add(String topic, String messageType) {
        boolean result = false;
        MessageSubscriberBase<? extends Message> stateDataSubscriber = null;

        this.topicCaches.put(topic, messageType);

        for (Entry<Class<? extends Message>, Class<?>> subscriber : SUBSCRIBER_MAPPING.entrySet()) {
            if (isMessageType(messageType, subscriber.getKey())) {
                try {
                    Object obj = subscriber.getValue().getConstructor(String.class).newInstance(topic);
                    if (obj instanceof MessageSubscriberBase<?>) {
                        stateDataSubscriber = (MessageSubscriberBase<? extends Message>) obj;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        if (stateDataSubscriber != null) {
            logger.info("Add topic : " + topic);
            result = this.addStateData(stateDataSubscriber);
        }

        return result;
    }

    /**
     * Remove topic removed.
     * @param topic
     */
    public synchronized void remove(String topic) {
        logger.info("Remove topic : " + topic);
        this.topicCaches.remove(topic);
        this.removeStateDate(this.stateDataSubscribers.get(topic));
    }

    /**
     * Clean all topics subscriber
     */
    public synchronized void clear() {
        for (MessageSubscriberBase<? extends Message> stateDataSubscriber : this.stateDataSubscribers.values()) {
            this.remove(stateDataSubscriber.getTopic());
        }
    }

    /**
     * @return the topicCaches
     */
    public synchronized final Map<String, String> getTopicCaches() {
        return new HashMap<String, String>(topicCaches);
    }

    private boolean addStateData(MessageSubscriberBase<? extends Message> stateDataSubscriber) {
        boolean result = false;

        if (stateDataSubscriber != null && !this.stateDataSubscribers.containsKey(stateDataSubscriber.getTopic())) {
            this.node.getLog().debug(String.format("Create subscriber for %s", stateDataSubscriber.getTopic()));
            this.stateDataSubscribers.put(stateDataSubscriber.getTopic(), stateDataSubscriber);
            this.createSubscriber(stateDataSubscriber);
            result = true;
        }

        return result;
    }

    private void removeStateDate(MessageSubscriberBase<? extends Message> stateDataSubscriber) {
        if (stateDataSubscriber != null) {
            this.stateDataSubscribers.remove(stateDataSubscriber.getTopic());
            Subscription<? extends Message> subscription = this.subscribers.remove(stateDataSubscriber.getTopic());

            if (subscription != null) {
                subscription.dispose();
            }
        }
    }

    private <T extends Message> void createSubscriber(final MessageSubscriberBase<T> stateDataSubscriber) {
        this.node.getLog().info("init subscriber on : " + stateDataSubscriber.getTopic());

        Subscription<T> subscription = this.node.<T>createSubscription(
                stateDataSubscriber.getType(),
                stateDataSubscriber.getTopic(),
                new SubscriptionCallback<T>() {
                    @Override
                    public void dispatch(T message) {
                        logger.debug("Receive message in subscriber");

                        if (stateDataSubscriber.mustInsert(message)) {
                            SubscriberManager.this.insert(
                                    stateDataSubscriber.getMessageDate(message),
                                    stateDataSubscriber.getMeasurement(),
                                    stateDataSubscriber.getMessageTags(message),
                                    stateDataSubscriber.getMessageFields(message));
                        }
                    }
                }
        );

        this.subscribers.put(stateDataSubscriber.getTopic(), subscription);
    }

    private static boolean isMessageType(String type, Class<? extends Message> messageClass) {
        return type != null && messageClass != null
                && type.equals(messageClass.getName().replace(".msg.", "/"));
    }

    private void insert(DateTime date, String measurement, Map<String, String> tags, Map<String, Object> fields) {
        this.influx.write(date, measurement, tags, fields);
    }
}
