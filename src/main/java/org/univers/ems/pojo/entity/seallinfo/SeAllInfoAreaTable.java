package org.univers.ems.pojo.entity.seallinfo;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class SeAllInfoAreaTable {

    private String name;

    private Double totalPassRate;

    private Double linePPassRate;

    private Double lineQPassRate;

    private Double unitPPassRate;

    private Double unitQPassRate;

    private Double loadPPassRate;

    private Double loadQPassRate;

    private Double trans2PPassRate;

    private Double trans2QPassRate;

    private Double trans3PPassRate;

    private Double trans3QPassRate;

    private Double busVoltPassRate;
}
