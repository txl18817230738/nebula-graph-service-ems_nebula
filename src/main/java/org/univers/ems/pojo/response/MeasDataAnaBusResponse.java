package org.univers.ems.pojo.response;


import lombok.Data;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaBus;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaCP;
import org.univers.ems.pojo.entity.measanaly.MeasDataBusOverview;
import org.univers.ems.pojo.entity.measanaly.MeasDataCPOverview;

import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaBusResponse {

    private List<MeasDataBusOverview> overviewList;

    private List<MeasDataAnaBus> tableData;

}
