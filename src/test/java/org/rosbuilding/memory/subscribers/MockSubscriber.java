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

import org.ros2.rcljava.internal.message.Message;
import org.rosbuilding.memory.concept.internal.MessageInfoBase;

public class MockSubscriber extends MessageInfoBase<Message>{

    protected MockSubscriber(String topic) {
        super(topic, Message.class, "");
    }

    @Override
    public Map<String, Object> getMessageFields(Message message) {
        Map<String, Object> result = new HashMap<>();

        return result;
    }

}
