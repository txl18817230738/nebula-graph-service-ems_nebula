package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataCSOverview;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyCSOverviewConverterImpl implements EntityConverter<MeasDataCSOverview> {
    @Override
    public MeasDataCSOverview convert(ResultSet.Record record) {
        MeasDataCSOverview measDataCSOverview = new MeasDataCSOverview();
        measDataCSOverview.setVoltLevel(String.valueOf(record.get(0)));
        measDataCSOverview.setCount(Integer.valueOf(String.valueOf(record.get(1))));
        measDataCSOverview.setMeasLess0(Integer.valueOf(String.valueOf(record.get(2))));
        measDataCSOverview.setMeasEqual0(Integer.valueOf(String.valueOf(record.get(3))));
        measDataCSOverview.setMeasLarge0(Integer.valueOf(String.valueOf(record.get(4))));
        return measDataCSOverview;
    }
}
