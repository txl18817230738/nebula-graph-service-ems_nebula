package org.univers.ems.pojo.entity.seallinfo;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class SeAllInfoTwoPortTransTable {
    private String name;

    private Double volt;

    private String fromSubName;

    private String fromBusName;

    private String fromPFlag;

    private Double fromPPimeas;

    private Double fromPPredict;

    private Double fromPResidual;

    private Double fromPPredictResidual;

    private String fromQFlag;

    private Double fromQQimeas;

    private Double fromQPredict;

    private Double fromQResidual;

    private Double fromQPredictResidual;

    private String toBusName;

    private String toPFlag;

    private Double toPPimeas;

    private Double toPPredict;

    private Double toPResidual;

    private Double toPPredictResidual;

    private String toQFlag;

    private Double toQQimeas;

    private Double toQPredict;

    private Double toQResidual;

    private Double toQPredictResidual;

}
