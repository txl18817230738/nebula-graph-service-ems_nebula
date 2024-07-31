package org.univers.ems.pojo.entity.influx;


import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

import java.time.Instant;

/**
 * @author jie.xi
 */
@Data
@Measurement(name = "history_meas")
public class Meas {

    @Column(tag = true)
    private String measId;

    @Column
    private String devName;

    @Column
    private String devType;

    @Column
    private String subId;

    @Column
    private String subName;

    @Column
    private String p;

    @Column
    private String pFlag;

    @Column
    private String q;

    @Column
    private String qFlag;

    @Column(timestamp = true)
    private Instant time;
}
