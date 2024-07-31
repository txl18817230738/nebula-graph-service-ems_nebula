package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataAreaOverview;
import org.univers.ems.pojo.entity.measanaly.MeasDataLoadOverview;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyAreaOverviewConverterImpl implements EntityConverter<MeasDataAreaOverview> {
    @Override
    public MeasDataAreaOverview convert(ResultSet.Record record) {
        MeasDataAreaOverview measDataAreaOverview = new MeasDataAreaOverview();
        measDataAreaOverview.setLinePMeas("".equals(String.valueOf(record.get(0))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(0))));
        measDataAreaOverview.setLineQMeas("".equals(String.valueOf(record.get(1))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(1))));
        measDataAreaOverview.setUnitPMeas("".equals(String.valueOf(record.get(2))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(2))));
        measDataAreaOverview.setUnitQMeas("".equals(String.valueOf(record.get(3))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(3))));
        measDataAreaOverview.setLoadPMeas("".equals(String.valueOf(record.get(4))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(4))));
        measDataAreaOverview.setLoadQMeas("".equals(String.valueOf(record.get(5))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(5))));
        measDataAreaOverview.setTrans2PMeas("".equals(String.valueOf(record.get(6))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(6))));
        measDataAreaOverview.setTrans2QMeas("".equals(String.valueOf(record.get(7))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(7))));
        measDataAreaOverview.setTrans3PMeas("".equals(String.valueOf(record.get(8))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(8))));
        measDataAreaOverview.setTrans3QMeas("".equals(String.valueOf(record.get(9))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(9))));
        measDataAreaOverview.setCpQMeas("".equals(String.valueOf(record.get(10))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(10))));
        measDataAreaOverview.setBusVoltMeas("".equals(String.valueOf(record.get(11))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(11))));
        return measDataAreaOverview;
    }
}
