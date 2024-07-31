package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldAclineDotCime;
import org.univers.ems.pojo.entity.sld.SldUnitCime;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldAclineDotCimeConverterImpl implements EntityConverter<SldAclineDotCime>{
    @Override
    public SldAclineDotCime convert(ResultSet.Record record) {
        SldAclineDotCime sldAclineDotCime = new SldAclineDotCime();
        // 假设我们知道每一列的类型和顺序
        sldAclineDotCime.setId(String.valueOf(record.get(0)));
        sldAclineDotCime.setName(String.valueOf(record.get(1)));
        sldAclineDotCime.setSubstation(String.valueOf(record.get(2)));
        sldAclineDotCime.setVolt("".equals(String.valueOf(record.get(3))) ? 0.001 : Double.valueOf(String.valueOf(record.get(3))));
        sldAclineDotCime.setIsland("没有数据");
        sldAclineDotCime.setPoint(String.valueOf(record.get(4)));
        sldAclineDotCime.setP("".equals(String.valueOf(record.get(5))) ? 0.001 : Double.valueOf(String.valueOf(record.get(5))));
        sldAclineDotCime.setQ("".equals(String.valueOf(record.get(6))) ? 0.001 : Double.valueOf(String.valueOf(record.get(6))));
        sldAclineDotCime.setNode(String.valueOf(record.get(7)));
        return sldAclineDotCime;
    }
}
