package org.univers.ems.pojo.response;

import lombok.Data;
import org.univers.ems.pojo.entity.sld.SldSubstationCime;

import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class OverviewTableSubstation {
    private Integer totalCount500;

    private Integer totalCount220;

    private Integer totalCount110;

    private Integer totalCount35;

    private Integer totalCount10;

    private Integer totalCountOther;

    private Integer totalCount;

    private Integer islandCount500;

    private Integer islandCount220;

    private Integer islandCount110;

    private Integer islandCount35;

    private Integer islandCount10;

    private Integer islandCountOther;

    private Integer islandCount;

    private Integer noIslandCount500;

    private Integer noIslandCount220;

    private Integer noIslandCount110;

    private Integer noIslandCount35;

    private Integer noIslandCount10;

    private Integer noIslandCountOther;

    private Integer noIslandCount;

    private Integer loadCount500;

    private Integer loadCount220;

    private Integer loadCount110;

    private Integer loadCount35;

    private Integer loadCount10;

    private Integer loadCountOther;

    private Integer loadCount;

    private Integer noLoadCount500;

    private Integer noLoadCount220;

    private Integer noLoadCount110;

    private Integer noLoadCount35;

    private Integer noLoadCount10;

    private Integer noLoadCountOther;

    private Integer noLoadCount;

    private Integer unitCount500;

    private Integer unitCount220;

    private Integer unitCount110;

    private Integer unitCount35;

    private Integer unitCount10;

    private Integer unitCountOther;

    private Integer unitCount;

    private Integer noUnitCount500;

    private Integer noUnitCount220;

    private Integer noUnitCount110;

    private Integer noUnitCount35;

    private Integer noUnitCount10;

    private Integer noUnitCountOther;

    private Integer noUnitCount;

    private Integer waterCount500;

    private Integer waterCount220;

    private Integer waterCount110;

    private Integer waterCount35;

    private Integer waterCount10;

    private Integer waterCountOther;

    private Integer waterCount;

    private Integer fireCount500;

    private Integer fireCount220;

    private Integer fireCount110;

    private Integer fireCount35;

    private Integer fireCount10;

    private Integer fireCountOther;

    private Integer fireCount;

    private Integer windCount500;

    private Integer windCount220;

    private Integer windCount110;

    private Integer windCount35;

    private Integer windCount10;

    private Integer windCountOther;

    private Integer windCount;

    private Integer storeCount500;

    private Integer storeCount220;

    private Integer storeCount110;

    private Integer storeCount35;

    private Integer storeCount10;

    private Integer storeCountOther;

    private Integer storeCount;

    private Integer pshCount500;

    private Integer pshCount220;

    private Integer pshCount110;

    private Integer pshCount35;

    private Integer pshCount10;

    private Integer pshCountOther;

    private Integer pshCount;

    private Integer eleCount500;

    private Integer eleCount220;

    private Integer eleCount110;

    private Integer eleCount35;

    private Integer eleCount10;

    private Integer eleCountOther;

    private Integer eleCount;

    private Integer nuclearCount500;

    private Integer nuclearCount220;

    private Integer nuclearCount110;

    private Integer nuclearCount35;

    private Integer nuclearCount10;

    private Integer nuclearCountOther;

    private Integer nuclearCount;

    private Integer pvCount500;

    private Integer pvCount220;

    private Integer pvCount110;

    private Integer pvCount35;

    private Integer pvCount10;

    private Integer pvCountOther;

    private Integer pvCount;

    private Integer tCount500;

    private Integer tCount220;

    private Integer tCount110;

    private Integer tCount35;

    private Integer tCount10;

    private Integer tCountOther;

    private Integer tCount;

    private List<SldSubstationCime> substationList;
}
