package org.univers.ems.service.impl.measdataanaly;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaArea;
import org.univers.ems.pojo.entity.measanaly.MeasDataAnaBus;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class MeasAnalyAreaConverterImpl implements EntityConverter<MeasDataAnaArea> {
    @Override
    public MeasDataAnaArea convert(ResultSet.Record record) {
        MeasDataAnaArea measDataAnaArea = new MeasDataAnaArea();
        measDataAnaArea.setName(String.valueOf(record.get(0)));
        measDataAnaArea.setInduStandardCoverage("".equals(String.valueOf(record.get(1))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(1))));
        measDataAnaArea.setTotalCoverage("".equals(String.valueOf(record.get(2))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(2))));
        measDataAnaArea.setLinePCoverageRate("".equals(String.valueOf(record.get(3))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(3))));
        measDataAnaArea.setLineQCoverageRate("".equals(String.valueOf(record.get(4))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(4))));
        measDataAnaArea.setUnitPCoverageRate("".equals(String.valueOf(record.get(5))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(5))));
        measDataAnaArea.setUnitQCoverageRate("".equals(String.valueOf(record.get(6))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(6))));
        measDataAnaArea.setLoadPCoverageRate("".equals(String.valueOf(record.get(7))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(7))));
        measDataAnaArea.setLoadQCoverageRate("".equals(String.valueOf(record.get(8))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(8))));
        measDataAnaArea.setTrans2PCoverageRate("".equals(String.valueOf(record.get(9))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(9))));
        measDataAnaArea.setTrans2QCoverageRate("".equals(String.valueOf(record.get(10))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(10))));
        measDataAnaArea.setTrans3PCoverageRate("".equals(String.valueOf(record.get(11))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(11))));
        measDataAnaArea.setTrans3QCoverageRate("".equals(String.valueOf(record.get(12))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(12))));
        measDataAnaArea.setCpCoverageRate("".equals(String.valueOf(record.get(13))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(13))));
        measDataAnaArea.setBusMeasCoverageRate("".equals(String.valueOf(record.get(14))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(14))));
        return measDataAnaArea;
    }
}
