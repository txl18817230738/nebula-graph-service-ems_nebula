package org.univers.ems.pojo.entity.measanaly;


import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataBusOverview {
    private String voltLevel;

    private Integer count;

    private Integer pZeroIn;

    private Integer qZeroIn;

    private Integer noZeroIn;
}
