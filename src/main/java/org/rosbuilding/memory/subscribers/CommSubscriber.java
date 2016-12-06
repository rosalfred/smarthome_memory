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

import smarthome_comm_msgs.msg.Command;

/**
*
* @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
* @author Mickael Gaillard <mick.gaillard@gmail.com>
*/
public class CommSubscriber extends MessageSubscriberBase<Command> {

    public CommSubscriber(String topic) {
        super(topic, Command.class, "node_comm");
    }

    @Override
    public DateTime getMessageDate(Command message) {
        return DateTime.now();
    }

    @Override
    public Map<String, Object> getMessageFields(Command message) {
        Map<String, Object> result = new HashMap<>();

        result.put("subject", message.getSubject());
        result.put("context.where", message.getContext().getWhere());
        result.put("context.who", message.getContext().getWho());

        return result;
    }

}
