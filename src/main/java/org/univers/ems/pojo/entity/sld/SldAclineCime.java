package org.univers.ems.pojo.entity.sld;


import lombok.Data;

@Data
public class SldAclineCime {
    private String id;

    private String iSubstation;

    private String jSubstation;

    private String name;

    private Double volt;

    private String island;

    private Double r;

    private Double x;

    private Double b;

    private String iname;

    private String jname;

    private String ioff;

    private String joff;

    private Double ip;

    private Double iq;

    private Double jp;

    private Double jq;

    private String inode;

    private String jnode;

    private Double lineR;

    private Double lineX;

    private Double lineB;
}
