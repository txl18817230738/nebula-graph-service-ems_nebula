package org.univers.ems.pojo.response;


import lombok.Data;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaUnit;
import org.univers.ems.pojo.entity.measanaly.MeasDataUnitOverview;

import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaUnitResponse {

    /*private Integer fireTotalCount;

    private Integer fireLessCount;

    private Integer fireMiddleCount;

    private Integer fireLargeCount;

    private Integer fireOverloadCount;

    private Double fireTotalCapCount;

    private Double firePosEleGenTotal;

    private Double fireNegEleGenTotal;

    private Double fireMeasGenTotal;

    private Integer waterTotalCount;

    private Integer waterLessCount;

    private Integer waterMiddleCount;

    private Integer waterLargeCount;

    private Integer waterOverloadCount;

    private Double waterTotalCapCount;

    private Double waterPosEleGenTotal;

    private Double waterNegEleGenTotal;

    private Double waterMeasGenTotal;

    private Integer windTotalCount;

    private Integer windLessCount;

    private Integer windMiddleCount;

    private Integer windLargeCount;

    private Integer windOverloadCount;

    private Double windTotalCapCount;

    private Double windPosEleGenTotal;

    private Double windNegEleGenTotal;

    private Double windMeasGenTotal;

    private Integer nuclearTotalCount;

    private Integer nuclearLessCount;

    private Integer nuclearMiddleCount;

    private Integer nuclearLargeCount;

    private Integer nuclearOverloadCount;

    private Double nuclearTotalCapCount;

    private Double nuclearPosEleGenTotal;

    private Double nuclearNegEleGenTotal;

    private Double nuclearMeasGenTotal;

    private Integer storageTotalCount;

    private Integer storageLessCount;

    private Integer storageMiddleCount;

    private Integer storageLargeCount;

    private Integer storageOverloadCount;

    private Double storageTotalCapCount;

    private Double storagePosEleGenTotal;

    private Double storageNegEleGenTotal;

    private Double storageMeasGenTotal;

    private Integer otherTotalCount;

    private Integer otherLessCount;

    private Integer otherMiddleCount;

    private Integer otherLargeCount;

    private Integer otherOverloadCount;

    private Double otherTotalCapCount;

    private Double otherPosEleGenTotal;

    private Double otherNegEleGenTotal;

    private Double otherMeasGenTotal;*/
    private List<MeasDataUnitOverview> overviewList;

    private List<MeasDataAnaUnit> tableData;

}
