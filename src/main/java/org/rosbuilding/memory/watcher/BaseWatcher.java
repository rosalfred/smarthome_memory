/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.watcher;

import org.rosbuilding.memory.CachedManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base watcher.
 *
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public abstract class BaseWatcher implements Runnable {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** Cached Manager. */
    protected final CachedManager cachedManager;

    // Thread component.
    protected volatile Thread blinker;
    protected boolean threadSuspended;

    public BaseWatcher(CachedManager cachedManager) {
        this.cachedManager = cachedManager;
    }

    /** Start the watcher */
    public void start() {
        logger.debug("Start watcher...");
        this.blinker = new Thread(this);
        this.blinker.setName(this.getWatcherName());
        this.blinker.start();
    }

    /** Stop the watcher */
    public synchronized void stop() {
        logger.debug("Stop watcher...");
        this.blinker = null;
        this.notify();
    }

    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();

        while (blinker == thisThread) {
            BaseWatcher.this.check();

            try {
                Thread.sleep(5000);

                synchronized(this) {
                    while (threadSuspended && blinker==thisThread)
                        wait();
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    protected abstract void check();

    protected abstract String getWatcherName();
}
