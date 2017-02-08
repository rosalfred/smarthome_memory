/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.simulator.base;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.ros2.rcljava.internal.message.Message;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.topic.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import smarthome_msgs.msg.DeviceInfo;
import std_msgs.msg.Header;

public abstract class AbstractSimulator<M extends Message> implements Runnable {

    private final Logger log;
    private final int rate = 1;
    private final Thread thread;
    private final Node node;
    private final Publisher<M> publisher;

    private boolean isRunning = false;

    public AbstractSimulator(Class<M> cls, Node node, ThreadGroup group, int rate, String topicName) {
        this.log = LoggerFactory.getLogger(this.getClass());
        this.thread = new Thread(group, this);
        this.thread.setName(this.getClass().getSimpleName());

        this.node = node;
        this.publisher = this.node.<M>createPublisher(cls, topicName);
    }

    public void start() {
        this.isRunning = true;
        this.thread.start();
    }

    @Override
    public void run() {
        final Gson gson = new Gson();
        while(this.isRunning) {
            M msg = this.makeMessage();

//            this.log.info("Publish simulator...");
            this.log.info(gson.toJson(msg));
            this.publisher.publish(msg);
            try {
                TimeUnit.MILLISECONDS.sleep(1000/rate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract M makeMessage();

    protected void hydrateHeader(Header header) {
        header.setFrameId("frame01");
        header.getStamp().setSec((int)new Date().getTime());
    }

    protected void hydrateDeviceInfo(DeviceInfo info) {
        info.setAddress("0x0000ff");
        info.setHwVersion("1.0");
        info.setManufacturer("TI");
        info.setModel("CC3200");
        info.setSerialNumber("0000000000000000");
        info.setSwVersion("1.0.0");
    }
}
