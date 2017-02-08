/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.simulator.base;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.Node;
import org.rosbuilding.memory.simulator.CommSimulator;
import org.rosbuilding.memory.simulator.HeaterSimulator;
import org.rosbuilding.memory.simulator.LightSimulator;
import org.rosbuilding.memory.simulator.MediaSimulator;

public class SimulatorLauncher {
    private static final String NODE_NAME = SimulatorLauncher.class.getSimpleName().toLowerCase();

    public static void main(String[] args) throws InterruptedException {
        RCLJava.rclJavaInit();
        Node node = RCLJava.createNode(NODE_NAME);

        ThreadGroup threads = new ThreadGroup("SimulatorPool");
        (new CommSimulator(node, threads)).start();
//        (new ElectricalSimulator(node, threads)).start();
        (new HeaterSimulator(node, threads)).start();
        (new LightSimulator(node, threads)).start();
        (new MediaSimulator(node, threads)).start();

        RCLJava.spin(node);

        node.dispose();
        RCLJava.shutdown();
    }
}
