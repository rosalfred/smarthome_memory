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

import smarthome_light_msgs.msg.StateData;

/**
 *
 * @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class ElectricalInfo extends MessageInfoBase<StateData> {

    public ElectricalInfo(String topic) {
        super(topic, StateData.class, "node_electrical");
    }

    @Override
    public Map<String, Object> getMessageFields(StateData message) {
        Map<String, Object> result = new HashMap<>();

//        result.put("state", message.getState());

        return result;
    }

}
