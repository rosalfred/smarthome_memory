/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.tsdb;

import org.rosbuilding.memory.MemoryConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TimeSerieFactory {

    private static final Logger logger = LoggerFactory.getLogger(InfluxRepository.class);

    public static TimeSerieRepository makeRepository(MemoryConfig config) {
        logger.debug("Create Influx Repository...");
        return new InfluxRepository(config);
    }
}
