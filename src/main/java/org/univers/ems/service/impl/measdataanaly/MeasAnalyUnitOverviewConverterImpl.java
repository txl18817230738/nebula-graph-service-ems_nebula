package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaUnit;
import org.univers.ems.pojo.entity.measanaly.MeasDataUnitOverview;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyUnitOverviewConverterImpl implements EntityConverter<MeasDataUnitOverview> {
    @Override
    public MeasDataUnitOverview convert(ResultSet.Record record) {
        MeasDataUnitOverview measDataUnitOverview = new MeasDataUnitOverview();
        measDataUnitOverview.setGenType(String.valueOf(record.get(0)));
        measDataUnitOverview.setCount(Integer.valueOf(String.valueOf(record.get(1))));
        measDataUnitOverview.setCap("".equals(String.valueOf(record.get(2))) ? 0.001 : Double.valueOf(String.valueOf(record.get(2))));
        measDataUnitOverview.setPos("".equals(String.valueOf(record.get(4))) ? 0.001 : Double.valueOf(String.valueOf(record.get(4))));
        measDataUnitOverview.setNeg("".equals(String.valueOf(record.get(3))) ? 0.001 : Double.valueOf(String.valueOf(record.get(3))));
        measDataUnitOverview.setOverLoadLess(Integer.valueOf(String.valueOf(record.get(5))));
        measDataUnitOverview.setOverLoadMiddle(Integer.valueOf(String.valueOf(record.get(6))));
        measDataUnitOverview.setOverLoadLarge(Integer.valueOf(String.valueOf(record.get(7))));
        measDataUnitOverview.setOverLoadOverload(Integer.valueOf(String.valueOf(record.get(8))));
        return measDataUnitOverview;
    }
}
