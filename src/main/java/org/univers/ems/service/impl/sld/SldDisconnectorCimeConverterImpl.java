package org.univers.ems.service.impl.sld;

import com.vesoft.nebula.client.graph.data.ResultSet;
import org.univers.ems.pojo.entity.sld.SldBreakerCime;
import org.univers.ems.pojo.entity.sld.SldDisconnectorCime;
import org.univers.ems.service.EntityConverter;

/**
 * @author jie.xi
 */
public class SldDisconnectorCimeConverterImpl implements EntityConverter<SldDisconnectorCime>{
    @Override
    public SldDisconnectorCime convert(ResultSet.Record record) {
        SldDisconnectorCime sldDisconnectorCime = new SldDisconnectorCime();
        // 假设我们知道每一列的类型和顺序
        sldDisconnectorCime.setId(String.valueOf(record.get(0)));
        sldDisconnectorCime.setSubstation(String.valueOf(record.get(1)));
        sldDisconnectorCime.setName(String.valueOf(record.get(3)));
        sldDisconnectorCime.setVolt(String.valueOf(record.get(4)));
        sldDisconnectorCime.setPoint(String.valueOf(record.get(5)));
        sldDisconnectorCime.setInode(String.valueOf(record.get(6)));
        sldDisconnectorCime.setJnode(String.valueOf(record.get(7)));
        return sldDisconnectorCime;
    }
}
