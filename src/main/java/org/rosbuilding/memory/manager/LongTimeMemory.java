/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.manager;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.rosbuilding.memory.tsdb.TimeSerieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongTimeMemory {

    private static final Logger logger = LoggerFactory.getLogger(LongTimeMemory.class);

    private final TimeSerieRepository db;
    private final MemoryManager memory;

    public LongTimeMemory(final MemoryManager memory, final TimeSerieRepository db) {
        this.memory = memory;
        this.db = db;
    }

    public void update(final List<String> nodes) {
        if (!nodes.isEmpty()) {
            DateTime date = DateTime.now();
            this.db.writeNodes(date, "node", nodes);
        }
    }

    public void insert(DateTime date, String measurement, Map<String, String> tags, Map<String, Object> fields) {
        this.db.writeTopic(date, measurement, tags, fields);
    }

    public void close() {
        // TODO Auto-generated method stub

    }
}
