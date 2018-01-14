/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.concept;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import org.rosbuilding.memory.concept.internal.MessageInfoInfluxBase;
import org.rosbuilding.memory.concept.internal.RosColumn;

import smarthome_heater_msgs.msg.SensorTemperatureStateData;
import smarthome_heater_msgs.msg.HeatingStateData;

/**
 *
 * @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * TODO switch to
 */
public class HeaterInfo extends MessageInfoInfluxBase<SensorTemperatureStateData> {
    private static final String MEASUREMENT         = "node_thermal";
    private static final String TAG_TOPIC           = "sys.topic";
    private static final String FIELD_PROPORTIONAL  = "pid.p";
    private static final String FIELD_INTEGRAL      = "pid.i";
    private static final String FIELD_DERIVATIVE    = "pid.d";
    private static final String FIELD_AMBIANT_TEMPERATIVE = "ambiant.temperature";
    private static final String FIELD_AMBIANT_VARIANCE    = "ambiant.temperature";
    private static final String FIELD_OBJECT_TEMPERATIVE  = "object.temperature";
    private static final String FIELD_OBJECT_VARIANCE     = "object.temperature";
    private static final String FIELD_TARGET_TEMPERATIVE  = "target.temperature";
    private static final String FIELD_TARGET_VARIANCE     = "target.temperature";

    public HeaterInfo(String topic) {
        super(topic, SensorTemperatureStateData.class, Heating.class);
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

    @Measurement(name=MEASUREMENT)
    public static class Heating {

        @Column(name = TIME)
        protected Instant time;

        @Column(name = TAG_TOPIC, tag = true)
        protected String topic;

        @RosColumn
        @Column(name = FIELD_PROPORTIONAL)
        protected Double proportional;

        @RosColumn
        @Column(name = FIELD_INTEGRAL)
        protected Double integral;

        @RosColumn
        @Column(name = FIELD_DERIVATIVE)
        protected Double derivative;

        @RosColumn(name = "temperatureReal.temperature")
        @Column(name = FIELD_AMBIANT_TEMPERATIVE)
        protected Double ambiantTemperature;

        @RosColumn(name = "temperatureAmbiant.variance")
        @Column(name = FIELD_AMBIANT_VARIANCE)
        protected Double ambiantVariance;
    }
}
