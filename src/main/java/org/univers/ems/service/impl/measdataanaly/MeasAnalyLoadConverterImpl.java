package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaLoad;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaUnit;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyLoadConverterImpl implements EntityConverter<MeasDataAnaLoad> {
    @Override
    public MeasDataAnaLoad convert(ResultSet.Record record) {
        MeasDataAnaLoad measDataAnaLoad = new MeasDataAnaLoad();
        measDataAnaLoad.setName(String.valueOf(record.get(0)));
        measDataAnaLoad.setSubName(String.valueOf(record.get(1)));
        measDataAnaLoad.setBus(String.valueOf(record.get(2)));
        measDataAnaLoad.setVolt("".equals(String.valueOf(record.get(3))) ? 0.001 : Double.valueOf(String.valueOf(record.get(3))));
        measDataAnaLoad.setPMark(String.valueOf(record.get(4)));
        measDataAnaLoad.setPiMeas("".equals(String.valueOf(record.get(5))) ? 0.001 : Double.valueOf(String.valueOf(record.get(5))));
        measDataAnaLoad.setMeasNext("".equals(String.valueOf(record.get(6))) ? 0.001 : Double.valueOf(String.valueOf(record.get(6))));
        measDataAnaLoad.setQMark(String.valueOf(record.get(7)));
        measDataAnaLoad.setQiMeas("".equals(String.valueOf(record.get(8))) ? 0.001 : Double.valueOf(String.valueOf(record.get(8))));
        return measDataAnaLoad;
    }
}
