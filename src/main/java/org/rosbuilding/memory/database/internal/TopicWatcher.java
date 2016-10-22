package org.rosbuilding.memory.database.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class TopicWatcher {
    private static final String MEASUREMENT = "topic";

    private final InfluxDb influx;

    private List<String> topics;

    public TopicWatcher(InfluxDb influx) {
        this.topics = new ArrayList<>();
        this.influx = influx;
    }

    public void checkTopics(HashMap<String,String> topicsTypes) {
        List<String> addedTopics = new ArrayList<>(topicsTypes.keySet());
        List<String> removedTopics = new ArrayList<>(this.topics);

        addedTopics.removeAll(this.topics);
        removedTopics.removeAll(topicsTypes.keySet());

        this.topics = new ArrayList<>(topicsTypes.keySet());

        DateTime now = DateTime.now();

        for (String topic : addedTopics) {
            this.insert(now, this.getTags(topic), this.getFields(true));
        }

        for (String topic : removedTopics) {
            this.insert(now, this.getTags(topic), this.getFields(false));
        }
    }

    private Map<String, String> getTags(String topic) {
        Map<String, String> result = new HashMap<>();

        result.put("topic", topic);

        return result;
    }

    private Map<String, Object> getFields(boolean online) {
        Map<String, Object> result = new HashMap<>();

        result.put("status", online);

        return result;
    }

    private void insert(DateTime date, Map<String, String> tags, Map<String, Object> fields) {
        //this.influx.write(date, MEASUREMENT, tags, fields);
    }
}
