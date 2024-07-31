package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataBusOverview;
import org.univers.ems.pojo.entity.measanaly.MeasDataCPOverview;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyBusOverviewConverterImpl implements EntityConverter<MeasDataBusOverview> {
    @Override
    public MeasDataBusOverview convert(ResultSet.Record record) {
        MeasDataBusOverview measDataBusOverview = new MeasDataBusOverview();
        measDataBusOverview.setVoltLevel(String.valueOf(record.get(0)));
        measDataBusOverview.setCount(Integer.valueOf(String.valueOf(record.get(1))));
        measDataBusOverview.setPZeroIn(Integer.valueOf(String.valueOf(record.get(2))));
        measDataBusOverview.setQZeroIn(Integer.valueOf(String.valueOf(record.get(3))));
        measDataBusOverview.setNoZeroIn(Integer.valueOf(String.valueOf(record.get(4))));
        return measDataBusOverview;
    }
}
