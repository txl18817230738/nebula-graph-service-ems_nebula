package org.univers.ems.pojo.entity.measanaly;


import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataCPOverview {
    private String voltLevel;

    private Integer count;

    private Integer measLess0;

    private Integer measEqual0;

    private Integer measLarge0;
}
