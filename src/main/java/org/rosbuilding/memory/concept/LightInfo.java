/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.concept;

import java.util.HashMap;
import java.util.Map;

import org.rosbuilding.memory.concept.internal.MessageInfoBase;

import geometry_msgs.msg.Pose;
import sensor_msgs.msg.Illuminance;
import smarthome_light_msgs.msg.Hsb;
import smarthome_light_msgs.msg.StateData;

/**
 *
 * @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class LightInfo extends MessageInfoBase<StateData> {

    public LightInfo(String topic) {
        super(topic, StateData.class, "node_luminous");
    }

    @Override
    public Map<String, Object> getMessageFields(StateData message) {
        Map<String, Object> result = new HashMap<>();

        result.put("state", message.getState().getVal());

        Hsb hsb = message.getHsb();
        result.put("hsb.brightness",    hsb.getBrightness());
        result.put("hsb.hue",           hsb.getHue());
        result.put("hsb.saturation",    hsb.getSaturation());
        hsb = null;

        Illuminance illuminance = message.getIlluminance();
        result.put("illuminance.value",     illuminance.getIlluminance());
        result.put("illuminance.variance",  illuminance.getVariance());
        illuminance = null;

        Pose pose = message.getPose();
        result.put("point.position.x",      pose.getPosition().getX());
        result.put("point.position.y",      pose.getPosition().getY());
        result.put("point.position.z",      pose.getPosition().getZ());
        result.put("point.orientation.x",   pose.getOrientation().getX());
        result.put("point.orientation.y",   pose.getOrientation().getY());
        result.put("point.orientation.z",   pose.getOrientation().getZ());
        result.put("point.orientation.w",   pose.getOrientation().getW());
        pose = null;

        return result;
    }

}
