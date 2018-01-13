/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory;

import org.apache.log4j.BasicConfigurator;
import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.Node;

import org.rosbuilding.common.BaseSimpleNode;

import org.rosbuilding.memory.manager.MemoryManager;

/**
 * Memory ROS Node.
 *
 * @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class MemoryNode extends BaseSimpleNode<MemoryConfig> {

    public static final String NAME = "memory";

    private MemoryManager memory;

    @Override
    public void onStart(Node connectedNode) {
        super.onStart(connectedNode);

        this.memory = new MemoryManager(this.configuration, connectedNode);
        this.memory.start();
    }

    @Override
    public void onShutdown() {
        this.memory.stop();

        super.onShutdown();
    }

    @Override
    protected MemoryConfig makeConfiguration() {
        return new MemoryConfig(this.getConnectedNode());
    }

    public static void main(String[] args) throws InterruptedException {
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure();
        RCLJava.rclJavaInit();

        final MemoryNode memory = new MemoryNode();
        final Node node = RCLJava.createNode(NAME);

        memory.onStart(node);
        memory.onStarted();

        RCLJava.spin(node);

        memory.onShutdown();
        memory.onShutdowned();

        RCLJava.shutdown();
    }
}
