package org.univers.ems.pojo.entity.measanaly;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaSubstation {
    private String name;

    private String area;

    private Double totalCoverage;

    private Double volt;

    private Integer aclineDotCount;

    private Integer aclineDotMeasCount;

    private Integer trans2Count;

    private Integer trans2MeasCount;

    private Integer trans3Count;

    private Integer trans3MeasCount;

    private Integer unitCount;

    private Integer unitMeasCount;

    private Integer loadCount;

    private Integer loadMeasCount;

    private Integer busCount;

    private Integer busMeasCount;

}
