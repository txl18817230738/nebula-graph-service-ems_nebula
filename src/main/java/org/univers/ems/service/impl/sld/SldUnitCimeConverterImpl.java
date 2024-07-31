package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldDisconnectorCime;
import org.univers.ems.pojo.entity.sld.SldUnitCime;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldUnitCimeConverterImpl implements EntityConverter<SldUnitCime>{
    @Override
    public SldUnitCime convert(ResultSet.Record record) {
        SldUnitCime sldUnitCime = new SldUnitCime();
        // 假设我们知道每一列的类型和顺序
        sldUnitCime.setId(String.valueOf(record.get(0)));
        sldUnitCime.setSubstation(String.valueOf(record.get(1)));
        sldUnitCime.setName(String.valueOf(record.get(2)));
        sldUnitCime.setType("没有数据");
        sldUnitCime.setVolt("".equals(String.valueOf(record.get(4))) ? null : Double.valueOf(String.valueOf(record.get(4))));
        sldUnitCime.setPoint(String.valueOf(record.get(5)));
        sldUnitCime.setP("".equals(String.valueOf(record.get(6))) ? 0.001 : Double.valueOf(String.valueOf(record.get(6))));
        sldUnitCime.setQ("".equals(String.valueOf(record.get(7))) ? 0.001 : Double.valueOf(String.valueOf(record.get(7))));
        sldUnitCime.setNode(String.valueOf(record.get(8)));
        sldUnitCime.setBaseVoltage(String.valueOf(record.get(9)));
        sldUnitCime.setIsland("否");
        return sldUnitCime;
    }
}
