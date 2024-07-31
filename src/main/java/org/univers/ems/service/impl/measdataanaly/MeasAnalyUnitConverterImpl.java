package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaSubstation;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaUnit;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyUnitConverterImpl implements EntityConverter<MeasDataAnaUnit> {
    @Override
    public MeasDataAnaUnit convert(ResultSet.Record record) {
        MeasDataAnaUnit measDataAnaUnit = new MeasDataAnaUnit();
        measDataAnaUnit.setName(String.valueOf(record.get(0)));
        measDataAnaUnit.setMeasOverload("".equals(String.valueOf(record.get(10))) ? 0.001 : Double.valueOf(String.valueOf(record.get(10))));
        measDataAnaUnit.setGenType(String.valueOf(record.get(3)));
        measDataAnaUnit.setSubName(String.valueOf(record.get(1)));
        measDataAnaUnit.setVolt("".equals(String.valueOf(record.get(4))) ? 0.001 : Double.valueOf(String.valueOf(record.get(4))));
        measDataAnaUnit.setPMark(String.valueOf(record.get(8)));
        measDataAnaUnit.setPiMeas("".equals(String.valueOf(record.get(9))) ? 0.001 : Double.valueOf(String.valueOf(record.get(9))));
        measDataAnaUnit.setMeasNext("".equals(String.valueOf(record.get(14))) ? 0.001 : Double.valueOf(String.valueOf(record.get(14))));
        measDataAnaUnit.setOverloadType(String.valueOf(record.get(13)));
        measDataAnaUnit.setQMark(String.valueOf(record.get(11)));
        measDataAnaUnit.setQiMeas("".equals(String.valueOf(record.get(12))) ? 0.001 : Double.valueOf(String.valueOf(record.get(12))));
        return measDataAnaUnit;
    }
}
