package org.rosbuilding.memory.database.internal;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.rosbuilding.memory.database.MemoryConfig;
import org.rosbuilding.memory.database.MemoryNode;

public class InfluxDb {

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
     * Construct.
     *
     * @param setting
     *            The settings.
     */
    public InfluxDb(MemoryNode node, MemoryConfig config) {
        this.node = node;
        this.config = config;
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

    public void write(DateTime date, String measurement, Map<String, String> tags, Map<String, Object> fields) {
        this.node.logI("Write data...");

        long time = date.withZone(DateTimeZone.UTC).getMillis();

        Builder builder = Point.measurement(measurement).time(time, TimeUnit.MILLISECONDS);
        builder.tag(tags);
        builder.fields(fields);

        Point point = builder.build();

        try {
            this.influxDB.write(this.config.getName(), "autogen", point);
        } catch (Exception e) {
            this.node.getConnectedNode().getLog().debug(e.getMessage());
        }
    }
}
