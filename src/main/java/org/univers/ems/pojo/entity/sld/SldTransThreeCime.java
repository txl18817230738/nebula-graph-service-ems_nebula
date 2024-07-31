package org.univers.ems.pojo.entity.sld;

import lombok.Data;

@Data
public class SldTransThreeCime {
    private String id;

    private String substation;

    private String name;

    private String type;

    private Double volt;

    private String island;

    private String point;

    private Double p;

    private Double q;

    private String node;

    private Double rStar;

    private Double xStar;

    private String t;

    private String baseVoltage;
}
