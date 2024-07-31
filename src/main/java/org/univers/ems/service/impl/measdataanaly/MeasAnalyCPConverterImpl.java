package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaCP;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaLoad;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyCPConverterImpl implements EntityConverter<MeasDataAnaCP> {
    @Override
    public MeasDataAnaCP convert(ResultSet.Record record) {
        MeasDataAnaCP measDataAnaCP = new MeasDataAnaCP();
        measDataAnaCP.setName(String.valueOf(record.get(0)));
        measDataAnaCP.setSubName(String.valueOf(record.get(1)));
        measDataAnaCP.setVolt("".equals(String.valueOf(record.get(2))) ? 0.001 : Double.valueOf(String.valueOf(record.get(2))));
        measDataAnaCP.setQMark(String.valueOf(record.get(3)));
        measDataAnaCP.setQiMeas("".equals(String.valueOf(record.get(4))) ? 0.001 : Double.valueOf(String.valueOf(record.get(4))));
        return measDataAnaCP;
    }
}
