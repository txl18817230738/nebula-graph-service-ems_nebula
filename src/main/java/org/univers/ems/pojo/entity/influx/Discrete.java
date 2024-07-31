package org.univers.ems.pojo.entity.influx;


import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class Discrete {
    private String discreteId;

    private String devName;

    private String devType;

    private String subName;

    private String subId;

    private Integer flag;

    private Integer status;
}
