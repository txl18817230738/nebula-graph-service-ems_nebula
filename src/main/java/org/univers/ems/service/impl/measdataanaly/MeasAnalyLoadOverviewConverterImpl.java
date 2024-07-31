package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataLoadOverview;
import org.univers.ems.pojo.entity.measanaly.MeasDataUnitOverview;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyLoadOverviewConverterImpl implements EntityConverter<MeasDataLoadOverview> {
    @Override
    public MeasDataLoadOverview convert(ResultSet.Record record) {
        MeasDataLoadOverview measDataLoadOverview = new MeasDataLoadOverview();
        measDataLoadOverview.setVoltLevel(String.valueOf(record.get(0)));
        measDataLoadOverview.setCount(Integer.valueOf(String.valueOf(record.get(1))));
        measDataLoadOverview.setPos("".equals(String.valueOf(record.get(2))) ? 0.001 : Double.valueOf(String.valueOf(record.get(2))));
        measDataLoadOverview.setNeg("".equals(String.valueOf(record.get(4))) ? 0.001 : Double.valueOf(String.valueOf(record.get(4))));
        measDataLoadOverview.setCal("".equals(String.valueOf(record.get(3))) ? 0.001 : Double.valueOf(String.valueOf(record.get(3))));
        measDataLoadOverview.setLoadNeg(Integer.valueOf(String.valueOf(record.get(5))));
        measDataLoadOverview.setLoadSmall(Integer.valueOf(String.valueOf(record.get(6))));
        measDataLoadOverview.setLoadMiddle(Integer.valueOf(String.valueOf(record.get(7))));
        measDataLoadOverview.setLoadLarge(Integer.valueOf(String.valueOf(record.get(8))));
        return measDataLoadOverview;
    }
}
