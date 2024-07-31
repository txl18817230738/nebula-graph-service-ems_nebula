package org.univers.ems.pojo.entity.influx;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

import java.time.Instant;

/**
 * description: 时序数据库添加参数
 */
@Data
@Measurement(name = "com_history_p_meas")
public class InsertParams {
    /**
      * 区域Id
      */
    @Column(tag = true)
    private Integer areaId;

    /**
     * 有功功率
     */
    @Column
    private Double  power;

    /**
     * 时间戳
     */
    @Column(timestamp = true)
    private Instant time;
}
