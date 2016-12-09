/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.simulator;

import org.ros2.rcljava.node.Node;

import smarthome_heater_msgs.msg.SensorTemperatureStateData;

public class HeaterSimulator extends AbstractSimulator<SensorTemperatureStateData> {

    public HeaterSimulator(Node node, ThreadGroup group) {
        super(SensorTemperatureStateData.class, node, group, 1, "/home/simulator/heater1/statedata");
    }

    @Override
    protected SensorTemperatureStateData makeMessage() {
        SensorTemperatureStateData msg = new SensorTemperatureStateData();
        msg.setState(2);

        this.hydrateHeader(msg.getHeader());
        msg.getTemperatureAmbiant().setTemperature(12.0);
        msg.getTemperatureObject().setTemperature(13.0);

        return msg;
    }
}
