package org.univers.ems.pojo.entity.measanaly;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaLoad {
    private String name;

    private String subName;

    private String bus;

    private Double volt;

    private String pMark;

    private Double piMeas;

    private Double measNext;

    private String qMark;

    private Double qiMeas;
}
