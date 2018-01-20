package org.rosbuilding.memory.concept.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import org.ros2.rcljava.internal.message.Message;

public abstract class MessageInfoInfluxBase<T extends Message> extends MessageInfoBase<T> {

    protected static final String TIME = "time";

    private final Class<?> pojoClass;
    private final Map<Field, ColumnMethod> cacheFields = new HashMap<Field, ColumnMethod>();

    private static class ColumnMethod {
        public ColumnMethod(Column column, Method method) {
            this.column = column;
            this.method = method;
        }

        Column column;
        Method method;
    }

    protected MessageInfoInfluxBase(
            String topic,
            Class<T> messageClass,
            Class<?> pojoClass) {
        super(topic, messageClass, getMeasurementName(pojoClass), null);
        this.pojoClass = pojoClass;

        // Make cache.
        final Field fields[] = this.pojoClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);

                String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                Method method = messageClass.getMethod(methodName);

                Column column = field.getAnnotation(Column.class);

                this.cacheFields.put(field, new ColumnMethod(column, method));
            } catch (Exception e) {
                MessageInfoInfluxBase.this.logger.debug(String.format("Field %s not found in message.", field.getName()));
            }
        }
    }

    @Override
    public Map<String, Object> getMessageFields(T message) {
        Map<String, Object> result = new HashMap<>();

        for (Entry<Field, ColumnMethod> field : cacheFields.entrySet()) {
            if (!field.getValue().column.tag() && !TIME.equals(field.getValue().column.name())) {
                try {
                    result.put(field.getValue().column.name(), field.getValue().method.invoke(message));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

//    @Override
//    public Map<String, String> getMessageTags(T message) throws BadInfoException {
//        Map<String, String> result = new HashMap<>();
//        DetectNode node = new DetectNode();
//
//        String topic = this.getTopic().replace(STATEDATA, "");
//
//        node.parse(topic);
//        //node.findSGBDR();
//
//        if (Strings.isNullOrEmpty(node.getName())) {
//            throw new BadInfoException();
//        }
//
//        result.putAll(node.getMessageTags());
//
//        return result;
//    }

    protected static String getMeasurementName(final Class<?> clazz) {
        return ((Measurement) clazz.getAnnotation(Measurement.class)).name();
    }

//    public Temperature toPojo(sensor_msgs.msg.Temperature message) {
//        Temperature result = null;
//
//        result = new Temperature();
//
//        for (String string : listOfField) {
//
//		}
//    for (Field field : listOfField) {
//        Column column = field.getAnnotation(Column.class);
//        if (!column.tag() && !column.name().equals("time")) {
//            field.setAccessible(true);
//            field.getType()
//
//            field.set(obj, value);
//        }
//    }
//
//        getValue(result, message)
//        result.temperature = message.getTemperature();
//        result.variance = message.getVariance();
//
//        return result;
//    }
}
