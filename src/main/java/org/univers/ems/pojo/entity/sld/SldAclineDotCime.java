package org.univers.ems.pojo.entity.sld;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class SldAclineDotCime {
    private String id;

    private String name;

    private String substation;

    private Double volt;

    private String island;

    private String point;

    private Double p;

    private Double q;

    private String node;
}
