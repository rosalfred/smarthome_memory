package org.rosbuilding.memory.subscribers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.rosbuilding.memory.subscribers.internal.BadMessageException;
import org.rosbuilding.memory.watcher.DetectNode;

public class TestTopicExtractor {

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
        DetectNode node = new DetectNode();
        node.parse("/node");

        assertEquals(node.getWorld(), "");
        assertEquals(node.getZone(), "");
        assertEquals(node.getName(), "node");
    }

    @Test
    public final void testNodeWorld() throws BadMessageException {
        DetectNode node = new DetectNode();
        node.parse("/world/node");

        assertEquals(node.getWorld(), "world");
        assertEquals(node.getZone(), "");
        assertEquals(node.getName(), "node");
    }

    @Test
    public final void testNodeOneZoneWorld() throws BadMessageException {
        DetectNode node = new DetectNode();
        node.parse("/world/zone/node");

        assertEquals(node.getWorld(), "world");
        assertEquals(node.getZone(), "zone");
        assertEquals(node.getName(), "node");
    }

    @Test
    public final void testNodeMultiZoneWorld() throws BadMessageException {
        DetectNode node = new DetectNode();
        node.parse("/world/level/zone/node");

        assertEquals(node.getWorld(), "world");
        assertEquals(node.getZone(), "level/zone");
        assertEquals(node.getName(), "node");
    }

}
