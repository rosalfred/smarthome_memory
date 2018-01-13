/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.simulator;

import java.util.concurrent.ThreadLocalRandom;

import org.ros2.rcljava.node.Node;
import org.rosbuilding.memory.simulator.base.AbstractSimulator;

import smarthome_heater_msgs.msg.SensorTemperatureStateData;

public class HeaterSimulator extends AbstractSimulator<SensorTemperatureStateData> {
    private static final String TOPIC = "/home/simulator/heater1/statedata";
    private static final int RATE = 1;
    private static final int STATE = 2;
    private static final double VARIANCE = 3.0d; // For air

    private static final double TMP_MIN = 10.0d;
    private static final double TMP_MAX = 10.9d;
    private static final double TMP_VAR = 1.0d;

    public HeaterSimulator(Node node, ThreadGroup group) {
        super(SensorTemperatureStateData.class, node, group, RATE, TOPIC);
    }

    @Override
    protected SensorTemperatureStateData makeMessage() {
        SensorTemperatureStateData msg = new SensorTemperatureStateData();
        msg.setState(STATE);

        this.hydrateHeader(msg.getHeader());
        double tmp = ThreadLocalRandom.current().nextDouble(TMP_MIN, TMP_MAX);
        double var = ThreadLocalRandom.current().nextDouble(-TMP_VAR, TMP_VAR);
        msg.getTemperatureAmbiant().setTemperature(tmp);
        msg.getTemperatureAmbiant().setVariance(VARIANCE);
        msg.getTemperatureObject().setTemperature(tmp+var);
        msg.getTemperatureObject().setVariance(VARIANCE);

        return msg;
    }
}
