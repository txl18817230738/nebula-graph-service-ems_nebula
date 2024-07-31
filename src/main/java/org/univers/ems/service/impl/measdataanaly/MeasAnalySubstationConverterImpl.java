package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaSubstation;

import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalySubstationConverterImpl implements EntityConverter<MeasDataAnaSubstation> {
    @Override
    public MeasDataAnaSubstation convert(ResultSet.Record record) {
        MeasDataAnaSubstation measDataAnaSubstation = new MeasDataAnaSubstation();
        measDataAnaSubstation.setName(String.valueOf(record.get(0)));
        measDataAnaSubstation.setArea(String.valueOf(record.get(1)));
        measDataAnaSubstation.setTotalCoverage("".equals(String.valueOf(record.get(2))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(2))));
        measDataAnaSubstation.setVolt("".equals(String.valueOf(record.get(3))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(3))));
        measDataAnaSubstation.setAclineDotCount("".equals(String.valueOf(record.get(4))) ? 0 : Integer.parseInt(String.valueOf(record.get(4))));
        measDataAnaSubstation.setAclineDotMeasCount("".equals(String.valueOf(record.get(5))) ? 0 : Integer.parseInt(String.valueOf(record.get(5))));
        measDataAnaSubstation.setTrans2Count("".equals(String.valueOf(record.get(6))) ? 0 : Integer.parseInt(String.valueOf(record.get(6))));
        measDataAnaSubstation.setTrans2MeasCount("".equals(String.valueOf(record.get(7))) ? 0 : Integer.parseInt(String.valueOf(record.get(7))));
        measDataAnaSubstation.setTrans3Count("".equals(String.valueOf(record.get(8))) ? 0 : Integer.parseInt(String.valueOf(record.get(8))));
        measDataAnaSubstation.setTrans3MeasCount("".equals(String.valueOf(record.get(9))) ? 0 : Integer.parseInt(String.valueOf(record.get(9))));
        measDataAnaSubstation.setUnitCount("".equals(String.valueOf(record.get(10))) ? 0 : Integer.parseInt(String.valueOf(record.get(10))));
        measDataAnaSubstation.setUnitMeasCount("".equals(String.valueOf(record.get(11))) ? 0 : Integer.parseInt(String.valueOf(record.get(11))));
        measDataAnaSubstation.setLoadCount("".equals(String.valueOf(record.get(12))) ? 0 : Integer.parseInt(String.valueOf(record.get(12))));
        measDataAnaSubstation.setLoadMeasCount("".equals(String.valueOf(record.get(13))) ? 0 : Integer.parseInt(String.valueOf(record.get(13))));
        measDataAnaSubstation.setBusCount("".equals(String.valueOf(record.get(14))) ? 0 : Integer.parseInt(String.valueOf(record.get(14))));
        measDataAnaSubstation.setBusMeasCount("".equals(String.valueOf(record.get(15))) ? 0 : Integer.parseInt(String.valueOf(record.get(15))));
        return measDataAnaSubstation;
    }
}
