package org.univers.ems.pojo.entity.sld;


import lombok.Data;

@Data
public class SldCompensatorSCime {
    private String id;

    private String substation;

    private String name;

    private Double volt;

    private Double zk;

    private String island;

    private String point;

    private Double ip;

    private Double iq;

    private Double jp;

    private Double jq;

    private String inode;

    private String jnode;

    private Double cszk;
}
