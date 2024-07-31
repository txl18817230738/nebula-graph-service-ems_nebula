package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataSubstationOverview;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalySubstationOverviewConverterImpl implements EntityConverter<MeasDataSubstationOverview> {
    @Override
    public MeasDataSubstationOverview convert(ResultSet.Record record) {
        MeasDataSubstationOverview measDataSubstationOverview = new MeasDataSubstationOverview();
        measDataSubstationOverview.setVoltLevel(String.valueOf(record.get(0)));
        measDataSubstationOverview.setCount(Integer.valueOf(String.valueOf(record.get(1))));
        measDataSubstationOverview.setMeasCoverageLess50(Integer.valueOf(String.valueOf(record.get(2))));
        measDataSubstationOverview.setMeasCoverageLess80(Integer.valueOf(String.valueOf(record.get(3))));
        measDataSubstationOverview.setMeasCoverageLarge80(Integer.valueOf(String.valueOf(record.get(4))));
        return measDataSubstationOverview;
    }
}
