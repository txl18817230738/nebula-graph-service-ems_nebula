package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldAclineCime;

import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldAclineCimeConverterImpl implements EntityConverter<SldAclineCime>{
    @Override
    public SldAclineCime convert(ResultSet.Record record) {
        SldAclineCime sldAclineCime = new SldAclineCime();
        // 假设我们知道每一列的类型和顺序
        sldAclineCime.setId(String.valueOf(record.get(2)));
        sldAclineCime.setISubstation(String.valueOf(record.get(4)));
        sldAclineCime.setJSubstation(String.valueOf(record.get(6)));
        sldAclineCime.setName(String.valueOf(record.get(7)));
        sldAclineCime.setVolt("".equals(String.valueOf(record.get(8))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(8))));
        sldAclineCime.setIsland("没有数据");
        sldAclineCime.setR("".equals(String.valueOf(record.get(10))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(10))));
        sldAclineCime.setX("".equals(String.valueOf(record.get(11))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(11))));
        sldAclineCime.setB("".equals(String.valueOf(record.get(12))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(12))));
        sldAclineCime.setIname(String.valueOf(record.get(13)));
        sldAclineCime.setJname(String.valueOf(record.get(14)));
        sldAclineCime.setIoff(String.valueOf(record.get(15)));
        sldAclineCime.setJoff(String.valueOf(record.get(16)));
        sldAclineCime.setIp("".equals(String.valueOf(record.get(17))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(17))));
        sldAclineCime.setIq("".equals(String.valueOf(record.get(18))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(18))));
        sldAclineCime.setJp("".equals(String.valueOf(record.get(19))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(19))));
        sldAclineCime.setJq("".equals(String.valueOf(record.get(20))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(20))));
        sldAclineCime.setInode(String.valueOf(record.get(22)));
        sldAclineCime.setJnode(String.valueOf(record.get(23)));
        sldAclineCime.setLineR("".equals(String.valueOf(record.get(28))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(28))));
        sldAclineCime.setLineX("".equals(String.valueOf(record.get(29))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(29))));
        sldAclineCime.setLineB("".equals(String.valueOf(record.get(30))) ? 0.001 : Double.parseDouble(String.valueOf(record.get(30))));
        return sldAclineCime;
    }
}
