package org.univers.ems.pojo.response;


import lombok.Data;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaCP;
import org.univers.ems.pojo.entity.measanaly.MeasDataCPOverview;

import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaCPResponse {

    private List<MeasDataCPOverview> overviewList;

    private List<MeasDataAnaCP> tableData;

}
