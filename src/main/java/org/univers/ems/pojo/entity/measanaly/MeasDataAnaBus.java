package org.univers.ems.pojo.entity.measanaly;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaBus {
    private String name;

    private String subName;

    private Double volt;

    private Double voltWeight;

    private Double pWeight;

    private Double qWeight;

    private Double piMeas;

    private Double pZeroIn;

    private Double qZeroIn;
}
