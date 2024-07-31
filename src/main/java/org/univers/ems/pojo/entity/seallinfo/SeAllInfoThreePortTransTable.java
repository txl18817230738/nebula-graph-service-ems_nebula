package org.univers.ems.pojo.entity.seallinfo;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class SeAllInfoThreePortTransTable {
    private String name;

    private Double volt;

    private String subName;

    private String fromBus;

    private String toBus;

    private String pFlag;

    private Double fromPPimeas;

    private Double fromPPredict;

    private Double fromPResidual;

    private Double fromPPredictResidual;

    private String qFlag;

    private Double fromQQimeas;

    private Double fromQPredict;

    private Double fromQResidual;

    private Double fromQPredictResidual;
}
