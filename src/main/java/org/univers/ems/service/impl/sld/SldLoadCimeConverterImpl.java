package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldBusCime;
import org.univers.ems.pojo.entity.sld.SldLoadCime;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldLoadCimeConverterImpl implements EntityConverter<SldLoadCime> {
    @Override
    public SldLoadCime convert(ResultSet.Record record) {
        SldLoadCime sldLoadCime = new SldLoadCime();
        // 假设我们知道每一列的类型和顺序
        sldLoadCime.setId(String.valueOf(record.get(0)));
        sldLoadCime.setSubstation(String.valueOf(record.get(1)));
        sldLoadCime.setName(String.valueOf(record.get(2)));
        sldLoadCime.setVolt("".equals(String.valueOf(record.get(3))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(3))));
        sldLoadCime.setPoint(String.valueOf(record.get(4)));
        sldLoadCime.setIsland("没有数据");
        sldLoadCime.setP("".equals(String.valueOf(record.get(5))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(5))));
        sldLoadCime.setQ("".equals(String.valueOf(record.get(6))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(6))));
        sldLoadCime.setNode(String.valueOf(record.get(7)));
        sldLoadCime.setBaseVoltage("".equals(String.valueOf(record.get(8))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(8))));
        sldLoadCime.setType("负荷");
        return sldLoadCime;
    }
}
