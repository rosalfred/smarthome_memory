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

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import org.rosbuilding.memory.concept.internal.MessageInfoInfluxBase;
import org.rosbuilding.memory.concept.internal.RosColumn;

/**
*
* @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
* @author Mickael Gaillard <mick.gaillard@gmail.com>
*
* TODO switch to
*/
public class ThermalInfo extends MessageInfoInfluxBase<sensor_msgs.msg.Temperature> {
    private static final String MEASUREMENT         = "node_thermal";
    private static final String TAG_TOPIC           = "sys.topic";
    private static final String FIELD_TEMPERATURE   = "ambiant.temperature";
    private static final String FIELD_VARIANCE      = "ambiant.variance";

    public ThermalInfo(String topic) {
        super(topic, sensor_msgs.msg.Temperature.class, Temperature.class);
    }

    @Measurement(name=MEASUREMENT)
    public static class Temperature {

        @Column(name = TIME)
        protected Instant time;

        @Column(name = TAG_TOPIC, tag = true)
        protected String topic;

        @RosColumn
        @Column(name = FIELD_TEMPERATURE)
        protected Double temperature;

        @RosColumn
        @Column(name = FIELD_VARIANCE)
        protected Double variance;
    }

}
