package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldCompensatorPCime;
import org.univers.ems.pojo.entity.sld.SldTransThreeCime;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldCpCimeConverterImpl implements EntityConverter<SldCompensatorPCime>{
    @Override
    public SldCompensatorPCime convert(ResultSet.Record record) {
        SldCompensatorPCime sldCompensatorPCime = new SldCompensatorPCime();
        // 假设我们知道每一列的类型和顺序
        sldCompensatorPCime.setId(String.valueOf(record.get(0)));
        sldCompensatorPCime.setSubstation(String.valueOf(record.get(1)));
        sldCompensatorPCime.setName(String.valueOf(record.get(2)));
        sldCompensatorPCime.setType("并补");
        sldCompensatorPCime.setVolt("".equals(String.valueOf(record.get(3))) ? null : Double.valueOf(String.valueOf(record.get(3))));
        sldCompensatorPCime.setPoint(String.valueOf(record.get(4)));
        sldCompensatorPCime.setRatedCap(Double.valueOf(String.valueOf(record.get(5))));
        sldCompensatorPCime.setRatedVolt(Double.valueOf(String.valueOf(record.get(6))));
        sldCompensatorPCime.setIsland("没有数据");
        sldCompensatorPCime.setQ("".equals(String.valueOf(record.get(7))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(7))));
        sldCompensatorPCime.setNode(String.valueOf(record.get(8)));
        sldCompensatorPCime.setBaseVoltage("".equals(String.valueOf(record.get(7))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(7))));
        return sldCompensatorPCime;
    }
}
