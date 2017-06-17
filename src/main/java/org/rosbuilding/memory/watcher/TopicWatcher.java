/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.rosbuilding.memory.CachedManager;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

/**
 * Topics watcher.<p>
 * Detect new/destroy Topics.
 *
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class TopicWatcher extends BaseWatcher {

    /**
     * Constructor of TopicWatcher.
     * @param cachedManager
     */
    public TopicWatcher(final CachedManager cachedManager) {
        super(cachedManager);
    }

    @Override
    protected void check() {
        logger.debug("Detect Topics available...");
        HashMap<String, List<String>> topicsTypes = this.cachedManager.getNode().getTopicNamesAndTypes();

        MapDifference<String, List<String>> diff = Maps.difference(this.cachedManager.getTopicCaches(), topicsTypes);
        Map<String, List<String>> removed = diff.entriesOnlyOnLeft();
        Map<String, List<String>> added   = diff.entriesOnlyOnRight();

        // TODO Remove from detected node, not only from topic (because is subscribed)
        // Remove removed topic.
        for (Entry<String, List<String>> topic : removed.entrySet()) {
            this.cachedManager.remove(topic.getKey());
        }

        // Add added topic.
        for (Entry<String, List<String>> topic : added.entrySet()) {
            this.cachedManager.add(topic.getKey(), topic.getValue());
        }
    }

    @Override
    protected String getWatcherName() {
        return "Watcher Topic";
    }
}
