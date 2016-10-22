package org.rosbuilding.memory.database.internal;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.ros2.rcljava.internal.message.Message;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.topic.Consumer;
import org.ros2.rcljava.node.topic.Subscription;
import org.rosbuilding.memory.database.internal.subscribers.BotSubscriber;
import org.rosbuilding.memory.database.internal.subscribers.MediaSubscriber;
import org.rosbuilding.memory.database.internal.subscribers.StateDataSubscriber;
import org.rosbuilding.memory.database.internal.subscribers.TemperatureSubscriber;

import smarthome_comm_msgs.msg.Command;
import smarthome_heater_msgs.msg.SensorTemperatureStateData;
import smarthome_media_msgs.msg.StateData;

public class StateDataWatcher {
    private final InfluxDb influx;
    private final Node node;
    private final Map<String, StateDataSubscriber<? extends Message>> stateDataSubscribers;
    private final Map<String, Subscription<? extends Message>> subscribers;

    public StateDataWatcher(Node node, InfluxDb influx) {
        this.stateDataSubscribers = new HashMap<>();
        this.subscribers = new HashMap<>();
        this.node = node;
        this.influx = influx;
    }

    public boolean addTopic(String topic, String messageType) {
        boolean result = false;
        StateDataSubscriber<? extends Message> stateDataSubscriber = null;

        if (isMessageType(messageType, Command.class)) {
            stateDataSubscriber = new BotSubscriber(topic);
        } else if (isMessageType(messageType, StateData.class)) {
            stateDataSubscriber = new MediaSubscriber(topic);
        } else if (isMessageType(messageType, SensorTemperatureStateData.class)) {
            stateDataSubscriber = new TemperatureSubscriber(topic);
        }

        if (stateDataSubscriber != null) {
            result = this.addStateData(stateDataSubscriber);
        }

        return result;
    }

    public void removeAllTopics() {
        for (StateDataSubscriber<? extends Message> stateDataSubscriber : this.stateDataSubscribers.values()) {
            this.removeTopic(stateDataSubscriber.getTopic());
        }
    }

    public void removeTopic(String topic) {
        this.removeStateDate(this.stateDataSubscribers.get(topic));
    }

    private boolean addStateData(StateDataSubscriber<? extends Message> stateDataSubscriber) {
        boolean result = false;

        if (stateDataSubscriber != null && !this.stateDataSubscribers.containsKey(stateDataSubscriber.getTopic())) {
            this.node.getLog().debug(String.format("Create subscriber for %s", stateDataSubscriber.getTopic()));
            this.stateDataSubscribers.put(stateDataSubscriber.getTopic(), stateDataSubscriber);
            this.createSubscriber(stateDataSubscriber);
            result = true;
        }

        return result;
    }

    private void removeStateDate(StateDataSubscriber<? extends Message> stateDataSubscriber) {
        if (stateDataSubscriber != null) {
            this.stateDataSubscribers.remove(stateDataSubscriber.getTopic());
            Subscription<? extends Message> subscription = this.subscribers.remove(stateDataSubscriber.getTopic());

            if (subscription != null) {
                subscription.dispose();
            }
        }
    }

    private <T extends Message> void createSubscriber(final StateDataSubscriber<T> stateDataSubscriber) {
        this.node.getLog().info("init subscriber on : " + stateDataSubscriber.getTopic());

        Subscription<T> subscription = this.node.<T>createSubscription(
                stateDataSubscriber.getType(),
                stateDataSubscriber.getTopic(),
                new Consumer<T>() {
                    @Override
                    public void accept(T message) {
                        node.getLog().info("receive message in subscriber");

                        if (stateDataSubscriber.mustInsert(message)) {
                            StateDataWatcher.this.insert(
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
