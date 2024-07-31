package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataCPOverview;
import org.univers.ems.pojo.entity.measanaly.MeasDataLoadOverview;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyCPOverviewConverterImpl implements EntityConverter<MeasDataCPOverview> {
    @Override
    public MeasDataCPOverview convert(ResultSet.Record record) {
        MeasDataCPOverview measDataCPOverview = new MeasDataCPOverview();
        measDataCPOverview.setVoltLevel(String.valueOf(record.get(0)));
        measDataCPOverview.setCount(Integer.valueOf(String.valueOf(record.get(1))));
        measDataCPOverview.setMeasLess0(Integer.valueOf(String.valueOf(record.get(2))));
        measDataCPOverview.setMeasEqual0(Integer.valueOf(String.valueOf(record.get(3))));
        measDataCPOverview.setMeasLarge0(Integer.valueOf(String.valueOf(record.get(4))));
        return measDataCPOverview;
    }
}
