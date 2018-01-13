/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.watcher;

import org.rosbuilding.memory.manager.MemoryManager;

/**
 * Topics watcher.<p>
 * Detect new/destroy Topics.
 *
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class TopicWatcher extends ThreadWatcherBase {

    /**
     * Constructor of TopicWatcher.
     * @param cachedManager
     */
    public TopicWatcher(final MemoryManager memory) {
        super(memory);
    }

    @Override
    protected void check() {
        this.logger.debug("Detect Topics available...");

        this.memory.refreshTopics();
    }

    @Override
    protected String getWatcherName() {
        return "Watcher Topic";
    }
}
