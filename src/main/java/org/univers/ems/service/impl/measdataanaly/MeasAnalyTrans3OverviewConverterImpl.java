package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataCPOverview;
import org.univers.ems.pojo.entity.measanaly.MeasDataTrans3Overview;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyTrans3OverviewConverterImpl implements EntityConverter<MeasDataTrans3Overview> {
    @Override
    public MeasDataTrans3Overview convert(ResultSet.Record record) {
        MeasDataTrans3Overview measDataTrans3Overview = new MeasDataTrans3Overview();
        measDataTrans3Overview.setVoltLevel(String.valueOf(record.get(0)));
        measDataTrans3Overview.setCount(Integer.valueOf(String.valueOf(record.get(1))));
        measDataTrans3Overview.setLoadSmall(Integer.valueOf(String.valueOf(record.get(2))));
        measDataTrans3Overview.setLoadMiddle(Integer.valueOf(String.valueOf(record.get(3))));
        measDataTrans3Overview.setLoadLarge(Integer.valueOf(String.valueOf(record.get(4))));
        return measDataTrans3Overview;
    }
}
