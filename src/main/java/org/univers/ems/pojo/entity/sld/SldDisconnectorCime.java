package org.univers.ems.pojo.entity.sld;


import lombok.Data;

/**
 * @author jie.xi
 */
@Data
public class SldDisconnectorCime {
    private String id;

    private String substation;

    private String name;

    private String volt;

    private String point;

    private String inode;

    private String jnode;
}
