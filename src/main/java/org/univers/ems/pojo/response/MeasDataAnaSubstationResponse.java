package org.univers.ems.pojo.response;


import lombok.Data;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaSubstation;
import org.univers.ems.pojo.entity.measanaly.MeasDataSubstationOverview;

import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaSubstationResponse {

    private List<MeasDataSubstationOverview> overviewList;

    private List<MeasDataAnaSubstation> tableData;
}
