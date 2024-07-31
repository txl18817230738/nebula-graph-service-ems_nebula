package org.univers.ems.pojo.entity.measanaly;


import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataUnitOverview {
    private String genType;

    private Integer count;

    private Double cap;

    private Double pos;

    private Double neg;

    private Integer overLoadLess;

    private Integer overLoadMiddle;

    private Integer overLoadLarge;

    private Integer overLoadOverload;
}
