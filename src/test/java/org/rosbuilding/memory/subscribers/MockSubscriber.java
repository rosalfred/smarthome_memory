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
import org.ros2.rcljava.internal.message.Message;
import org.rosbuilding.memory.subscribers.internal.MessageSubscriberBase;

public class MockSubscriber extends MessageSubscriberBase<Message>{

    protected MockSubscriber(String topic) {
        super(topic, Message.class, "");
    }

    @Override
    public DateTime getMessageDate(Message message) {
        return DateTime.now();
    }

    @Override
    public Map<String, Object> getMessageFields(Message message) {
        Map<String, Object> result = new HashMap<>();

        return result;
    }

}
