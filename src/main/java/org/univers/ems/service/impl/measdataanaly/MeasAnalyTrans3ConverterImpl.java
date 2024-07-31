package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaBus;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaTrans3;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyTrans3ConverterImpl implements EntityConverter<MeasDataAnaTrans3> {
    @Override
    public MeasDataAnaTrans3 convert(ResultSet.Record record) {
        MeasDataAnaTrans3 measDataAnaTrans3 = new MeasDataAnaTrans3();
        measDataAnaTrans3.setName(String.valueOf(record.get(0)));
        measDataAnaTrans3.setMeasOverload("".equals(String.valueOf(record.get(1))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(1))));
        measDataAnaTrans3.setOverloadType(String.valueOf(record.get(2)));
        measDataAnaTrans3.setSubName(String.valueOf(record.get(3)));
        measDataAnaTrans3.setVolt("".equals(String.valueOf(record.get(4))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(4))));
        measDataAnaTrans3.setFromBusName(String.valueOf(record.get(5)));
        measDataAnaTrans3.setToBusName(String.valueOf(record.get(6)));
        measDataAnaTrans3.setPMark(String.valueOf(record.get(7)));
        measDataAnaTrans3.setFromPMeas("".equals(String.valueOf(record.get(8))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(8))));
        measDataAnaTrans3.setQMark(String.valueOf(record.get(9)));
        measDataAnaTrans3.setFromQMeas("".equals(String.valueOf(record.get(10))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(10))));
        measDataAnaTrans3.setPMeasWeight("".equals(String.valueOf(record.get(11))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(11))));

        return measDataAnaTrans3;
    }
}
