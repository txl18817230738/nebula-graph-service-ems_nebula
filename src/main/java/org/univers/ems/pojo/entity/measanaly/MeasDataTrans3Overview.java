package org.univers.ems.pojo.entity.measanaly;


import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class MeasDataTrans3Overview {
    private String voltLevel;

    private Integer count;

    private Integer loadSmall;

    private Integer loadMiddle;

    private Integer loadLarge;
}
