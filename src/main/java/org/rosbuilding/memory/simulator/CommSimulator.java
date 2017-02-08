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
import org.rosbuilding.memory.simulator.base.AbstractSimulator;

import smarthome_comm_msgs.msg.Command;
import smarthome_comm_msgs.msg.Context;

public class CommSimulator extends AbstractSimulator<Command> {
    private static final String TOPIC = "/speech";
    private static final int RATE = 10;

    public CommSimulator(Node node, ThreadGroup group) {
        super(Command.class, node, group, RATE, TOPIC);
    }

    @Override
    protected Command makeMessage() {
        Command msg = new Command();

        msg.setAction("SAY");
        msg.setSubject("Comment vas-tu ?");

        Context ctx = msg.getContext();
        ctx.setWhere("/");
        ctx.setWho("Simulator");

        return msg;
    }



}
