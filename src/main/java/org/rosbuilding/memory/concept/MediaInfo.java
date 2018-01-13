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

import org.rosbuilding.common.media.MediaStateDataComparator;
import org.rosbuilding.memory.concept.internal.MessageInfoBase;

import smarthome_media_msgs.msg.StateData;

/**
 *
 * @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class MediaInfo extends MessageInfoBase<StateData> {
    public MediaInfo(String topic) {
        super(topic, StateData.class, "node_media", new MediaStateDataComparator());
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
