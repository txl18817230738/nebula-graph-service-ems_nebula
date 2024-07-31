package org.univers.ems.pojo.entity.sld;

import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class SldSubstationCime {
    private String id;

    private String name;

    private String controlArea;

    private String volt;

    private String type;

    private Integer aclineDotCount;

    private Integer transTwoCount;

    private Integer transThreeCount;

    private Integer unitCount;

    private Integer loadCount;

    private Integer busCount;

    private Integer cpCount;

    private Integer csCount;

    private Integer breakerCount;

    private Integer disCount;
}
