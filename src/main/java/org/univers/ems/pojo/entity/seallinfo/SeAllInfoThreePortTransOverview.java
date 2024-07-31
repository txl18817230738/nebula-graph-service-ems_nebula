package org.univers.ems.pojo.entity.seallinfo;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class SeAllInfoThreePortTransOverview {
    private String voltageLevel;

    private Integer count;

    private Integer pPredictLessValue;

    private Integer pPredictLargeValue;

    private Integer qPredictLessValue;

    private Integer qPredictLargeValue;
}
