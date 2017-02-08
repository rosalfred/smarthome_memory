/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.watcher;

import java.util.List;

import org.rosbuilding.memory.CachedManager;

/**
 * Nodes watcher.<p>
 * Detect nodes.
 *
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class NodeWatcher extends BaseWatcher {

    /**
     * Constuctor of NodeWatcher.
     * @param cachedManager
     */
    public NodeWatcher(CachedManager cachedManager) {
        super(cachedManager);
    }

    @Override
    protected void check() {
        logger.debug("Detect Nodes available...");
        List<String> detectedNodes = this.cachedManager.getNode().getNodeNames();

        this.cachedManager.update(detectedNodes);
    }

    @Override
    protected String getWatcherName() {
        return "Watcher Node";
    }


}
