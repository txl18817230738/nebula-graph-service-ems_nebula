package org.univers.ems.pojo.entity.measanaly;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaArea {
    private String name;

    private Double induStandardCoverage;

    private Double totalCoverage;

    private Double linePCoverageRate;

    private Double lineQCoverageRate;

    private Double unitPCoverageRate;

    private Double unitQCoverageRate;

    private Double loadPCoverageRate;

    private Double loadQCoverageRate;

    private Double trans2PCoverageRate;

    private Double trans2QCoverageRate;

    private Double trans3PCoverageRate;

    private Double trans3QCoverageRate;

    private Double cpCoverageRate;

    private Double busMeasCoverageRate;
}
