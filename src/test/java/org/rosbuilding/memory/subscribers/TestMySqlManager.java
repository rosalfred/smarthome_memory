package org.rosbuilding.memory.subscribers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rosbuilding.memory.sgbd.MySqlManager;
import org.rosbuilding.memory.watcher.DetectNode;

public class TestMySqlManager {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void test() {
        MySqlManager manager = new MySqlManager();
        DetectNode node = new DetectNode();
        node.parse("/home/salon/heater");
        manager.getId(node);

        assertEquals(node.getNodeTag(), "node_35");
        assertEquals(node.getZoneTag(), "zone_8");
        assertEquals(node.getWorldTag(), "world_2");
    }

}
