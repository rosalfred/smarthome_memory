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
 * Nodes watcher.<p>
 * Detect nodes.
 *
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class NodeWatcher extends ThreadWatcherBase {

    /**
     * Constuctor of NodeWatcher.
     * @param cachedManager
     */
    public NodeWatcher(final MemoryManager memory) {
        super(memory);
    }

    @Override
    protected void check() {
        this.logger.debug("Detect Nodes available...");

        this.memory.refreshNodes();
    }

    @Override
    protected String getWatcherName() {
        return "Watcher Node";
    }

}
