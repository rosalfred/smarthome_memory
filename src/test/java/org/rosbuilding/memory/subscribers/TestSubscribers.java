package org.rosbuilding.memory.subscribers;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.ros2.rcljava.internal.message.Message;

import org.rosbuilding.memory.subscribers.internal.BadMessageException;
import org.rosbuilding.memory.subscribers.internal.MessageSubscriberBase;
import org.rosbuilding.memory.watcher.DetectNode;

public class TestSubscribers {

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
    public final void testNodeOnly() throws BadMessageException {
        MessageSubscriberBase<Message> sub = new MockSubscriber("/node");
        Map<String, String> map = sub.getMessageTags(null);

        assertNull(map.get(DetectNode.TAG_WORLD));
        assertNull(map.get(DetectNode.TAG_ZONE));
        assertEquals(map.get(DetectNode.TAG_NODE), "node_0");
    }

    @Test
    public final void testNodeWorld() throws BadMessageException {
        MessageSubscriberBase<Message> sub = new MockSubscriber("/world/node");
        Map<String, String> map = sub.getMessageTags(null);

        assertEquals(map.get(DetectNode.TAG_WORLD), "world_0");
        assertNull(map.get(DetectNode.TAG_ZONE));
        assertEquals(map.get(DetectNode.TAG_NODE), "node_0");
    }

    @Test
    public final void testNodeOneZoneWorld() throws BadMessageException {
        MessageSubscriberBase<Message> sub = new MockSubscriber("/world/zone/node");
        Map<String, String> map = sub.getMessageTags(null);

        assertEquals(map.get(DetectNode.TAG_WORLD), "world_0");
        assertEquals(map.get(DetectNode.TAG_ZONE), "zone_0");
        assertEquals(map.get(DetectNode.TAG_NODE), "node_0");
    }

    @Test
    public final void testNodeMultiZoneWorld() throws BadMessageException {
        MessageSubscriberBase<Message> sub = new MockSubscriber("/world/level/zone/node");
        Map<String, String> map = sub.getMessageTags(null);

        assertEquals(map.get(DetectNode.TAG_WORLD), "world_0");
        assertEquals(map.get(DetectNode.TAG_ZONE), "zone_0");
        assertEquals(map.get(DetectNode.TAG_NODE), "node_0");
    }

}
