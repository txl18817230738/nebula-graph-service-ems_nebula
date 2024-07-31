package org.univers.ems.pojo.response;


import lombok.Data;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaLoad;
import org.univers.ems.pojo.entity.measanaly.MeasDataLoadOverview;

import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaLoadResponse {

    private List<MeasDataLoadOverview> overviewList;

    private List<MeasDataAnaLoad> tableData;

}
