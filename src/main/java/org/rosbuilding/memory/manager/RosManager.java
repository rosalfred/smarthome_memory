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
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

import org.ros2.rcljava.internal.message.Message;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.topic.Subscription;
import org.ros2.rcljava.node.topic.SubscriptionCallback;
import org.rosbuilding.memory.concept.CommInfo;
import org.rosbuilding.memory.concept.HeaterInfo;
import org.rosbuilding.memory.concept.LightInfo;
import org.rosbuilding.memory.concept.MediaInfo;
import org.rosbuilding.memory.concept.ThermalInfo;
import org.rosbuilding.memory.concept.internal.BadInfoException;
import org.rosbuilding.memory.concept.internal.MessageInfoBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RosManager {

    private static final Logger logger = LoggerFactory.getLogger(RosManager.class);

    private final Node node;
    private final MemoryManager memory;

    private final Map<String, MessageInfoBase<? extends Message>> stateDataSubscribers = new HashMap<>();
    private final Map<String, Subscription<? extends Message>> subscribers = new HashMap<>();

    private final static ConcurrentHashMap<Class<? extends Message>, Class<?>> SUBSCRIBER_MAPPING = new ConcurrentHashMap<Class<? extends Message>, Class<?>>() {
        /** Serial Id */
        private static final long serialVersionUID = 1L;
        {
            put(smarthome_comm_msgs.msg.Command.class,                          CommInfo.class);
            put(smarthome_media_msgs.msg.StateData.class,                       MediaInfo.class);
            put(smarthome_heater_msgs.msg.SensorTemperatureStateData.class,     HeaterInfo.class);
            put(smarthome_light_msgs.msg.StateData.class,                       LightInfo.class);
            put(sensor_msgs.msg.Temperature.class,                              ThermalInfo.class);
        }
    };

    public RosManager(final MemoryManager memory, final Node node) {
        this.memory = memory;
        this.node = node;
    }

    public List<String> getAvailableNodeNames() {
        return this.node.getNodeNames();
    }

    public HashMap<String, List<String>> getAvailabaleTopicNamesAndTypes() {
        HashMap<String, List<String>> result = this.node.getTopicNamesAndTypes();
        List<String> noUses = new ArrayList<String>();

        int count = 0;
        for (String topic : result.keySet()) {
            count = this.node.countPublishers(topic);
            if (count == 0) {
                noUses.add(topic);
            }
        }

        for(String topic : noUses) {
            result.remove(topic);
        }

        return result;
    }

    /**
     * New topic created.
     * @param topic
     * @param messageType
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean registerTopic(String topic, List<String> messageTypes) {
        boolean result = false;
        MessageInfoBase<? extends Message> messageInfo = null;

        for (Entry<Class<? extends Message>, Class<?>> subscriber : SUBSCRIBER_MAPPING.entrySet()) {
            if (RosManager.isMessageType(messageTypes, subscriber.getKey())) {
                try {
                    Object obj = subscriber.getValue().getConstructor(String.class).newInstance(topic);
                    if (obj instanceof MessageInfoBase<?>) {
                        messageInfo = (MessageInfoBase<? extends Message>) obj;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        if (messageInfo != null) {
            logger.info("Add topic : " + topic);
            result = this.addStateData(messageInfo);
        }

        return result;
    }

    public void unregisterTopic(String topic) {
        this.removeStateDate(this.stateDataSubscribers.get(topic));
    }

    private boolean addStateData(MessageInfoBase<? extends Message> messageInfo) {
        boolean result = false;

        if (messageInfo != null && !this.stateDataSubscribers.containsKey(messageInfo.getTopic())) {
            this.node.getLog().debug(String.format("Create subscriber for %s", messageInfo.getTopic()));
            this.stateDataSubscribers.put(messageInfo.getTopic(), messageInfo);
            this.createSubscriber(messageInfo);
            result = true;
        }

        return result;
    }

    private void removeStateDate(MessageInfoBase<? extends Message> messageInfo) {
        if (messageInfo != null) {
            this.stateDataSubscribers.remove(messageInfo.getTopic());
            Subscription<? extends Message> subscription = this.subscribers.remove(messageInfo.getTopic());

            if (subscription != null) {
                subscription.dispose();
            }
        }
    }

    /**
     * Clean all topics subscriber
     */
    public synchronized void clearTopics() {
        for (MessageInfoBase<? extends Message> stateDataSubscriber : this.stateDataSubscribers.values()) {
            this.unregisterTopic(stateDataSubscriber.getTopic());
        }
    }

    private <T extends Message> void createSubscriber(final MessageInfoBase<T> stateDataSubscriber) {
        this.node.getLog().info("init subscriber on : " + stateDataSubscriber.getTopic());

        Subscription<T> subscription = this.node.<T>createSubscription(
                stateDataSubscriber.getType(),
                stateDataSubscriber.getTopic(),
                new SubscriptionCallback<T>() {
                    @Override
                    public void dispatch(T message) {
                        logger.debug("Receive message in subscriber");

                        if (stateDataSubscriber.mustInsert(message)) {
                            try {
                                RosManager.this.memory.keep(stateDataSubscriber, message);
                            } catch (BadInfoException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        this.subscribers.put(stateDataSubscriber.getTopic(), subscription);
    }

    public static boolean isMessageType(List<String> types, Class<? extends Message> messageClass) {
        boolean result = false;

        if (types != null && messageClass != null) {
            for (String type : types) {
                if (type.equals(messageClass.getName().replace(".msg.", "/"))) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
}
