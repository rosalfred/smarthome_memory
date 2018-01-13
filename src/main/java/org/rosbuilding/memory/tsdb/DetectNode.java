/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.tsdb;

import java.util.HashMap;
import java.util.Map;

//import org.rosbuilding.memory.sgbd.MySqlManager;

public class DetectNode {
    public static final String FLD_TOPIC = "topic";
    public static final String FLD_WORLD = "world";
    public static final String FLD_ZONE  = "zone";
    public static final String FLD_NODE  = "node";

    public static final String TAG_TOPIC = "sys." + FLD_TOPIC;
    public static final String TAG_WORLD = "db." + FLD_WORLD;
    public static final String TAG_ZONE = "db." + FLD_ZONE;
    public static final String TAG_NODE = "db." + FLD_NODE;
    public static final String SEPARATOR = "/";

    private int worldId = 0;
    private int zoneId = 0;
    private int nodeId = 0;

    private String topic;

    private String world = "";
    private String name  = "";
    private StringBuilder zone  = new StringBuilder();

//    public void findSGBDR() {
//        // Query node from SGBD
//        MySqlManager manager = new MySqlManager();
//        manager.getId(this);
//    }

    public void parse(String topic) {
        this.topic = topic;

        if (topic.startsWith("/"))
            topic = topic.substring(1, topic.length());

        String[] splitTopic = topic.split(SEPARATOR);
        int splitTopicCount = splitTopic.length -1;

        for (int i = splitTopicCount; i >= 0; i--) {
            if (i == splitTopicCount) {
                this.name = splitTopic[i];
            }
            if (i == 0 && splitTopicCount > 0) {
                this.world = splitTopic[i];
            }
            if (i > 0 && i < splitTopicCount) {
                if (this.zone.length() > 0) {
                    this.zone.insert(0, splitTopic[i] + SEPARATOR);
                } else {
                    this.zone.insert(0, splitTopic[i]);
                }
            }
        }
    }

    public Map<String, String> getMessageTags() {
        Map<String, String> result = new HashMap<>();
        result.put(TAG_TOPIC, this.topic);

        if (!this.getWorld().isEmpty())
            result.put(TAG_WORLD, this.getWorldTag());

        if (!this.getZone().toString().isEmpty())
            result.put(TAG_ZONE,  this.getZoneTag());

        result.put(TAG_NODE,  this.getNodeTag());

        return result;
    }

    public final String getTopic() {
        return this.topic;
    }

    /**
     * @return the world
     */
    public final String getWorld() {
        return this.world;
    }

    /**
     * @return the node
     */
    public final String getName() {
        return this.name;
    }

    /**
     * @return the zone
     */
    public final String getZone() {
        return this.zone.toString();
    }

    /**
     * @return the world Tag
     */
    public final String getWorldTag() {
        return FLD_WORLD + "_" + this.worldId;
    }

    /**
     * @return the node Tag
     */
    public final String getNodeTag() {
        return FLD_NODE + "_" + this.nodeId;
    }

    /**
     * @return the zone Tag
     */
    public final String getZoneTag() {
        return FLD_ZONE + "_" + this.zoneId;
    }

    public void setId(int nid, int zid, int wid) {
        this.nodeId = nid;
        this.zoneId = zid;
        this.worldId = wid;
    }
}
