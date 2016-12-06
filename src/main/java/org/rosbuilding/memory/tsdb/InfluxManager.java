/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package org.rosbuilding.memory.tsdb;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import org.rosbuilding.memory.MemoryConfig;
import org.rosbuilding.memory.MemoryNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InfluxDB Implementation TSDB Manager.
 *
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class InfluxManager implements TimeSerieManager {

    private static final Logger logger = LoggerFactory.getLogger(InfluxManager.class);

    private static final String POLICY = "autogen";

    private final MemoryNode node;

    /**
     * The settings of the application.
     */
    protected MemoryConfig config;

    /**
     * The real influx database driver.
     */
    protected InfluxDB influxDB;

    /**
     * Constructor of InfluxDB Manager.
     *
     * @param setting The settings of influxDB.
     */
    public InfluxManager(MemoryNode node, MemoryConfig config) {
        this.node = node;
        this.config = config;

        logger.debug("Connection to InfluxDb...");

        String cnxString = String.format("http://%s:%d", this.config.getHost(), this.config.getPort());
        this.influxDB = InfluxDBFactory.connect(
                cnxString,
                this.config.getUser(),
                this.config.getPassword());
        this.influxDB.createDatabase(this.config.getName());
//        this.influxDB.enableBatch(
//                this.config.getBatchActions(),
//                this.config.getBatchTimeout(),
//                TimeUnit.MILLISECONDS);
        //this.influxDB.setLogLevel(LogLevel.FULL);
    }

    /* (non-Javadoc)
     * @see org.rosbuilding.memory.database.internal.TimeSerieDB#write(org.joda.time.DateTime, java.lang.String, java.util.Map, java.util.Map)
     */
    @Override
    public void write(DateTime date, String measurement, Map<String, String> tags, Map<String, Object> fields) {
        logger.debug("Write data on InfluxDb...");
        long time = date.withZone(DateTimeZone.UTC).getMillis();

        Builder builder = Point.measurement(measurement).time(time, TimeUnit.MILLISECONDS);
        builder.tag(tags);
        builder.fields(fields);

        try {
            Point point = builder.build();
            this.influxDB.write(this.config.getName(), POLICY, point);
        } catch (Exception e) {
            this.node.logD(e.getMessage());
        }
    }
}
