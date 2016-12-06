package org.rosbuilding.memory.subscribers;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.rosbuilding.memory.subscribers.internal.MessageSubscriberBase;

import smarthome_heater_msgs.msg.SensorTemperatureStateData;

public class HeaterSubscriber extends MessageSubscriberBase<SensorTemperatureStateData> {

    public HeaterSubscriber(String topic) {
        super(topic, SensorTemperatureStateData.class, "node_thermal");
    }

    @Override
    public DateTime getMessageDate(SensorTemperatureStateData message) {
        //return new DateTime(message.getHeader().getStamp().getSec());
        return DateTime.now();
    }

    @Override
    public Map<String, Object> getMessageFields(SensorTemperatureStateData message) {
        Map<String, Object> result = new HashMap<>();

        result.put("state", message.getState());

        result.put("ambiant.temperature", message.getTemperatureAmbiant().getTemperature());
        result.put("ambiant.variance", message.getTemperatureAmbiant().getVariance());

        result.put("object.temperature", message.getTemperatureObject().getTemperature());
        result.put("object.variance", message.getTemperatureObject().getVariance());

        return result;
    }

}
