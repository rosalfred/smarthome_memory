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

import sensor_msgs.msg.Temperature;

/**
*
* @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
* @author Mickael Gaillard <mick.gaillard@gmail.com>
*
* TODO switch to
*/
public class ThermalInfo extends MessageInfoBase<Temperature> {

    public ThermalInfo(String topic) {
        super(topic, Temperature.class, "node_thermal");
    }

    @Override
    public Map<String, Object> getMessageFields(Temperature message) {
        Map<String, Object> result = new HashMap<>();

        result.put("ambiant.temperature", message.getTemperature());
        result.put("ambiant.variance", message.getVariance());

        return result;
    }

}
