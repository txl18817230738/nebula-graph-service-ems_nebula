package org.univers.ems.pojo.entity.sld;


import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class SldCompensatorPCime {
    private String id;

    private String substation;

    private String name;

    private String type;

    private Double volt;

    private String island;

    private String point;

    private Double ratedCap;

    private Double ratedVolt;

    private Double q;

    private String node;

    private Double baseVoltage;
}
