package org.univers.ems.pojo.entity.measanaly;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaTrans3 {
    private String name;

    private Double measOverload;

    private String overloadType;

    private String subName;

    private Double volt;

    private String fromBusName;

    private String toBusName;

    private String pMark;

    private Double fromPMeas;

    private String qMark;

    private Double fromQMeas;

    private Double pMeasWeight;

}
