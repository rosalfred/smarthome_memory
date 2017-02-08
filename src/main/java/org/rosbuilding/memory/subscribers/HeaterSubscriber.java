/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.subscribers;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.rosbuilding.memory.subscribers.internal.MessageSubscriberBase;

import smarthome_heater_msgs.msg.SensorTemperatureStateData;
import smarthome_heater_msgs.msg.HeatingStateData;

/**
*
* @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
* @author Mickael Gaillard <mick.gaillard@gmail.com>
*
* TODO switch to
*/
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

//    @Override
    public Map<String, Object> getMessageFields(HeatingStateData message) {
        Map<String, Object> result = new HashMap<>();

        result.put("pid.p", message.getProportional());
        result.put("pid.i", message.getIntegral());
        result.put("pid.d", message.getDerivative());

        result.put("ambiant.temperature", message.getTemperatureReal().getTemperature());
        result.put("ambiant.variance", message.getTemperatureReal().getVariance());

        result.put("object.temperature", message.getTemperatureReal().getTemperature());
        result.put("object.variance", message.getTemperatureReal().getVariance());

        result.put("target.temperature", message.getTemperatureGoal().getTemperature());
        result.put("target.variance", message.getTemperatureGoal().getVariance());

        return result;
    }

}
