package org.univers.ems.pojo.response;


import lombok.Data;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaArea;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaBus;
import org.univers.ems.pojo.entity.measanaly.MeasDataAreaOverview;
import org.univers.ems.pojo.entity.measanaly.MeasDataBusOverview;

import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaAreaResponse {

    private List<MeasDataAreaOverview> overviewList;

    private List<MeasDataAnaArea> tableData;

}
