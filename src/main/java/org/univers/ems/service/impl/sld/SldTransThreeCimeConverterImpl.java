package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldTransThreeCime;
import org.univers.ems.pojo.entity.sld.SldTransTwoCime;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldTransThreeCimeConverterImpl implements EntityConverter<SldTransThreeCime>{
    @Override
    public SldTransThreeCime convert(ResultSet.Record record) {
        SldTransThreeCime sldTransThreeCime = new SldTransThreeCime();
        // 假设我们知道每一列的类型和顺序
        sldTransThreeCime.setId(String.valueOf(record.get(0)));
        sldTransThreeCime.setSubstation(String.valueOf(record.get(1)));
        sldTransThreeCime.setName(String.valueOf(record.get(2)));
        sldTransThreeCime.setType(String.valueOf(record.get(3)));
        sldTransThreeCime.setVolt("".equals(String.valueOf(record.get(4))) ? null : Double.valueOf(String.valueOf(record.get(4))));
        sldTransThreeCime.setPoint(String.valueOf(record.get(5)));
        sldTransThreeCime.setIsland("没有数据");
        sldTransThreeCime.setP("".equals(String.valueOf(record.get(6))) ? 0.001 : Double.valueOf(String.valueOf(record.get(6))));
        sldTransThreeCime.setQ("".equals(String.valueOf(record.get(7))) ? 0.001 : Double.valueOf(String.valueOf(record.get(7))));
        sldTransThreeCime.setNode(String.valueOf(record.get(8)));
        sldTransThreeCime.setRStar("".equals(String.valueOf(record.get(9))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(9))));
        sldTransThreeCime.setXStar("".equals(String.valueOf(record.get(10))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(10))));
        sldTransThreeCime.setT(String.valueOf(record.get(11)));
        sldTransThreeCime.setBaseVoltage(String.valueOf(record.get(12)));
        return sldTransThreeCime;
    }
}
