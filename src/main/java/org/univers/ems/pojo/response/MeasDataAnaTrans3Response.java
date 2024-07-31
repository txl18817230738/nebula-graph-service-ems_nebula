package org.univers.ems.pojo.response;


import lombok.Data;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaBus;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaTrans3;
import org.univers.ems.pojo.entity.measanaly.MeasDataBusOverview;
import org.univers.ems.pojo.entity.measanaly.MeasDataTrans3Overview;

import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaTrans3Response {

    private List<MeasDataTrans3Overview> overviewList;

    private List<MeasDataAnaTrans3> tableData;

}
