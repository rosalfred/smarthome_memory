/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.tsdb;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

/**
 * Time Serie Database Manager.
 *
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public interface TimeSerieRepository {

    /**
     * Write Data to Time Serie Database.
     * @param date
     * @param measurement
     * @param tags
     * @param fields
     */
    void writeTopic(DateTime date, String measurement, Map<String, String> tags, Map<String, Object> fields);

    void writeNodes(DateTime date, String measurement, List<String> cachedNodes);

}
