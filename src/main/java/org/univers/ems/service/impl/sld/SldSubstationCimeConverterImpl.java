package org.univers.ems.service.impl.sld;


import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldBreakerCime;
import org.univers.ems.pojo.entity.sld.SldSubstationCime;
import org.univers.ems.service.EntityConverter;

public class SldSubstationCimeConverterImpl implements EntityConverter<SldSubstationCime> {
    @Override
    public SldSubstationCime convert(ResultSet.Record record) {
        SldSubstationCime sldSubstationCime = new SldSubstationCime();
        // 假设我们知道每一列的类型和顺序
        sldSubstationCime.setId(String.valueOf(record.get(0)));
        sldSubstationCime.setName(String.valueOf(record.get(1)));
        sldSubstationCime.setVolt(String.valueOf(record.get(2)));
        sldSubstationCime.setControlArea(String.valueOf(record.get(4)));
        sldSubstationCime.setType(String.valueOf(record.get(5)));
        return sldSubstationCime;
    }
}
