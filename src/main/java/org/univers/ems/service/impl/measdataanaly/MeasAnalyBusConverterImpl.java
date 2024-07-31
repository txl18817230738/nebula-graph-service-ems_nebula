package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaBus;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaCP;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyBusConverterImpl implements EntityConverter<MeasDataAnaBus> {
    @Override
    public MeasDataAnaBus convert(ResultSet.Record record) {
        MeasDataAnaBus measDataAnaBus = new MeasDataAnaBus();
        measDataAnaBus.setName(String.valueOf(record.get(0)));
        measDataAnaBus.setSubName(String.valueOf(record.get(1)));
        measDataAnaBus.setVolt("".equals(String.valueOf(record.get(2))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(2))));
        measDataAnaBus.setVoltWeight("".equals(String.valueOf(record.get(3))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(3))));
        measDataAnaBus.setPWeight("".equals(String.valueOf(record.get(4))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(4))));
        measDataAnaBus.setQWeight("".equals(String.valueOf(record.get(5))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(5))));
        measDataAnaBus.setPiMeas("".equals(String.valueOf(record.get(6))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(6))));
        measDataAnaBus.setPZeroIn("".equals(String.valueOf(record.get(7))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(7))));
        measDataAnaBus.setQZeroIn("".equals(String.valueOf(record.get(8))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(8))));
        return measDataAnaBus;
    }
}
