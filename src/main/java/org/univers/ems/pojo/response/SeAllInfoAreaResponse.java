package org.univers.ems.pojo.response;

import lombok.Data;
import org.univers.ems.pojo.entity.seallinfo.SeAllInfoAreaTable;
import org.univers.ems.pojo.entity.seallinfo.SeAllInfoAreaOverview;


import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class SeAllInfoAreaResponse {
    private List<SeAllInfoAreaOverview> overviewList;

    private List<SeAllInfoAreaTable> tableData;
}
