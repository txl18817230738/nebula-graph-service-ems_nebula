package org.univers.ems.pojo.entity.seallinfo;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class SeAllInfoUnitTable {
    private String name;

    private String subName;

    private String unitType;

    private String busName;

    private Double volt;

    private String pFlag;

    private Double pPimeas;

    private Double pPredictValue;

    private Double pPredictResidual;

    private Double pPredictError;

    private String qFlag;

    private Double qPimeas;

    private Double qPredictValue;

    private Double qPredictResidual;

    private Double qPredictError;

    private Double eleUsed;
}
