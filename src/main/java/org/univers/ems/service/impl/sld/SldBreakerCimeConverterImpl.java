package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldBreakerCime;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldBreakerCimeConverterImpl implements EntityConverter<SldBreakerCime> {
    @Override
    public SldBreakerCime convert(ResultSet.Record record) {
        SldBreakerCime sldBreakerCime = new SldBreakerCime();
        // 假设我们知道每一列的类型和顺序
        sldBreakerCime.setId(String.valueOf(record.get(0)));
        sldBreakerCime.setSubstation(String.valueOf(record.get(1)));
        sldBreakerCime.setName(String.valueOf(record.get(3)));
        sldBreakerCime.setVolt(String.valueOf(record.get(4)));
        sldBreakerCime.setPoint(String.valueOf(record.get(5)));
        sldBreakerCime.setInode(String.valueOf(record.get(6)));
        sldBreakerCime.setJnode(String.valueOf(record.get(7)));
        return sldBreakerCime;
    }
}
