package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldTransTwoCime;
import org.univers.ems.pojo.entity.sld.SldUnitCime;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldTransTwoCimeConverterImpl implements EntityConverter<SldTransTwoCime>{
    @Override
    public SldTransTwoCime convert(ResultSet.Record record) {
        SldTransTwoCime sldTransTwoCime = new SldTransTwoCime();
        // 假设我们知道每一列的类型和顺序
        sldTransTwoCime.setId(String.valueOf(record.get(0)));
        sldTransTwoCime.setSubstation(String.valueOf(record.get(1)));
        sldTransTwoCime.setName(String.valueOf(record.get(2)));
        sldTransTwoCime.setType(String.valueOf(record.get(3)));
        sldTransTwoCime.setVolt("".equals(String.valueOf(record.get(4))) ? null : Double.valueOf(String.valueOf(record.get(4))));
        sldTransTwoCime.setPoint(String.valueOf(record.get(5)));
        sldTransTwoCime.setIsland("没有数据");
        sldTransTwoCime.setP("".equals(String.valueOf(record.get(6))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(6))));
        sldTransTwoCime.setQ("".equals(String.valueOf(record.get(7))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(7))));
        sldTransTwoCime.setNode(String.valueOf(record.get(8)));
        sldTransTwoCime.setRStar("".equals(String.valueOf(record.get(9))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(9))));
        sldTransTwoCime.setXStar("".equals(String.valueOf(record.get(10))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(10))));
        sldTransTwoCime.setT(String.valueOf(record.get(11)));
        sldTransTwoCime.setBaseVoltage(String.valueOf(record.get(12)));
        return sldTransTwoCime;
    }
}
