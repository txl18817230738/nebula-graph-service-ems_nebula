/* Copyright (c) 2022 vesoft inc. All rights reserved.
 *
 * This source code is licensed under Apache 2.0 License.
 */

package org.univers.ems.util;

import com.vesoft.nebula.client.graph.data.Relationship;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.data.ValueWrapper;
import com.vesoft.nebula.client.graph.data.Vertex;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.exception.NoValidSessionException;
import com.vesoft.nebula.client.graph.net.NebulaClient;
import com.vesoft.nebula.client.graph.scan.ScanEdgeResult;
import com.vesoft.nebula.client.graph.scan.ScanEdgeResultIterator;
import com.vesoft.nebula.client.graph.scan.ScanNodeResult;
import com.vesoft.nebula.client.graph.scan.ScanNodeResultIterator;
import com.vesoft.nebula.client.graph.scan.TableRow;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphClientExample {
    private static final Logger log = LoggerFactory.getLogger(GraphClientExample.class);
    static String host = "10.65.62.86:9669";
    static String user = "root";
    static String passwd = "nebula";

    public static void main(String[] args) {
        NebulaClient client = null;
        try {
            // init the NebulaPool and get session
            client = NebulaClient.builder(host, user, passwd)
                    .setConnectTimeoutMills(10000)
                    .setRequestTimeoutMills(30000)
                    .setMaxSessionSize(10)
                    .setMinSessionSize(1)
                    .setRetryTimes(3)
                    .setIntervalTimeMills(1000)
                    .setReconnect(true)
                    .setBlockWhenExhausted(true)
                    .setMaxWaitMills(1000)
                    .setStrictlyServerHealthy(true)
                    .build();

            //createGraphType(client);
            //createGraph(client);
            //insertData(client);
            query(client);
            //scanNode(client);
            //scanEdge(client);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    private static void createGraphType(NebulaClient client) throws IOErrorException,
            InterruptedException, NoValidSessionException {
        String createSchema = "CREATE GRAPH TYPE graph_type_nba AS {"
                + "(node_type_player(id) LABEL player {id INT, name STRING, score FLOAT, gender"
                + " bool, rate DOUBLE}),(node_type_player)-[edge_type_follow LABEL follow "
                + "{followness INT, likeness FLOAT64}]->(node_type_player)}";
        ResultSet resp = client.execute(createSchema);
        if (!resp.isSucceeded()) {
            log.error(String.format("Execute: `%s', failed: %s",
                    createSchema, resp.getGqlStatus()));
            System.out.println("create graph type failed, " + resp.getGqlStatus());
            System.exit(1);
        }
        TimeUnit.SECONDS.sleep(5);
    }

    private static void createGraph(NebulaClient client) throws IOErrorException,
            InterruptedException, NoValidSessionException {
        String createGraph = "CREATE GRAPH nba OF graph_type_nba";
        ResultSet resp = client.execute(createGraph);
        if (!resp.isSucceeded()) {
            log.error(String.format("Execute `%s`, failed: %s", createGraph,
                    resp.getGqlStatus()));
            System.out.println("create graph failed, " + resp.getGqlStatus());
            System.exit(1);
        }
        TimeUnit.SECONDS.sleep(5);
    }

    private static void insertData(NebulaClient client) throws IOErrorException,
            NoValidSessionException {
        String insertVertexes = " USE nba INSERT NODE node_type_player ({id:1, name:\"Tim\", "
                + "score: 87.0, gender: true, rate: 7.32}),({id:2, name:\"Jerry\", score: 95.0,"
                + " gender: false, rate: 4.01}),({id:3, name:\"Kyle\", score: 100, gender: "
                + "true, rate: 9.99})";
        ResultSet resp = client.execute(insertVertexes);
        if (!resp.isSucceeded()) {
            log.error(String.format("Execute: `%s', failed: %s",
                    insertVertexes, resp.getGqlStatus()));
            System.out.println("insert graph node failed, " + resp.getGqlStatus());
            System.exit(1);
        }

        String insertEdges = "USE nba INSERT EDGE edge_type_follow ({id:1})-[{followness:90, "
                + "likeness: 66.8}]->({id:2}),({id:2})-[{followness:100, likeness: 93.35}]->"
                + "({id:3})";
        resp = client.execute(insertEdges);
        if (!resp.isSucceeded()) {
            log.error(String.format("Execute: `%s', failed: %s",
                    insertEdges, resp.getGqlStatus()));
            System.out.println("insert graph edge failed, " + resp.getGqlStatus());
            System.exit(1);
        }
    }

    private static void query(NebulaClient client) throws IOErrorException,
            NoValidSessionException {
        String queryNode = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Breaker]->(F6:Breaker)" +
                " optional match (s)<-[:topoid_subid]-(t:TopoND) where startid.id = \"113715890758877224\" " +
                "return F6.id,F6.Substation,s.strid,F6.name,F6.volt,F6.point,F6.I_nd,F6.J_nd limit 10";
        ResultSet resp = client.execute(queryNode);
        if (!resp.isSucceeded()) {
            log.error(String.format("Execute: `%s', failed: %s",
                    queryNode, resp.getGqlStatus()));
        }
        resolve(resp);

        System.out.println("\n\n");

        /*String queryEdge = "USE nba MATCH ()-[e:follow]->() RETURN e.followness, e.likeness";
        resp = client.execute(queryEdge);
        if (!resp.isSucceeded()) {
            log.error(String.format("Execute: `%s', failed: %s",
                    queryNode, resp.getGqlStatus()));
        }
        resolve(resp);*/
    }


    private static void resolve(ResultSet resultSet) {
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
            // process each line
            List<ValueWrapper> values = record.values();
            for (ValueWrapper valueWrapper : values) {
                // process each property for one line
                if (valueWrapper.isNull()) {
                    System.out.printf("%15s |", "");
                } else if (valueWrapper.isEmpty()) {
                    System.out.printf("%15s |", "_EMPTY_");
                } else if (valueWrapper.isByte()) {
                    System.out.printf("%15s |", valueWrapper.asByte());
                } else if (valueWrapper.isShort()) {
                    System.out.printf("%15s |", valueWrapper.asShort());
                } else if (valueWrapper.isInt()) {
                    System.out.printf("%15s |", valueWrapper.asInt());
                } else if (valueWrapper.isLong()) {
                    System.out.printf("%15s |", valueWrapper.asLong());
                } else if (valueWrapper.isBoolean()) {
                    System.out.printf("%15s |", valueWrapper.asBoolean());
                } else if (valueWrapper.isDouble()) {
                    System.out.printf("%15s |", valueWrapper.asDouble());
                } else if (valueWrapper.isString()) {
                    System.out.printf("%15s |", valueWrapper.asString());
                } else if (valueWrapper.isDate()) {
                    System.out.printf("%15s |", valueWrapper.asDate());
                } else if (valueWrapper.isLocalTime()) {
                    System.out.printf("%15s |", valueWrapper.asLocalTime());
                } else if (valueWrapper.isLocalDateTime()) {
                    System.out.printf("%15s |", valueWrapper.asLocalDateTime());
                } else if (valueWrapper.isDuration()) {
                    System.out.printf("%15s |", valueWrapper.asDuration());
                } else if (valueWrapper.isList()) {
                    System.out.printf("%15s |", valueWrapper.asList());
                } else if (valueWrapper.isMap()) {
                    System.out.printf("%15s |", valueWrapper.asMap());
                } else if (valueWrapper.isNode()) {
                    Vertex node = valueWrapper.asNode();
                    long nodeId = node.getId();
                    String nodeType = node.getNodeType();
                    Map<String, ValueWrapper> properties = node.getProperties();
                    System.out.printf("%15s |", valueWrapper.asNode());
                } else if (valueWrapper.isEdge()) {
                    Relationship relationship = valueWrapper.asEdge();
                    long srcId = relationship.getSrcId();
                    long dstId = relationship.getDstId();
                    long rank = relationship.getRank();
                    Map<String, ValueWrapper> properties = relationship.getProperties();
                    System.out.printf("%15s |", valueWrapper.asEdge());
                }
            }
        }
    }


    private static void scanNode(NebulaClient client) {
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


    private static void scanEdge(NebulaClient client) {
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
}
