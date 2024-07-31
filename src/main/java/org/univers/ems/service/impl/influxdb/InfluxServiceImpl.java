package org.univers.ems.service.impl.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.univers.ems.exception.FileParsingException;
import org.univers.ems.influxdb.config.InfluxBean;
import org.univers.ems.pojo.entity.influx.InsertParams;
import org.univers.ems.influxdb.model.restlt.InfluxResult;
import org.univers.ems.service.InfluxService;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
  * description: 时序数据库Impl
  */
@Service
@Slf4j
public class InfluxServiceImpl implements InfluxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfluxServiceImpl.class);
    private int batchSize = 10000;

    private int pageSize = 1000;

    @Resource
    private InfluxBean influxBean;
    @Override
    public void insert(InsertParams insertParams) {
        ZonedDateTime nowInChina = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        Instant nowInUTC = nowInChina.toInstant();
        insertParams.setTime(nowInUTC);
        influxBean.write(insertParams);
        influxBean.writeData();
    }

    @Override
    public List<InfluxResult> queue(){
        // 下面两个 private 方法 赋值给 list 查询对应的数据
        List<FluxTable> list = queryInfluxAll();
        List<InfluxResult> results = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getRecords().size(); j++) {
                InfluxResult influxResult = new InfluxResult();
                influxResult.setAreaId(list.get(i).getRecords().get(j).getValues().get("areaId").toString());
                influxResult.setPower(list.get(i).getRecords().get(j).getValues().get("power").toString());
                Instant specificUTC = Instant.parse(list.get(i).getRecords().get(j).getValues().get("_time").toString());
                ZonedDateTime specificBeijing = specificUTC.atZone(ZoneId.of("Asia/Shanghai"));
                influxResult.setTime(specificBeijing.format(formatter));
                System.err.println(list.get(i).getRecords().get(j).getValues().toString());
                results.add(influxResult);
            }
        }
        return results;
    }

    @Override
    public void insertMeas(String filePath) throws FileParsingException {
        if (!Files.exists(Paths.get(filePath))) {
            throw new FileParsingException("File does not exist at specified path: " + filePath);
        }
        // 创建一个DateTimeFormatter格式化器，用于解析断面时间
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
                .toFormatter();
        List<Point> points = new ArrayList<>(batchSize);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
            // 预编译正则表达式
            Pattern pattern = Pattern.compile(regex);
            // 创建一个新的 DateTimeFormatter 来生成不含秒和毫秒的时间字符串
            DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                // 根据正则表达式分割字符串
                String[] parts = pattern.split(line);
                String time = parts[parts.length - 1];
                // 将断面时间转换为LocalDateTime
                LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
                // 默认使用系统默认时区，将LocalDateTime转换为Instant
                Instant instantTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                // 使用新的格式化程序来格式化 LocalDateTime 对象
                String sectionTime = localDateTime.format(targetFormat);
                Point point = Point.measurement("history_meas")
                        .addTag("measId",parts[0])
                        .addTag("sectionTime",sectionTime)
                        .addField("devName",parts[1])
                        .addField("devType",parts[2])
                        .addField("subId",parts[3])
                        .addField("subName",parts[4])
                        .addField("p",parts[5])
                        .addField("pFlag",parts[6])
                        .addField("q",parts[7])
                        .addField("qFlag",parts[8])
                        .time(instantTime, WritePrecision.NS);
                points.add(point);
                // 当达到批量大小时写入并清空列表
                if (points.size() >= batchSize) {
                    LOGGER.info("插入条数:{}",points.size());
                    influxBean.batchInsert(points);
                    points.clear(); // 重置点的集合以用于下一个批次
                }
            }
            // 确保最后一批数据也被写入
            if (!points.isEmpty()) {
                LOGGER.info("插入条数:{}",points.size());
                influxBean.batchInsert(points);
            }
        } catch (IOException e) {
            throw new FileParsingException("Error while reading the file", e);
        }
    }

    @Override
    public void queryMeas() {
    }

    @Override
    public void insertDiscrete(String filePath) throws FileParsingException {
        if (!Files.exists(Paths.get(filePath))) {
            LOGGER.warn("File does not exist at specified path: {}", filePath);
            throw new FileParsingException("File does not exist at specified path: " + filePath);
        }
        // 创建一个DateTimeFormatter格式化器,用于解析每一条记录的时间
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
                .toFormatter();
        List<Point> points = new ArrayList<>(batchSize);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            // 正则表达式，用于匹配带引号或不带引号的CSV字段
            String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
            // 预编译正则表达式
            Pattern pattern = Pattern.compile(regex);
            // 创建一个新的 DateTimeFormatter 来生成不含秒和毫秒的时间字符串
            DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                // 根据正则表达式分割字符串
                String[] parts = pattern.split(line);
                String time = parts[parts.length-1];
                // 将字符串转换为LocalDateTime
                LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
                // 默认使用系统默认时区，将LocalDateTime转换为Instant
                Instant instantTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                // 使用新的格式化程序来格式化 LocalDateTime 对象
                String sectionTime = localDateTime.format(targetFormat);
                Point point = Point.measurement("history_discrete")
                        .addTag("discreteId",parts[0])
                        .addTag("sectionTime",sectionTime)
                        .addField("devName",parts[1])
                        .addField("devType",parts[2])
                        .addField("subId",parts[3])
                        .addField("subName",parts[4])
                        .addField("flag",parts[5])
                        .addField("status",parts[6])
                        .addField("off",parts[7])
                        .addField("time", String.valueOf(instantTime));
                points.add(point);
                // 当达到批量大小时写入并清空列表
                if (points.size() >= batchSize) {
                    LOGGER.info("插入条数:{}",points.size());
                    influxBean.batchInsert(points);
                    points.clear(); // 重置点的集合以用于下一个批次
                }
            }
            // 确保最后一批数据也被写入
            if (!points.isEmpty()) {
                LOGGER.info("插入条数:{}",points.size());
                influxBean.batchInsert(points);
            }
        } catch (IOException e) {
            LOGGER.warn("Error while reading the file");
            throw new FileParsingException("Error while reading the file", e);
        }
    }

    @Override
    public void queryDiscrete() {
    }



    /**
      * description: 查询一小时内的InsertParams所有数据
      * date: 2022/1/21 13:44
      * author: zhouhong
      * @param  * @param null
      * @return
      */
    private List<FluxTable> queryInfluxAll(){
        String query = " from(bucket: \"ems_time_series_data\")" +
                "  |> range(start: -3h, stop: now())" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"com_history_p_meas\")" +
                "  |> pivot( rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\" )";
        System.out.println(query);
        return influxBean.queryTable(query);
    }
    /**
      * description: 根据某一个字段的值过滤(查询 用电量 energyUsed 为 322 的那条记录)
      * date: 2022/1/21 12:44
      * author: zhouhong
      * @param  * @param null
      * @return
      */
    public List<FluxTable> queryFilterByEnergyUsed(){
        String query = " from(bucket: \"ems_time_series_data\")" +
                "  |> range(start: -60m, stop: now())" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"influx_test\")" +
                "  |> filter(fn: (r) => r[\"energyUsed\"] == \"1\")" +
                "  |> pivot( rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\" )";
        return influxBean.queryTable(query);
    }


    private List<FluxTable> queryInfluxAllTest(){
        String query = " from(bucket: \"ems_time_series_data\")" +
                "  |> range(start: -3h, stop: now())" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"temperature\")" +
                "  |> pivot( rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\" )";
        System.out.println(query);
        return influxBean.queryTable(query);
    }

    @Override
    public void writeMeasCSV(String tagValue) {

        String filePath = "C:\\work\\桌面整理\\nebula改造相关\\meas_v3.csv";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(tagValue, formatter);
        // 将LocalDateTime转换为ZonedDateTime，时区为CST (UTC+8)
        ZonedDateTime cstDateTime = ZonedDateTime.of(dateTime, ZoneId.of("Asia/Shanghai"));

        // 将CST时间转换为UTC时间
        ZonedDateTime utcDateTime = cstDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        // 计算开始时间和结束时间
        ZonedDateTime startTime = utcDateTime.minusMinutes(2);
        ZonedDateTime endTime = utcDateTime.plusMinutes(2);

        // 转换为RFC3339格式
        DateTimeFormatter rfc3339Formatter = DateTimeFormatter.ISO_DATE_TIME;
        String startStr = startTime.format(rfc3339Formatter).split("\\[")[0];;
        String endStr = endTime.format(rfc3339Formatter).split("\\[")[0];;

        String fluxTemplate = "from(bucket: \"ems_history_data\") " +
                "|> range(start: %s, stop: %s)" +
                "|> filter(fn: (r) => r[\"_measurement\"] == \"history_meas\")" +
                "|> filter(fn: (r) => r[\"sectionTime\"] == \"%s\") "+
                "|> pivot( rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\" )";
        String flux = String.format(fluxTemplate, startStr, endStr, tagValue, pageSize);

        Path path = Paths.get(filePath);
        QueryApi queryApi = influxBean.getQueryApi();
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedWriter finalWriter = writer;
        // 正则表达式，用于匹配带引号或不带引号的CSV字段
        String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        // 预编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        queryApi.queryRaw(flux, (cancellable, line) -> {

            // 根据正则表达式分割字符串
            String[] parts = pattern.split(line);
            if(parts.length == 17 && !"table".equals(parts[2])){
                ZonedDateTime utcTime = ZonedDateTime.parse(parts[5], rfc3339Formatter);
                ZonedDateTime cstTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai"));
                String dataTime = cstTime.format(rfc3339Formatter).split("\\+")[0].replace("T"," ");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(parts[7])//measId
                        .append(",")
                        .append(parts[9])//devName
                        .append(",")
                        .append(parts[10])//devType
                        .append(",")
                        .append(parts[11])//p
                        .append(",")
                        .append(parts[12])//paflag
                        .append(",")
                        .append(parts[13])//q
                        .append(",")
                        .append(parts[14])//qflag
                        .append(",")
                        .append(parts[15])//subId
                        .append(",")
                        .append(parts[16])//subName
                        .append(",")
                        .append(dataTime).toString();//time
                System.out.println(stringBuilder);
                try {
                    finalWriter.write(String.valueOf(stringBuilder));
                    finalWriter.newLine();
                    finalWriter.flush();
                } catch (IOException e) {
                    cancellable.cancel();
                    throw new RuntimeException("Error writing to CSV", e);
                }
            }else {
                System.out.println(line);
            }
        });

    }
}
