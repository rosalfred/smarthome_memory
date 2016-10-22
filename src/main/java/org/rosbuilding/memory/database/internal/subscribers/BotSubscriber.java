package org.rosbuilding.memory.database.internal.subscribers;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import smarthome_comm_msgs.msg.Command;

public class BotSubscriber extends StateDataSubscriber<Command> {

    public BotSubscriber(String topic) {
        super(topic, Command.class, "bot");
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
