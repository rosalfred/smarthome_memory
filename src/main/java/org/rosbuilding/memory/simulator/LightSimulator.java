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

import geometry_msgs.msg.Point;
import sensor_msgs.msg.Illuminance;
import smarthome_light_msgs.msg.Hsb;
import smarthome_light_msgs.msg.StateData;

public class LightSimulator extends AbstractSimulator<StateData> {

    public LightSimulator(Node node, ThreadGroup group) {
        super(StateData.class, node, group, 1, "/home/simulator/light1/statedata");
    }

    @Override
    protected StateData makeMessage() {
        StateData msg = new StateData();

        this.hydrateDeviceInfo(msg.getDescriptor());
        this.hydrateHeader(msg.getHeader());

        Hsb hsb = msg.getHsb();
        hsb.setBrightness(1);
        hsb.setHue(1);
        hsb.setSaturation(1);

        Illuminance illuminance = msg.getIlluminance();
        illuminance.setHeader(msg.getHeader());
        illuminance.setIlluminance(1.0);
        illuminance.setVariance(1.0);

        Point point = msg.getPose().getPosition();
        point.setX(1.0);
        point.setY(1.0);
        point.setZ(1.0);

//        msg.getState().setVal(-1);

        return msg;
    }
}
