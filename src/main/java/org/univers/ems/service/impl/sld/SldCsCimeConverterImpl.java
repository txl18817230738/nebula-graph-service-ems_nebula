package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldCompensatorPCime;
import org.univers.ems.pojo.entity.sld.SldCompensatorSCime;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldCsCimeConverterImpl implements EntityConverter<SldCompensatorSCime>{
    @Override
    public SldCompensatorSCime convert(ResultSet.Record record) {
        SldCompensatorSCime sldCompensatorSCime = new SldCompensatorSCime();
        // 假设我们知道每一列的类型和顺序
        sldCompensatorSCime.setId(String.valueOf(record.get(0)));
        sldCompensatorSCime.setSubstation(String.valueOf(record.get(1)));
        sldCompensatorSCime.setName(String.valueOf(record.get(2)));
        sldCompensatorSCime.setVolt("".equals(String.valueOf(record.get(3))) ? null : Double.valueOf(String.valueOf(record.get(3))));
        sldCompensatorSCime.setZk("".equals(String.valueOf(record.get(4))) ? null : Double.valueOf(String.valueOf(record.get(4))));
        sldCompensatorSCime.setPoint(String.valueOf(record.get(5)));
        sldCompensatorSCime.setIsland("没有数据");
        sldCompensatorSCime.setIp("".equals(String.valueOf(record.get(6))) ? null : Double.valueOf(String.valueOf(record.get(6))));
        sldCompensatorSCime.setIq("".equals(String.valueOf(record.get(7))) ? null : Double.valueOf(String.valueOf(record.get(7))));
        sldCompensatorSCime.setJp("".equals(String.valueOf(record.get(8))) ? null : Double.valueOf(String.valueOf(record.get(8))));
        sldCompensatorSCime.setJq("".equals(String.valueOf(record.get(9))) ? null : Double.valueOf(String.valueOf(record.get(9))));
        sldCompensatorSCime.setInode(String.valueOf(record.get(10)));
        sldCompensatorSCime.setJnode(String.valueOf(record.get(11)));
        sldCompensatorSCime.setCszk("".equals(String.valueOf(record.get(12))) ? null : Double.valueOf(String.valueOf(record.get(12))));
        return sldCompensatorSCime;
    }
}
