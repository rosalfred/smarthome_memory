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

import smarthome_media_msgs.msg.MonitorInfo;
import smarthome_media_msgs.msg.PlayerInfo;
import smarthome_media_msgs.msg.SpeakerInfo;
import smarthome_media_msgs.msg.StateData;

public class MediaSimulator extends AbstractSimulator<StateData> {

    public MediaSimulator(Node node, ThreadGroup group) {
        super(StateData.class, node, group, 1, "/home/simulator/kodi/statedata");
    }

    @Override
    protected StateData makeMessage() {
        StateData msg = new StateData();
        msg.setState(2);

        this.hydrateHeader(msg.getHeader());

        MonitorInfo monitor = msg.getMonitor();
        monitor.setHeight(1024);
        monitor.setWigth(640);
        monitor.setSource("tv");

        PlayerInfo player = msg.getPlayer();
        player.setFile("");

        SpeakerInfo speaker = msg.getSpeaker();
        speaker.setLevel(90);


        return msg;
    }
}
