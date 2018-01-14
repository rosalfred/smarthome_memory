/*
 * This file is part of the Alfred package.
 *
 * (c) Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */

package org.rosbuilding.memory.tsdb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import org.rosbuilding.memory.MemoryConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InfluxDB Implementation TSDB Manager.
 *
 * @author Mickael Gaillard <mick.gaillard@gmail.com>
 */
public class InfluxRepository implements TimeSerieRepository {

    private static final Logger logger = LoggerFactory.getLogger(InfluxRepository.class);

    private static final String POLICY = "autogen";

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
    public InfluxRepository(MemoryConfig config) {
        this.config = config;

        logger.debug("Connection to InfluxDb...");

        final String cnxString = String.format("http://%s:%d", this.config.getHost(), this.config.getPort());
        this.influxDB = InfluxDBFactory.connect(
                cnxString,
                this.config.getUser(),
                this.config.getPassword());

        if (!this.influxDB.databaseExists(this.config.getName())) {
            this.influxDB.createDatabase(this.config.getName());
        }

        this.influxDB.enableBatch(
                this.config.getBatchActions(),
                this.config.getBatchTimeout(),
                TimeUnit.SECONDS);
        //this.influxDB.setLogLevel(LogLevel.FULL);
    }

    /* (non-Javadoc)
     * @see org.rosbuilding.memory.database.internal.TimeSerieDB#write(org.joda.time.DateTime, java.lang.String, java.util.Map, java.util.Map)
     */
    @Override
    public void writeTopic(DateTime date, String measurement, Map<String, String> tags, Map<String, Object> fields) {
        logger.debug("Write data on InfluxDb...");
        long time = date.withZone(DateTimeZone.UTC).getMillis();

        Builder builder = Point.measurement(measurement).time(time, TimeUnit.MILLISECONDS);
        builder.tag(tags);
        builder.fields(fields);

        try {
            Point point = builder.build();
            this.influxDB.write(this.config.getName(), POLICY, point);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    @Override
    public void writeNodes(DateTime date, String measurement, List<String> nodes) {
        logger.debug("Write data on InfluxDb...");
        long time = date.withZone(DateTimeZone.UTC).getMillis();

        BatchPoints batchPoints = BatchPoints
                .database(this.config.getName())
                .retentionPolicy(POLICY)
                .consistency(ConsistencyLevel.ALL)
                .build();

        try {
            for (String node : nodes) {
                DetectNode detectNode = new DetectNode();
                detectNode.parse(node);
                //detectNode.findSGBDR();
                Map<String, Object> fields = new HashMap<String, Object>();
                fields.put("value", 0);

                Builder builder = Point.measurement(measurement).time(time, TimeUnit.MILLISECONDS);
                builder.tag(detectNode.getMessageTags());
                builder.fields(fields);

                Point point = builder.build();
                batchPoints.point(point);
            }

            if (!batchPoints.getPoints().isEmpty()) {
                this.influxDB.write(batchPoints);
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }
}
