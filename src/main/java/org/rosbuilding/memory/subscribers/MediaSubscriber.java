package org.rosbuilding.memory.subscribers;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.rosbuilding.common.media.MediaStateDataComparator;
import org.rosbuilding.memory.subscribers.internal.MessageSubscriberBase;

import smarthome_media_msgs.msg.StateData;

public class MediaSubscriber extends MessageSubscriberBase<StateData> {
    public MediaSubscriber(String topic) {
        super(topic, StateData.class, "node_media", new MediaStateDataComparator());
    }

    @Override
    public DateTime getMessageDate(StateData message) {
        //return new DateTime(message.getHeader().getStamp().getSec());
        return DateTime.now();
    }

    @Override
    public Map<String, Object> getMessageFields(StateData message) {
        Map<String, Object> result = new HashMap<>();

        result.put("state", message.getState());

        result.put("monitor.height", message.getMonitor().getHeight());
        result.put("monitor.width", message.getMonitor().getWigth());
        result.put("monitor.source", message.getMonitor().getSource());

        result.put("player.canseek", message.getPlayer().getCanseek());
        result.put("player.file", message.getPlayer().getFile());
        result.put("player.mediaid", message.getPlayer().getMediaid());
        result.put("player.mediatype.value", message.getPlayer().getMediatype().getValue());
        result.put("player.speed", message.getPlayer().getSpeed());
        result.put("player.stamp", message.getPlayer().getStamp().getSec());
        result.put("player.state", message.getPlayer().getState());
        result.put("player.subtitleenabled", message.getPlayer().getSubtitleenabled());
        result.put("player.thumbnail", message.getPlayer().getThumbnail());
        result.put("player.title", message.getPlayer().getTitle());
        result.put("player.totaltime", message.getPlayer().getTotaltime().getSec());

        result.put("speaker.channel", message.getSpeaker().getChannel());
        result.put("speaker.level", message.getSpeaker().getLevel());
        result.put("speaker.muted", message.getSpeaker().getMuted());
        result.put("speaker.output", message.getSpeaker().getOutput());
        result.put("speaker.source", message.getSpeaker().getSource());

        return result;
    }

}
