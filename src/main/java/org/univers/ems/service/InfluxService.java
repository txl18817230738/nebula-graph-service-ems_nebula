package org.univers.ems.service;


import org.univers.ems.exception.FileParsingException;
import org.univers.ems.pojo.entity.influx.InsertParams;
import org.univers.ems.influxdb.model.restlt.InfluxResult;

import java.util.List;

/**
 * description: 时序数据库service
 * @author jie.xi
 */
public interface InfluxService {

    /**
      * description: InfluxDB插入
      */
    void insert(InsertParams insertParams);
    /**
     * description: 查询
     */
    List<InfluxResult> queue();

    void insertMeas(String filePath) throws FileParsingException;

    void queryMeas();

    void insertDiscrete(String filePath) throws FileParsingException;

    void queryDiscrete();

    void writeMeasCSV(String tagValue);
}
