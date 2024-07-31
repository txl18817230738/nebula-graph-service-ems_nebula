package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;

import org.univers.ems.pojo.entity.sld.SldBusCime;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldBusCimeConverterImpl implements EntityConverter<SldBusCime> {
    @Override
    public SldBusCime convert(ResultSet.Record record) {
        SldBusCime sldBusCime = new SldBusCime();
        // 假设我们知道每一列的类型和顺序
        sldBusCime.setId(String.valueOf(record.get(0)));
        sldBusCime.setSubstation(String.valueOf(record.get(1)));
        sldBusCime.setName(String.valueOf(record.get(2)));
        sldBusCime.setVolt("".equals(String.valueOf(record.get(3))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(3))));
        sldBusCime.setIsland("没有数据");
        sldBusCime.setType("母线");
        sldBusCime.setPoint(String.valueOf(record.get(4)));
        sldBusCime.setP("".equals(String.valueOf(record.get(5))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(5))));
        sldBusCime.setQ("".equals(String.valueOf(record.get(6))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(6))));
        sldBusCime.setNode(String.valueOf(record.get(7)));
        sldBusCime.setVMax("".equals(String.valueOf(record.get(8))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(8))));
        sldBusCime.setVMin("".equals(String.valueOf(record.get(9))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(9))));
        sldBusCime.setBaseVoltage("".equals(String.valueOf(record.get(10))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(10))));
        return sldBusCime;
    }
}
