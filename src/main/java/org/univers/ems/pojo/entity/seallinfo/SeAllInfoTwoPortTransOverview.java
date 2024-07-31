package org.univers.ems.pojo.entity.seallinfo;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class SeAllInfoTwoPortTransOverview {
    private String voltageLevel;

    private Integer count;

    private Integer fromLessValue;

    private Integer fromLargeValue;

    private Integer toLessValue;

    private Integer toLargeValue;
}
