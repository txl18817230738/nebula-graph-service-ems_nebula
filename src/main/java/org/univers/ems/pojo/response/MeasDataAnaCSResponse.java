package org.univers.ems.pojo.response;


import lombok.Data;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaCS;
import org.univers.ems.pojo.entity.measanaly.MeasDataCSOverview;

import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class MeasDataAnaCSResponse {

    private List<MeasDataCSOverview> overviewList;

    private List<MeasDataAnaCS> tableData;

}
