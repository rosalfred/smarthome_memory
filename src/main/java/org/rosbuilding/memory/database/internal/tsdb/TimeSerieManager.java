/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.database.internal.tsdb;

import java.util.Map;

import org.joda.time.DateTime;

/** Time Serie Database Manager. */
public interface TimeSerieManager {

    /**
     * Write Data to Time Serie Database.
     * @param date
     * @param measurement
     * @param tags
     * @param fields
     */
    void write(DateTime date, String measurement, Map<String, String> tags, Map<String, Object> fields);

}
