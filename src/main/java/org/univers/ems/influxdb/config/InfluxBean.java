package org.univers.ems.influxdb.config;

import com.influxdb.client.*;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxTable;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Setter
@Getter
public class InfluxBean{
    /**
     * 数据库url地址
     */
    private String influxUrl;
    /**
     * 桶(表)
     */
    private String bucket;
    /**
     * 组织
     */
    private String org;
    /**
     * token
     */
    private String token;
    /**
     * 数据库连接
     */
    private InfluxDBClient client;
    /**
     * 构造方法
     */
    public InfluxBean(String influxUrl, String bucket, String org, String token) {
        this.influxUrl = influxUrl;
        this.bucket = bucket;
        this.org = org;
        this.token = token;
        this.client = getClient();
    }
    /**
     * 获取连接
     */
    private InfluxDBClient getClient() {
        if (client == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(Duration.ofMinutes(2)) // 设置连接超时
                    .readTimeout(Duration.ofMinutes(5)); // 设置读取超时，可以用于查询超时

            InfluxDBClientOptions options = InfluxDBClientOptions.builder()
                    .url(influxUrl)
                    .authenticateToken(token.toCharArray())
                    .org(org)
                    .bucket(bucket)
                    .okHttpClient(httpClientBuilder)
                    .build();
            client  = InfluxDBClientFactory.create(options);
        }
        return client;
    }
    /**
     * 写入数据(以秒为时间单位)
     */
    public void write(Object object){
        /*String data = "com_history_p_meas, host=host1 used_percent=23.43234543";
        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        writeApi.writeRecord(bucket, org, WritePrecision.NS, data);*/

        try (WriteApi writeApi = client.getWriteApi()) {
            writeApi.writeMeasurement(bucket, org, WritePrecision.NS, object);
        }
    }

    public void writeData(){
        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        Point point = Point.measurement("temperature")
                .addTag("location", "west12")
                .addField("value", 55D)
                .addField("test_value_long",600.00)
                .time(Instant.now().toEpochMilli(), WritePrecision.MS);
        writeApi.writePoint(point);
    }

    /**
     * 读取数据
     */
    public List<FluxTable> queryTable(String fluxQuery){
        return client.getQueryApi().query(fluxQuery, org);
    }


    public void batchInsert(List<Point> pointList){
        WriteApiBlocking writeApi = client.getWriteApiBlocking();
        try {
            writeApi.writePoints(bucket, org, pointList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FluxTable> query(String flux){
        QueryApi queryApi = client.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux);
        return tables;
    }

    public QueryApi getQueryApi(){
        return client.getQueryApi();
    }

    @PreDestroy
    public void closeClient() {
        if (client != null) {
            client.close();
        }
    }
}