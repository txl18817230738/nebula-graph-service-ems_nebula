package org.univers.ems.pojo.entity.measanaly;


import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataLoadOverview {
    private String voltLevel;

    private Integer count;

    private Double pos;

    private Double neg;

    private Double cal;

    private Integer loadNeg;

    private Integer loadSmall;

    private Integer loadMiddle;

    private Integer loadLarge;
}
