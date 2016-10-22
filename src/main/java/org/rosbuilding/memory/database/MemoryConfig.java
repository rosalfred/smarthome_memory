/**
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.database;

import org.ros2.rcljava.node.Node;
import org.rosbuilding.common.NodeConfig;

/**
*
* @author Erwan Le Huitouze <erwan.lehuitouze@gmail.com>
*
*/
public class MemoryConfig extends NodeConfig {

    private String host;
    private int    port;
    private String user;
    private String password;
    private String name;

    public MemoryConfig(Node connectedNode) {
        super(connectedNode, "/influx", "fixed_frame", 1);
    }

    @Override
    protected void loadParameters() {
        super.loadParameters();

//        this.host = this.connectedNode.getParameterTree()
//                .getString("~ip", "localhost");
//        this.port = this.connectedNode.getParameterTree()
//                .getInteger("~port", 5684);
//        this.user = this.connectedNode.getParameterTree()
//                .getString("~user", "root");
//        this.password = this.connectedNode.getParameterTree()
//                .getString("~password", "root");
//        this.name = this.connectedNode.getParameterTree()
//                .getString("~name", "alfred");

        this.host = "localhost";
        this.port = 8086;
        this.user = "root";
        this.password = "root";
        this.name = "alfred";
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }
}
