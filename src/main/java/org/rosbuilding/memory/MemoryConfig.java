/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory;

import org.ros2.rcljava.node.Node;
import org.rosbuilding.common.NodeDriverConnectedConfig;

/**
*
* @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
* @author Mickael Gaillard <mick.gaillard@gmail.com>
*/
public class MemoryConfig extends NodeDriverConnectedConfig {

    /** Name of TSDB database */
    private String name;

    public MemoryConfig(Node connectedNode) {
        super(
                connectedNode,
                "/home",
                "memory",
                "fixed_frame",
                1,
                "00:00:00:00:00:00",
                "localhost",
                8086,
                "admin",
                "admin");

        this.name = "smarthome_memory";
    }

    /**
     * Name of TSDB.
     * @return String name
     */
    public String getName() {
        return this.name;
    }
}
