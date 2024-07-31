package org.univers.ems.influxdb.model.restlt;


import lombok.Data;

@Data
public class InfluxResult {
    /**
     * 区域Id
     */
    private String areaId;
    /**
     * 有功功率
     */
    private String power;
    /**
     * 时间戳
     */
    private String time;
}
