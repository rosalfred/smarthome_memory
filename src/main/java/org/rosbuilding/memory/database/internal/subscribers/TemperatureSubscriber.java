package org.rosbuilding.memory.database.internal.subscribers;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import smarthome_heater_msgs.msg.SensorTemperatureStateData;

public class TemperatureSubscriber extends StateDataSubscriber<SensorTemperatureStateData> {

    public TemperatureSubscriber(String topic) {
        super(topic, SensorTemperatureStateData.class, "temperature");
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

        return result;
    }

}
