package org.univers.ems.pojo.entity.measanaly;


import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataSubstationOverview {
    private String voltLevel;

    private Integer count;

    private Integer measCoverageLess50;

    private Integer measCoverageLess80;

    private Integer measCoverageLarge80;
}
