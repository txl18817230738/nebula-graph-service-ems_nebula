package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaCS;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyCSConverterImpl implements EntityConverter<MeasDataAnaCS> {
    @Override
    public MeasDataAnaCS convert(ResultSet.Record record) {
        MeasDataAnaCS measDataAnaCS = new MeasDataAnaCS();
        measDataAnaCS.setName(String.valueOf(record.get(0)));
        measDataAnaCS.setSubName(String.valueOf(record.get(1)));
        measDataAnaCS.setVolt("".equals(String.valueOf(record.get(2))) ? 0.001 : Double.valueOf(String.valueOf(record.get(2))));
        measDataAnaCS.setQMark(String.valueOf(record.get(3)));
        measDataAnaCS.setQiMeas("".equals(String.valueOf(record.get(4))) ? 0.001 : Double.valueOf(String.valueOf(record.get(4))));
        return measDataAnaCS;
    }
}
