package org.univers.ems.nebula.util;

import com.vesoft.nebula.client.graph.data.Relationship;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.data.ValueWrapper;
import com.vesoft.nebula.client.graph.data.Vertex;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.exception.NoValidSessionException;
import com.vesoft.nebula.client.graph.net.NebulaClient;
import com.vesoft.nebula.client.graph.scan.*;
import org.univers.ems.service.EntityConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NebulaExecFunction {

    public static void resolve(ResultSet resultSet) {
        if (!resultSet.isSucceeded()) {
            System.out.println("result is not succeed, status is : " + resultSet.getGqlStatus());
            return;
        }
        // resolve the resultSet content only when resultSet is succeed.
        System.out.println("query result row size: " + resultSet.getRows().size());
        System.out.println("query latency: " + resultSet.getLatency());
        List<String> columns = resultSet.getColumnNames();
        System.out.println("result columns: " + columns);
        List<ResultSet.Record> records = resultSet.getRows();
        for (ResultSet.Record record : records) {
            StringBuilder recordOutput = new StringBuilder(); // 用于构建每个record的输出
            // process each line
            List<ValueWrapper> values = record.values();
            System.out.println(values);
            for (ValueWrapper valueWrapper : values) {
                // process each property for one line
                if (valueWrapper.isNull()) {
                    recordOutput.append(String.format("%15s |", ""));
                } else if (valueWrapper.isEmpty()) {
                    recordOutput.append(String.format("%15s |", "_EMPTY_"));
                } else if (valueWrapper.isByte()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asByte()));
                } else if (valueWrapper.isShort()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asShort()));
                } else if (valueWrapper.isInt()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asInt()));
                } else if (valueWrapper.isLong()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asLong()));
                } else if (valueWrapper.isBoolean()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asBoolean()));
                } else if (valueWrapper.isDouble()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asDouble()));
                } else if (valueWrapper.isString()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asString()));
                } else if (valueWrapper.isDate()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asDate()));
                } else if (valueWrapper.isLocalTime()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asLocalTime()));
                } else if (valueWrapper.isLocalDateTime()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asLocalDateTime()));
                } else if (valueWrapper.isDuration()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asDuration()));
                } else if (valueWrapper.isList()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asList()));
                } else if (valueWrapper.isMap()) {
                    recordOutput.append(String.format("%15s |", valueWrapper.asMap()));
                } else if (valueWrapper.isNode()) {
                    Vertex node = valueWrapper.asNode();
                    long nodeId = node.getId();
                    String nodeType = node.getNodeType();
                    Map<String, ValueWrapper> properties = node.getProperties();
                    recordOutput.append(String.format("%15s |", node.toString()));
                } else if (valueWrapper.isEdge()) {
                    Relationship relationship = valueWrapper.asEdge();
                    long srcId = relationship.getSrcId();
                    long dstId = relationship.getDstId();
                    long rank = relationship.getRank();
                    Map<String, ValueWrapper> properties = relationship.getProperties();
                    recordOutput.append(String.format("%15s |", relationship.toString()));
                } else {
                    // 处理未知类型的ValueWrapper
                    recordOutput.append(String.format("%15s |", "UNKNOWN_TYPE"));
                }
            }
            System.out.println(recordOutput);
        }
    }


    public static void scanNode(NebulaClient client) {
        String graphName = "nba";
        String nodeType = "node_type_player";

        ScanNodeResultIterator iterator = client.scanNode(graphName, nodeType, 3, 10);
        while (iterator.hasNext()) {
            ScanNodeResult result = iterator.next();
            if (result.isEmpty()) {
                continue;
            }
            System.out.println(result.getPropNames());
            List<TableRow> tableRows = result.getTableRows();
            for (TableRow row : tableRows) {
                System.out.println(row.getValues());
            }
            System.out.println("\n");
        }
    }


    public static void scanEdge(NebulaClient client) {
        String graphName = "nba";
        String edgeType = "edge_type_follow";

        ScanEdgeResultIterator iterator = client.scanEdge(graphName, edgeType, 3, 10);
        while (iterator.hasNext()) {
            ScanEdgeResult result = iterator.next();
            if (result.isEmpty()) {
                continue;
            }
            System.out.println(result.getPropNames());
            List<TableRow> tableRows = result.getTableRows();
            for (TableRow row : tableRows) {
                System.out.println(row.getValues());
            }
            System.out.println("\n");
        }
    }

    /**
     *
     * 解析数据，将数据写入List
     *
     * */
    public static <T> List<T> resolve(ResultSet resultSet, EntityConverter<T> converter) {
        List<T> entities = new ArrayList<>();
        if (!resultSet.isSucceeded()) {
            System.out.println("result is not succeed, status is : " + resultSet.getGqlStatus());
            return entities;
        }
        System.out.println("query result row size: " + resultSet.getRows().size());
        System.out.println("query latency: " + resultSet.getLatency());
        for (ResultSet.Record record : resultSet.getRows()) {
            entities.add(converter.convert(record));
        }
        return entities;
    }

    /**
     *
     * 查询数据的总条数
     *
     * */
    public static int executeQueryForTotalSize(NebulaClient client, String query) {
        ResultSet resultSet = null;
        try {
            resultSet = client.execute(query);
            //System.out.println("query latency: " + resultSet.getLatency());
        } catch (IOErrorException | NoValidSessionException e) {
            throw new RuntimeException(e);
        }
        if (!resultSet.isSucceeded()) {
            throw new RuntimeException(String.format("Execute: `%s`, failed: %s", query, resultSet.getGqlStatus()));
        }
        return resultSet.getRows().size();
    }


    /**
     *
     * 查询数据
     *
     * */
    public static ResultSet executeQueryForData(NebulaClient client,String query) {
        ResultSet resultSet = null;
        try {
            resultSet = client.execute(query);
        } catch (IOErrorException | NoValidSessionException e) {
            throw new RuntimeException(e);
        }
        if (!resultSet.isSucceeded()) {
            throw new RuntimeException(String.format("Execute: `%s`, failed: %s", query, resultSet.getGqlStatus()));
        }
        return resultSet;
    }

    public static Map<String,String> resolveToMap(ResultSet resultSet, String keyColumnName, String valueColumnName) {
        Map<String,String> map = new HashMap<>();
        if (!resultSet.isSucceeded()) {
            System.out.println("result is not succeed, status is : " + resultSet.getGqlStatus());
            return map;
        }
        for (ResultSet.Record record : resultSet.getRows()) {
            map.put(String.valueOf(record.get(keyColumnName)),String.valueOf(record.get(valueColumnName)));
        }
        return map;
    }

    public static <K, V> Map<V, Integer> countMapValues(Map<K, V> map) {
        Map<V, Integer> valueCount = new HashMap<>();
        for (V value : map.values()) {
            valueCount.put(value, valueCount.getOrDefault(value, 0) + 1);
        }
        return valueCount;
    }
}
