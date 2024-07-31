package org.univers.ems.pojo.entity.measanaly;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaUnit {
    private String name;

    private String subName;

    private String subId;

    private String genType;

    private Double volt;

    private Double baseValue;

    private String voltMark;

    private Double ueMeas;

    private String pMark;

    private Double piMeas;

    private Double measOverload;

    private String qMark;

    private Double qiMeas;

    private String overloadType;

    private Double measNext;
}
