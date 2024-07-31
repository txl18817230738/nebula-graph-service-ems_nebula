package org.univers.ems.service.impl;


import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.NebulaClient;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.univers.ems.pojo.entity.sld.*;
import org.univers.ems.pojo.response.*;
import org.univers.ems.service.impl.sld.*;
import org.univers.ems.util.GraphClientExample;
import org.univers.ems.nebula.util.NebulaExecFunction;
import org.univers.ems.service.SldService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jie.xi
 */
@Service
public class SldServiceImpl implements SldService {
    private static final Logger log = LoggerFactory.getLogger(GraphClientExample.class);
    private final NebulaClient client;

    @Autowired
    public SldServiceImpl(NebulaClient client) {
        this.client = client;
    }

    @Override
    public PaginationData execSldBreakerTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt)) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Breaker]->(F6:Breaker)" +
                " where startid.id = \""+ companyId + "\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +
                " return F6.id as id,F6.Substation as substation,F6.strid,F6.name as name,F6.volt as volt,F6.point as point,F6.I_nd as inode,F6.J_nd as jnode";

        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);

        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldBreakerCimeConverterImpl sldBreakerCimeConverter = new SldBreakerCimeConverterImpl();
        List<SldBreakerCime> sldBreakerCimeList = NebulaExecFunction.resolve(resultSet,sldBreakerCimeConverter);
        List<Object> objects = new ArrayList<>(sldBreakerCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverViewResponse execSldBreakerOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Breaker]->(F6:Breaker)" +
                " where startid.id = \""+ companyId +
                "\" return F6.id as id,F6.Substation as substation,F6.strid,F6.name as name,F6.volt as volt,F6.point as point,F6.I_nd as iNode,F6.J_nd as jNode";

        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);
        int size = resultSet.getRows().size();
        //数据转换
        SldBreakerCimeConverterImpl sldBreakerCimeConverter = new SldBreakerCimeConverterImpl();
        List<SldBreakerCime> sldBreakerCimeList = NebulaExecFunction.resolve(resultSet,sldBreakerCimeConverter);

        OverViewResponse overViewResponse = new OverViewResponse();
        Integer totalCount500 = 0;
        Integer totalCount220 = 0;
        Integer totalCount110 = 0;
        Integer totalCount35 = 0;
        Integer totalCount10 = 0;
        Integer totalCountOther = 0;
        Integer islandCount500 = 0;
        Integer islandCount220 = 0;
        Integer islandCount110 = 0;
        Integer islandCount35 = 0;
        Integer islandCount10 = 0;
        Integer islandCountOther = 0;
        Integer noIslandCount500 = 0;
        Integer noIslandCount220 = 0;
        Integer noIslandCount110 = 0;
        Integer noIslandCount35 = 0;
        Integer noIslandCount10 = 0;
        Integer noIslandCountOther = 0;
        Integer offCount500 = 0;
        Integer offCount220 = 0;
        Integer offCount110 = 0;
        Integer offCount35 = 0;
        Integer offCount10 = 0;
        Integer offCountOther = 0;
        Integer onCount500 = 0;
        Integer onCount220 = 0;
        Integer onCount110 = 0;
        Integer onCount35 = 0;
        Integer onCount10 = 0;
        Integer onCountOther = 0;
        for (int i = 0; i < sldBreakerCimeList.size(); i++) {
            Double volt = Double.parseDouble(sldBreakerCimeList.get(i).getVolt());
            Integer point = Integer.parseInt(sldBreakerCimeList.get(i).getPoint());
            if (volt >= 500){
                totalCount500++;
                if(point == 0){
                    offCount500++;
                }else {
                    onCount500++;
                }
            } else if (volt >= 220 && volt < 500) {
                totalCount220++;
                if(point == 0){
                    offCount220++;
                }else {
                    onCount220++;
                }
            } else if (volt >= 110 && volt < 220) {
                totalCount110++;
                if(point == 0){
                    offCount110++;
                }else {
                    onCount110++;
                }
            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;
                if(point == 0){
                    offCount35++;
                }else {
                    onCount35++;
                }
            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;
                if(point == 0){
                    offCount10++;
                }else {
                    onCount10++;
                }
            } else {
                totalCountOther++;
                if(point == 0){
                    offCountOther++;
                }else {
                    onCountOther++;
                }
            }
        }
        overViewResponse.setTotalCountOther(totalCountOther);
        overViewResponse.setTotalCount10(totalCount10);
        overViewResponse.setTotalCount35(totalCount35);
        overViewResponse.setTotalCount110(totalCount110);
        overViewResponse.setTotalCount220(totalCount220);
        overViewResponse.setTotalCount500(totalCount500);
        overViewResponse.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overViewResponse.setIslandCount10(10);
        overViewResponse.setIslandCount35(10);
        overViewResponse.setIslandCount110(10);
        overViewResponse.setIslandCount220(10);
        overViewResponse.setIslandCount500(10);
        overViewResponse.setIslandCountOther(10);
        overViewResponse.setIslandCount(500);

        overViewResponse.setNoIslandCount10(10);
        overViewResponse.setNoIslandCount35(10);
        overViewResponse.setNoIslandCount110(10);
        overViewResponse.setNoIslandCount220(10);
        overViewResponse.setNoIslandCount500(10);
        overViewResponse.setNoIslandCountOther(10);
        overViewResponse.setNoIslandCount(200);

        overViewResponse.setOffCount10(offCount10);
        overViewResponse.setOffCount35(offCount35);
        overViewResponse.setOffCount110(offCount110);
        overViewResponse.setOffCount220(offCount220);
        overViewResponse.setOffCount500(offCount500);
        overViewResponse.setOffCountOther(offCountOther);
        overViewResponse.setOffCount(offCount10+offCount35+offCount110+offCount220+offCount500+offCountOther);

        overViewResponse.setOnCount10(onCount10);
        overViewResponse.setOnCount35(onCount35);
        overViewResponse.setOnCount110(onCount110);
        overViewResponse.setOnCount220(onCount220);
        overViewResponse.setOnCount500(onCount500);
        overViewResponse.setOnCountOther(onCountOther);
        overViewResponse.setOnCount(onCount10+onCount35+onCount110+onCount220+onCount500+onCountOther);
        return overViewResponse;
    }

    @Override
    public PaginationData execSldDisconnectorTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt)) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Disconnector]->(F6:Disconnector)" +
                " where startid.id = \""+ companyId + "\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +
                " return F6.id as id,F6.Substation as substation,F6.strid,F6.name as name,F6.volt as volt,F6.point as point,F6.I_nd as inode,F6.J_nd as jnode";

        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);
        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldDisconnectorCimeConverterImpl sldDisconnectorCimeConverter = new SldDisconnectorCimeConverterImpl();
        List<SldDisconnectorCime> sldDisconnectorCimeList = NebulaExecFunction.resolve(resultSet,sldDisconnectorCimeConverter);
        List<Object> objects = new ArrayList<>(sldDisconnectorCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverViewResponse execSldDisconnectorOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Disconnector]->(F6:Disconnector)" +
                " where startid.id = \""+ companyId +
                "\" return F6.id as id,F6.Substation as substation, F6.strid, F6.name as name, F6.volt as volt, F6.point as point,F6.I_nd as inode,F6.J_nd as jnode";

        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);
        int size = resultSet.getRows().size();
        //数据转换
        SldDisconnectorCimeConverterImpl sldDisconnectorCimeConverter = new SldDisconnectorCimeConverterImpl();
        List<SldDisconnectorCime> sldDisconnectorCimeList = NebulaExecFunction.resolve(resultSet,sldDisconnectorCimeConverter);

        OverViewResponse overViewResponse = new OverViewResponse();
        Integer totalCount500 = 0;
        Integer totalCount220 = 0;
        Integer totalCount110 = 0;
        Integer totalCount35 = 0;
        Integer totalCount10 = 0;
        Integer totalCountOther = 0;

        Integer offCount500 = 0;
        Integer offCount220 = 0;
        Integer offCount110 = 0;
        Integer offCount35 = 0;
        Integer offCount10 = 0;
        Integer offCountOther = 0;
        Integer onCount500 = 0;
        Integer onCount220 = 0;
        Integer onCount110 = 0;
        Integer onCount35 = 0;
        Integer onCount10 = 0;
        Integer onCountOther = 0;
        for (int i = 0; i < sldDisconnectorCimeList.size(); i++) {
            Double volt = Double.parseDouble(sldDisconnectorCimeList.get(i).getVolt());
            Integer point = Integer.parseInt(sldDisconnectorCimeList.get(i).getPoint());
            if (volt >= 500){
                totalCount500++;
                if(point == 0){
                    offCount500++;
                }else {
                    onCount500++;
                }
            } else if (volt >= 220 && volt < 500) {
                totalCount220++;
                if(point == 0){
                    offCount220++;
                }else {
                    onCount220++;
                }
            } else if (volt >= 110 && volt < 220) {
                totalCount110++;
                if(point == 0){
                    offCount110++;
                }else {
                    onCount110++;
                }
            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;
                if(point == 0){
                    offCount35++;
                }else {
                    onCount35++;
                }
            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;
                if(point == 0){
                    offCount10++;
                }else {
                    onCount10++;
                }
            } else {
                totalCountOther++;
                if(point == 0){
                    offCountOther++;
                }else {
                    onCountOther++;
                }
            }
        }
        overViewResponse.setTotalCountOther(totalCountOther);
        overViewResponse.setTotalCount10(totalCount10);
        overViewResponse.setTotalCount35(totalCount35);
        overViewResponse.setTotalCount110(totalCount110);
        overViewResponse.setTotalCount220(totalCount220);
        overViewResponse.setTotalCount500(totalCount500);
        overViewResponse.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overViewResponse.setOffCount10(offCount10);
        overViewResponse.setOffCount35(offCount35);
        overViewResponse.setOffCount110(offCount110);
        overViewResponse.setOffCount220(offCount220);
        overViewResponse.setOffCount500(offCount500);
        overViewResponse.setOffCountOther(offCountOther);
        overViewResponse.setOffCount(offCount10+offCount35+offCount110+offCount220+offCount500+offCountOther);

        overViewResponse.setOnCount10(onCount10);
        overViewResponse.setOnCount35(onCount35);
        overViewResponse.setOnCount110(onCount110);
        overViewResponse.setOnCount220(onCount220);
        overViewResponse.setOnCount500(onCount500);
        overViewResponse.setOnCountOther(onCountOther);
        overViewResponse.setOnCount(onCount10+onCount35+onCount110+onCount220+onCount500+onCountOther);
        return overViewResponse;
    }

    @Override
    public PaginationData execSldUnitTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt)) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Unit]->(F6:`unit`)" +
                " where startid.id = \""+ companyId + "\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +
                " return F6.id as id, F6.Substation as substation, F6.name as name, F6.Gentype as type, F6.volt as volt, F6.off as off, F6.Pimeas as p, F6.Qimeas as q, F6.nd as `node`, F6.base_value as baseVoltage";

        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);
        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldUnitCimeConverterImpl sldUnitCimeConverter = new SldUnitCimeConverterImpl();
        List<SldUnitCime> sldUnitCimeList = NebulaExecFunction.resolve(resultSet,sldUnitCimeConverter);
        List<Object> objects = new ArrayList<>(sldUnitCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverViewResponse execSldUnitOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Unit]->(F6:`unit`)" +
                " where startid.id = \""+ companyId +
                "\" return F6.id as id, F6.Substation as substation, F6.name as name, F6.Gentype as type, F6.volt as volt, F6.off as off, F6.Pimeas as p, F6.Qimeas as q, F6.nd as `node`, F6.base_value as baseVoltage";

        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);
        int size = resultSet.getRows().size();
        //数据转换
        SldDisconnectorCimeConverterImpl sldDisconnectorCimeConverter = new SldDisconnectorCimeConverterImpl();
        List<SldDisconnectorCime> sldDisconnectorCimeList = NebulaExecFunction.resolve(resultSet,sldDisconnectorCimeConverter);

        OverViewResponse overViewResponse = new OverViewResponse();
        Integer totalCount500 = 0;
        Integer totalCount220 = 0;
        Integer totalCount110 = 0;
        Integer totalCount35 = 0;
        Integer totalCount10 = 0;
        Integer totalCountOther = 0;

        Integer islandCount500 = 0;
        Integer islandCount220 = 0;
        Integer islandCount110 = 0;
        Integer islandCount35 = 0;
        Integer islandCount10 = 0;
        Integer islandCountOther = 0;

        Integer noIslandCount500 = 0;
        Integer noIslandCount220 = 0;
        Integer noIslandCount110 = 0;
        Integer noIslandCount35 = 0;
        Integer noIslandCount10 = 0;
        Integer noIslandCountOther = 0;
        for (int i = 0; i < sldDisconnectorCimeList.size(); i++) {
            Double volt = Double.parseDouble(sldDisconnectorCimeList.get(i).getVolt());
            //Integer point = Integer.parseInt(sldDisconnectorCimeList.get(i).getPoint());
            if (volt >= 500){
                totalCount500++;

            } else if (volt >= 220 && volt < 500) {
                totalCount220++;

            } else if (volt >= 110 && volt < 220) {
                totalCount110++;

            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;

            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;

            } else {
                totalCountOther++;

            }
        }
        overViewResponse.setTotalCountOther(totalCountOther);
        overViewResponse.setTotalCount10(totalCount10);
        overViewResponse.setTotalCount35(totalCount35);
        overViewResponse.setTotalCount110(totalCount110);
        overViewResponse.setTotalCount220(totalCount220);
        overViewResponse.setTotalCount500(totalCount500);
        overViewResponse.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overViewResponse.setIslandCountOther(islandCountOther);
        overViewResponse.setIslandCount10(islandCount10);
        overViewResponse.setIslandCount35(islandCount35);
        overViewResponse.setIslandCount110(islandCount110);
        overViewResponse.setIslandCount220(islandCount220);
        overViewResponse.setIslandCount500(islandCount500);
        overViewResponse.setIslandCount(islandCount10 + islandCount35 + islandCount110 + islandCount220 + islandCount500 + islandCountOther);

        overViewResponse.setNoIslandCountOther(noIslandCountOther);
        overViewResponse.setNoIslandCount10(noIslandCount10);
        overViewResponse.setNoIslandCount35(noIslandCount35);
        overViewResponse.setNoIslandCount110(noIslandCount110);
        overViewResponse.setNoIslandCount220(noIslandCount220);
        overViewResponse.setNoIslandCount500(noIslandCount500);
        overViewResponse.setNoIslandCount(noIslandCount10 + noIslandCount35 + noIslandCount110 + noIslandCount220 + noIslandCount500 + noIslandCountOther);


        return overViewResponse;
    }

    @Override
    public OverviewTableSubstation execSldSubstationTableOverview(String companyId) {
        OverviewTableSubstation overviewTableSubstation = new OverviewTableSubstation();
        String Substation_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)<-[:controlarea_sub]-(F6:ControlArea)" +
                " where startid.id = \"" + companyId + "\" return s.id as subId, s.name as name, s.volt as volt, s.ControlArea as controlArea, F6.name as area, s.subtype as type";

        String ACLINE_DOT_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Aclinedot]->(F6:ACline_dot)" +
                " where startid.id = \"" + companyId + "\" return F6.id as id, s.id as subId";

        String TRANS_TWO_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Trans_two]->(F6:two_port_transformer)" +
                " where startid.id = \"" + companyId + "\" return F6.id as id, s.id as subId";

        String TRANS_THREE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Trans_three]->(F6:three_port_transformer)" +
                " where startid.id = \"" + companyId + "\" return F6.id as id, s.id as subId";

        String UNIT_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Unit]->(F6:`unit`)" +
                " where startid.id = \"" + companyId + "\" return F6.id as id, s.id as subId";

        String LOAD_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Load]->(F6:l_oad)" +
                " where startid.id = \"" + companyId + "\" return F6.id as id, s.id as subId";

        String BUS_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Bus]->(F6:BUS)" +
                " where startid.id = \"" + companyId + "\" return F6.id as id, s.id as subId";

        String C_P_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Compensator_P]->(F6:C_P)" +
                " where startid.id = \"" + companyId + "\" return F6.id as id, s.id as subId";

        String C_S_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Compensator_S]->(F6:C_S)" +
                " where startid.id = \"" + companyId + "\" return F6.id as id, s.id as subId";

        String BREAKER_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Breaker]->(F6:Breaker)" +
                " where startid.id = \"" + companyId + "\" return F6.id as id, s.id as subId";

        String DISCONNECTOR_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Disconnector]->(F6:Disconnector)" +
                " where startid.id = \"" + companyId + "\" return F6.id as id, s.id as subId";

        ResultSet substationResultSet = NebulaExecFunction.executeQueryForData(client, Substation_QUERY);
        SldSubstationCimeConverterImpl sldSubstationCimeConverter = new SldSubstationCimeConverterImpl();
        List<SldSubstationCime> sldSubstationCimeList = NebulaExecFunction.resolve(substationResultSet, sldSubstationCimeConverter);
        ResultSet aclineDotResultSet = NebulaExecFunction.executeQueryForData(client, ACLINE_DOT_QUERY);
        System.out.println(aclineDotResultSet.getRows().size());
        Map<String, String> aclineDotMap = NebulaExecFunction.resolveToMap(aclineDotResultSet, "id", "subId");
        Map<String, Integer> aclineDotSubCount = NebulaExecFunction.countMapValues(aclineDotMap);

        ResultSet transTwoResultSet = NebulaExecFunction.executeQueryForData(client, TRANS_TWO_QUERY);
        Map<String, String> transTwoMap = NebulaExecFunction.resolveToMap(transTwoResultSet, "id", "subId");
        Map<String, Integer> transTwoSubCount = NebulaExecFunction.countMapValues(transTwoMap);

        ResultSet transThreeResultSet = NebulaExecFunction.executeQueryForData(client, TRANS_THREE_QUERY);
        Map<String, String> transThreeMap = NebulaExecFunction.resolveToMap(transThreeResultSet, "id", "subId");
        Map<String, Integer> transThreeSubCount = NebulaExecFunction.countMapValues(transThreeMap);

        ResultSet unitResultSet = NebulaExecFunction.executeQueryForData(client, UNIT_QUERY);
        Map<String, String> unitMap = NebulaExecFunction.resolveToMap(unitResultSet, "id", "subId");
        Map<String, Integer> unitSubCount = NebulaExecFunction.countMapValues(unitMap);

        ResultSet loadResultSet = NebulaExecFunction.executeQueryForData(client, LOAD_QUERY);
        Map<String, String> loadMap = NebulaExecFunction.resolveToMap(loadResultSet, "id", "subId");
        Map<String, Integer> loadSubCount = NebulaExecFunction.countMapValues(loadMap);

        ResultSet busResultSet = NebulaExecFunction.executeQueryForData(client, BUS_QUERY);
        Map<String, String> busMap = NebulaExecFunction.resolveToMap(busResultSet, "id", "subId");
        Map<String, Integer> busSubCount = NebulaExecFunction.countMapValues(busMap);

        ResultSet cpResultSet = NebulaExecFunction.executeQueryForData(client, C_P_QUERY);
        Map<String, String> cpMap = NebulaExecFunction.resolveToMap(cpResultSet, "id", "subId");
        Map<String, Integer> cpSubCount = NebulaExecFunction.countMapValues(cpMap);

        ResultSet csResultSet = NebulaExecFunction.executeQueryForData(client, C_S_QUERY);
        Map<String, String> csMap = NebulaExecFunction.resolveToMap(csResultSet, "id", "subId");
        Map<String, Integer> csSubCount = NebulaExecFunction.countMapValues(csMap);

        ResultSet breakerResultSet = NebulaExecFunction.executeQueryForData(client, BREAKER_QUERY);
        Map<String, String> breakerMap = NebulaExecFunction.resolveToMap(breakerResultSet, "id", "subId");
        Map<String, Integer> breakerSubCount = NebulaExecFunction.countMapValues(breakerMap);

        ResultSet disResultSet = NebulaExecFunction.executeQueryForData(client, DISCONNECTOR_QUERY);
        Map<String, String> disMap = NebulaExecFunction.resolveToMap(disResultSet, "id", "subId");
        Map<String, Integer> disSubCount = NebulaExecFunction.countMapValues(disMap);

        Integer totalCount500=0;Integer totalCount220=0;Integer totalCount110=0;Integer totalCount35=0;Integer totalCount10=0;Integer totalCountOther=0;Integer totalCount=0;
        Integer islandCount500=0;Integer islandCount220=0;Integer islandCount110=0;Integer islandCount35=0;Integer islandCount10=0;Integer islandCountOther=0;Integer islandCount=0;
        Integer noIslandCount500=0;Integer noIslandCount220=0;Integer noIslandCount110=0;Integer noIslandCount35=0;Integer noIslandCount10=0;Integer noIslandCountOther=0;Integer noIslandCount=0;
        Integer loadCount500=0;Integer loadCount220=0;Integer loadCount110=0;Integer loadCount35=0;Integer loadCount10=0;Integer loadCountOther=0;Integer loadCount=0;
        Integer noLoadCount500=0;Integer noLoadCount220=0;Integer noLoadCount110=0;Integer noLoadCount35=0;Integer noLoadCount10=0;Integer noLoadCountOther=0;Integer noLoadCount=0;
        Integer unitCount500=0;Integer unitCount220=0;Integer unitCount110=0;Integer unitCount35=0;Integer unitCount10=0;Integer unitCountOther=0;Integer unitCount=0;
        Integer noUnitCount500=0;Integer noUnitCount220=0;Integer noUnitCount110=0;Integer noUnitCount35=0;Integer noUnitCount10=0;Integer noUnitCountOther=0;Integer noUnitCount=0;
        Integer waterCount500=0;Integer waterCount220=0;Integer waterCount110=0;Integer waterCount35=0;Integer waterCount10=0;Integer waterCountOther=0;Integer waterCount=0;
        Integer fireCount500=0;Integer fireCount220=0;Integer fireCount110=0;Integer fireCount35=0;Integer fireCount10=0;Integer fireCountOther=0;Integer fireCount=0;
        Integer windCount500=0;Integer windCount220=0;Integer windCount110=0;Integer windCount35=0;Integer windCount10=0;Integer windCountOther=0;Integer windCount=0;
        Integer storeCount500=0;Integer storeCount220=0;Integer storeCount110=0;Integer storeCount35=0;Integer storeCount10=0;Integer storeCountOther=0;Integer storeCount=0;
        Integer pshCount500=0;Integer pshCount220=0;Integer pshCount110=0;Integer pshCount35=0;Integer pshCount10=0;Integer pshCountOther=0;Integer pshCount=0;
        Integer eleCount500=0;Integer eleCount220=0;Integer eleCount110=0;Integer eleCount35=0;Integer eleCount10=0;Integer eleCountOther=0;Integer eleCount=0;
        Integer nuclearCount500=0;Integer nuclearCount220=0;Integer nuclearCount110=0;Integer nuclearCount35=0;Integer nuclearCount10=0;Integer nuclearCountOther=0;Integer nuclearCount=0;
        Integer pvCount500=0;Integer pvCount220=0;Integer pvCount110=0;Integer pvCount35=0;Integer pvCount10=0;Integer pvCountOther=0;Integer pvCount=0;
        Integer tCount500=0;Integer tCount220=0;Integer tCount110=0;Integer tCount35=0;Integer tCount10=0;Integer tCountOther=0;Integer tCount=0;
        
        
        
        for (int i = 0; i < sldSubstationCimeList.size(); i++) {
            SldSubstationCime sldSubstationCime = sldSubstationCimeList.get(i);
            String id = sldSubstationCime.getId();
            sldSubstationCime.setAclineDotCount(aclineDotSubCount.get(id)== null ? 0 : aclineDotSubCount.get(id));
            sldSubstationCime.setBreakerCount(breakerSubCount.get(id)== null ? 0 : breakerSubCount.get(id));
            sldSubstationCime.setTransTwoCount(transTwoSubCount.get(id)== null ? 0 : transTwoSubCount.get(id));
            sldSubstationCime.setTransThreeCount(transThreeSubCount.get(id)== null ? 0 : transThreeSubCount.get(id));
            sldSubstationCime.setUnitCount(unitSubCount.get(id) == null ? 0 : unitSubCount.get(id));
            sldSubstationCime.setLoadCount(loadSubCount.get(id) == null ? 0 : loadSubCount.get(id));
            sldSubstationCime.setBusCount(busSubCount.get(id) == null ? 0 : busSubCount.get(id));
            sldSubstationCime.setCpCount(cpSubCount.get(id)== null ? 0 : cpSubCount.get(id));
            sldSubstationCime.setCsCount(csSubCount.get(id)== null ? 0 : csSubCount.get(id));
            sldSubstationCime.setDisCount(disSubCount.get(id)== null ? 0 : disSubCount.get(id));
            
            Double volt = Double.parseDouble(sldSubstationCimeList.get(i).getVolt());
            String type = sldSubstationCimeList.get(i).getType();
            //Integer point = Integer.parseInt(sldDisconnectorCimeList.get(i).getPoint());
            if (volt >= 500){
                totalCount500++;
                if("水电厂".equals(type)){
                    waterCount500++;
                } else if ("火电厂".equals(type)) {
                    fireCount500++;
                } else if ("风电场".equals(type)) {
                    windCount500++;
                } else if ("储能站".equals(type)) {
                    storeCount500++;
                } else if ("抽蓄厂".equals(type)) {
                    pshCount500++;
                } else if ("变电站".equals(type)) {
                    eleCount500++;
                } else if ("核电厂".equals(type)) {
                    nuclearCount500++;
                } else if ("光伏站".equals(type)) {
                    pvCount500++;
                } else if ("T接站".equals(type)) {
                    tCount500++;
                }
                if (unitSubCount.get(id) == null || unitSubCount.get(id) == 0){
                    noUnitCount500 ++;
                }else {
                    unitCount500 ++;
                }

                if(loadSubCount.get(id) == null || loadSubCount.get(id) == 0){
                    noLoadCount500 ++;
                }else {
                    loadCount500 ++;
                }
            } else if (volt >= 220 && volt < 500) {
                totalCount220++;
                if("水电厂".equals(type)){
                    waterCount220++;
                } else if ("火电厂".equals(type)) {
                    fireCount220++;
                } else if ("风电场".equals(type)) {
                    windCount220++;
                } else if ("储能站".equals(type)) {
                    storeCount220++;
                } else if ("抽蓄厂".equals(type)) {
                    pshCount220++;
                } else if ("变电站".equals(type)) {
                    eleCount220++;
                } else if ("核电厂".equals(type)) {
                    nuclearCount220++;
                } else if ("光伏站".equals(type)) {
                    pvCount220++;
                } else if ("T接站".equals(type)) {
                    tCount220++;
                }
                if (unitSubCount.get(id) == null || unitSubCount.get(id) == 0){
                    noUnitCount220 ++;
                }else {
                    unitCount220 ++;
                }

                if(loadSubCount.get(id) == null || loadSubCount.get(id) == 0){
                    noLoadCount220 ++;
                }else {
                    loadCount220 ++;
                }
            } else if (volt >= 110 && volt < 220) {
                totalCount110++;
                if("水电厂".equals(type)){
                    waterCount110++;
                } else if ("火电厂".equals(type)) {
                    fireCount110++;
                } else if ("风电场".equals(type)) {
                    windCount110++;
                } else if ("储能站".equals(type)) {
                    storeCount110++;
                } else if ("抽蓄厂".equals(type)) {
                    pshCount110++;
                } else if ("变电站".equals(type)) {
                    eleCount110++;
                } else if ("核电厂".equals(type)) {
                    nuclearCount110++;
                } else if ("光伏站".equals(type)) {
                    pvCount110++;
                } else if ("T接站".equals(type)) {
                    tCount110++;
                }
                if (unitSubCount.get(id) == null || unitSubCount.get(id) == 0){
                    noUnitCount110 ++;
                }else {
                    unitCount110 ++;
                }

                if(loadSubCount.get(id) == null || loadSubCount.get(id) == 0){
                    noLoadCount110 ++;
                }else {
                    loadCount110 ++;
                }
            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;
                if("水电厂".equals(type)){
                    waterCount35++;
                } else if ("火电厂".equals(type)) {
                    fireCount35++;
                } else if ("风电场".equals(type)) {
                    windCount35++;
                } else if ("储能站".equals(type)) {
                    storeCount35++;
                } else if ("抽蓄厂".equals(type)) {
                    pshCount35++;
                } else if ("变电站".equals(type)) {
                    eleCount35++;
                } else if ("核电厂".equals(type)) {
                    nuclearCount35++;
                } else if ("光伏站".equals(type)) {
                    pvCount35++;
                } else if ("T接站".equals(type)) {
                    tCount35++;
                }
                if (unitSubCount.get(id) == null || unitSubCount.get(id) == 0){
                    noUnitCount35 ++;
                }else {
                    unitCount35 ++;
                }

                if(loadSubCount.get(id) == null || loadSubCount.get(id) == 0){
                    noLoadCount35 ++;
                }else {
                    loadCount35 ++;
                }
            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;
                if("水电厂".equals(type)){
                    waterCount10++;
                } else if ("火电厂".equals(type)) {
                    fireCount10++;
                } else if ("风电场".equals(type)) {
                    windCount10++;
                } else if ("储能站".equals(type)) {
                    storeCount10++;
                } else if ("抽蓄厂".equals(type)) {
                    pshCount10++;
                } else if ("变电站".equals(type)) {
                    eleCount10++;
                } else if ("核电厂".equals(type)) {
                    nuclearCount10++;
                } else if ("光伏站".equals(type)) {
                    pvCount10++;
                } else if ("T接站".equals(type)) {
                    tCount10++;
                }
                if (unitSubCount.get(id) == null || unitSubCount.get(id) == 0){
                    noUnitCount10 ++;
                }else {
                    unitCount10 ++;
                }

                if(loadSubCount.get(id) == null || loadSubCount.get(id) == 0){
                    noLoadCount10 ++;
                }else {
                    loadCount10 ++;
                }
            } else {
                totalCountOther++;
                if("水电厂".equals(type)){
                    waterCountOther++;
                } else if ("火电厂".equals(type)) {
                    fireCountOther++;
                } else if ("风电场".equals(type)) {
                    windCountOther++;
                } else if ("储能站".equals(type)) {
                    storeCountOther++;
                } else if ("抽蓄厂".equals(type)) {
                    pshCountOther++;
                } else if ("变电站".equals(type)) {
                    eleCountOther++;
                } else if ("核电厂".equals(type)) {
                    nuclearCountOther++;
                } else if ("光伏站".equals(type)) {
                    pvCountOther++;
                } else if ("T接站".equals(type)) {
                    tCountOther++;
                }
                if (unitSubCount.get(id) == null || unitSubCount.get(id) == 0){
                    noUnitCountOther ++;
                }else {
                    unitCountOther ++;
                }

                if(loadSubCount.get(id) == null || loadSubCount.get(id) == 0){
                    noLoadCountOther ++;
                }else {
                    loadCountOther ++;
                }
            }
        }
        overviewTableSubstation.setTotalCount500(totalCount500);
        overviewTableSubstation.setTotalCount220(totalCount220);
        overviewTableSubstation.setTotalCount110(totalCount110);
        overviewTableSubstation.setTotalCount35(totalCount35);
        overviewTableSubstation.setTotalCount10(totalCount10);
        overviewTableSubstation.setTotalCountOther(totalCountOther);
        overviewTableSubstation.setTotalCount(totalCount500+totalCount220+totalCount110+totalCount35+totalCount10+totalCountOther);

        overviewTableSubstation.setIslandCount500(islandCount500);
        overviewTableSubstation.setIslandCount220(islandCount220);
        overviewTableSubstation.setIslandCount110(islandCount110);
        overviewTableSubstation.setIslandCount35(islandCount35);
        overviewTableSubstation.setIslandCount10(islandCount10);
        overviewTableSubstation.setIslandCountOther(islandCountOther);
        overviewTableSubstation.setIslandCount(islandCount500+islandCount220+islandCount110+islandCount35+islandCount10+islandCountOther);

        overviewTableSubstation.setNoIslandCount500(noIslandCount500);
        overviewTableSubstation.setNoIslandCount220(noIslandCount220);
        overviewTableSubstation.setNoIslandCount110(noIslandCount110);
        overviewTableSubstation.setNoIslandCount35(noIslandCount35);
        overviewTableSubstation.setNoIslandCount10(noIslandCount10);
        overviewTableSubstation.setNoIslandCountOther(noIslandCountOther);
        overviewTableSubstation.setNoIslandCount(noIslandCount500+noIslandCount220+noIslandCount110+noIslandCount35+noIslandCount10+noIslandCountOther);

        overviewTableSubstation.setLoadCount500(loadCount500);
        overviewTableSubstation.setLoadCount220(loadCount220);
        overviewTableSubstation.setLoadCount110(loadCount110);
        overviewTableSubstation.setLoadCount35(loadCount35);
        overviewTableSubstation.setLoadCount10(loadCount10);
        overviewTableSubstation.setLoadCountOther(loadCountOther);
        overviewTableSubstation.setLoadCount(loadCount500+loadCount220+loadCount110+loadCount35+loadCount10+loadCountOther);

        overviewTableSubstation.setNoLoadCount500(noLoadCount500);
        overviewTableSubstation.setNoLoadCount220(noLoadCount220);
        overviewTableSubstation.setNoLoadCount110(noLoadCount110);
        overviewTableSubstation.setNoLoadCount35(noLoadCount35);
        overviewTableSubstation.setNoLoadCount10(noLoadCount10);
        overviewTableSubstation.setNoLoadCountOther(noLoadCountOther);
        overviewTableSubstation.setNoLoadCount(noLoadCount500+noLoadCount220+noLoadCount110+noLoadCount35+noLoadCount10+noLoadCountOther);

        overviewTableSubstation.setUnitCount500(unitCount500);
        overviewTableSubstation.setUnitCount220(unitCount220);
        overviewTableSubstation.setUnitCount110(unitCount110);
        overviewTableSubstation.setUnitCount35(unitCount35);
        overviewTableSubstation.setUnitCount10(unitCount10);
        overviewTableSubstation.setUnitCountOther(unitCountOther);
        overviewTableSubstation.setUnitCount(unitCount500+unitCount220+unitCount110+unitCount35+unitCount10+unitCountOther);

        overviewTableSubstation.setNoUnitCount500(noUnitCount500);
        overviewTableSubstation.setNoUnitCount220(noUnitCount220);
        overviewTableSubstation.setNoUnitCount110(noUnitCount110);
        overviewTableSubstation.setNoUnitCount35(noUnitCount35);
        overviewTableSubstation.setNoUnitCount10(noUnitCount10);
        overviewTableSubstation.setNoUnitCountOther(noUnitCountOther);
        overviewTableSubstation.setNoUnitCount(noUnitCount500+noUnitCount220+noUnitCount110+noUnitCount35+noUnitCount10+noUnitCountOther);

        overviewTableSubstation.setWaterCount500(waterCount500);
        overviewTableSubstation.setWaterCount220(waterCount220);
        overviewTableSubstation.setWaterCount110(waterCount110);
        overviewTableSubstation.setWaterCount35(waterCount35);
        overviewTableSubstation.setWaterCount10(waterCount10);
        overviewTableSubstation.setWaterCountOther(waterCountOther);
        overviewTableSubstation.setWaterCount(waterCount500+waterCount220+waterCount110+waterCount35+waterCount10+waterCountOther);

        overviewTableSubstation.setFireCount500(fireCount500);
        overviewTableSubstation.setFireCount220(fireCount220);
        overviewTableSubstation.setFireCount110(fireCount110);
        overviewTableSubstation.setFireCount35(fireCount35);
        overviewTableSubstation.setFireCount10(fireCount10);
        overviewTableSubstation.setFireCountOther(fireCountOther);
        overviewTableSubstation.setFireCount(fireCount500+fireCount220+fireCount110+fireCount35+fireCount10+fireCountOther);

        overviewTableSubstation.setWindCount500(windCount500);
        overviewTableSubstation.setWindCount220(windCount220);
        overviewTableSubstation.setWindCount110(windCount110);
        overviewTableSubstation.setWindCount35(windCount35);
        overviewTableSubstation.setWindCount10(windCount10);
        overviewTableSubstation.setWindCountOther(windCountOther);
        overviewTableSubstation.setWindCount(windCount500+windCount220+windCount110+windCount35+windCount10+windCountOther);

        overviewTableSubstation.setStoreCount500(storeCount500);
        overviewTableSubstation.setStoreCount220(storeCount220);
        overviewTableSubstation.setStoreCount110(storeCount110);
        overviewTableSubstation.setStoreCount35(storeCount35);
        overviewTableSubstation.setStoreCount10(storeCount10);
        overviewTableSubstation.setStoreCountOther(storeCountOther);
        overviewTableSubstation.setStoreCount(storeCount500+storeCount220+storeCount110+storeCount35+storeCount10+storeCountOther);

        overviewTableSubstation.setPshCount500(pshCount500);
        overviewTableSubstation.setPshCount220(pshCount220);
        overviewTableSubstation.setPshCount110(pshCount110);
        overviewTableSubstation.setPshCount35(pshCount35);
        overviewTableSubstation.setPshCount10(pshCount10);
        overviewTableSubstation.setPshCountOther(pshCountOther);
        overviewTableSubstation.setPshCount(pshCount500+pshCount220+pshCount110+pshCount35+pshCount10+pshCountOther);

        overviewTableSubstation.setEleCount500(eleCount500);
        overviewTableSubstation.setEleCount220(eleCount220);
        overviewTableSubstation.setEleCount110(eleCount110);
        overviewTableSubstation.setEleCount35(eleCount35);
        overviewTableSubstation.setEleCount10(eleCount10);
        overviewTableSubstation.setEleCountOther(eleCountOther);
        overviewTableSubstation.setEleCount(eleCount500+eleCount220+eleCount110+eleCount35+eleCount10+eleCountOther);

        overviewTableSubstation.setNuclearCount500(nuclearCount500);
        overviewTableSubstation.setNuclearCount220(nuclearCount220);
        overviewTableSubstation.setNuclearCount110(nuclearCount110);
        overviewTableSubstation.setNuclearCount35(nuclearCount35);
        overviewTableSubstation.setNuclearCount10(nuclearCount10);
        overviewTableSubstation.setNuclearCountOther(nuclearCountOther);
        overviewTableSubstation.setNuclearCount(nuclearCount500+nuclearCount220+nuclearCount110+nuclearCount35+nuclearCount10+nuclearCountOther);

        overviewTableSubstation.setPvCount500(pvCount500);
        overviewTableSubstation.setPvCount220(pvCount220);
        overviewTableSubstation.setPvCount110(pvCount110);
        overviewTableSubstation.setPvCount35(pvCount35);
        overviewTableSubstation.setPvCount10(pvCount10);
        overviewTableSubstation.setPvCountOther(pvCountOther);
        overviewTableSubstation.setPvCount(pvCount500+pvCount220+pvCount110+pvCount35+pvCount10+pvCountOther);

        overviewTableSubstation.setTCount500(tCount500);
        overviewTableSubstation.setTCount220(tCount220);
        overviewTableSubstation.setTCount110(tCount110);
        overviewTableSubstation.setTCount35(tCount35);
        overviewTableSubstation.setTCount10(tCount10);
        overviewTableSubstation.setTCountOther(tCountOther);
        overviewTableSubstation.setTCount(tCount500+tCount220+tCount110+tCount35+tCount10+tCountOther);
        overviewTableSubstation.setSubstationList(sldSubstationCimeList);
        return overviewTableSubstation;
    }

    @Override
    public PaginationData execSldAclineDotTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt)) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Aclinedot]->(F6:ACline_dot)" +
                " where startid.id = \""+ companyId + /*"\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +*/
                "\" return F6.id as id, F6.name as name, F6.Substation as substation, F6.volt as volt, F6.off as off, F6.Pimeas as p, F6.Qimeas as q, F6.nd as `node`";

        System.out.println(BASE_QUERY);
        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);
        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldAclineDotCimeConverterImpl sldAclineDotCimeConverter = new SldAclineDotCimeConverterImpl();
        List<SldAclineDotCime> sldAclineDotCimeList = NebulaExecFunction.resolve(resultSet,sldAclineDotCimeConverter);
        List<Object> objects = new ArrayList<>(sldAclineDotCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverViewResponse execSldAclineDotOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Aclinedot]->(F6:ACline_dot)" +
                " where startid.id = \""+ companyId +
                "\" return F6.id as id, F6.name as name, F6.Substation as substation, F6.volt as volt, F6.off as off, F6.Pimeas as p, F6.Qimeas as q, F6.nd as `node`";

        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);

        //数据转换
        SldAclineDotCimeConverterImpl sldAclineDotCimeConverter = new SldAclineDotCimeConverterImpl();
        List<SldAclineDotCime> sldAclineDotCimeList = NebulaExecFunction.resolve(resultSet,sldAclineDotCimeConverter);

        OverViewResponse overViewResponse = new OverViewResponse();
        Integer totalCount500 = 0;
        Integer totalCount220 = 0;
        Integer totalCount110 = 0;
        Integer totalCount35 = 0;
        Integer totalCount10 = 0;
        Integer totalCountOther = 0;

        Integer islandCount500 = 0;
        Integer islandCount220 = 0;
        Integer islandCount110 = 0;
        Integer islandCount35 = 0;
        Integer islandCount10 = 0;
        Integer islandCountOther = 0;

        Integer noIslandCount500 = 0;
        Integer noIslandCount220 = 0;
        Integer noIslandCount110 = 0;
        Integer noIslandCount35 = 0;
        Integer noIslandCount10 = 0;
        Integer noIslandCountOther = 0;

        Integer offCount500 = 0;
        Integer offCount220 = 0;
        Integer offCount110 = 0;
        Integer offCount35 = 0;
        Integer offCount10 = 0;
        Integer offCountOther = 0;
        Integer onCount500 = 0;
        Integer onCount220 = 0;
        Integer onCount110 = 0;
        Integer onCount35 = 0;
        Integer onCount10 = 0;
        Integer onCountOther = 0;
        for (int i = 0; i < sldAclineDotCimeList.size(); i++) {
            Double volt = sldAclineDotCimeList.get(i).getVolt();
            //Integer point = Integer.parseInt(sldDisconnectorCimeList.get(i).getPoint());
            if (volt >= 500){
                totalCount500++;

            } else if (volt >= 220 && volt < 500) {
                totalCount220++;

            } else if (volt >= 110 && volt < 220) {
                totalCount110++;

            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;

            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;

            } else {
                totalCountOther++;
            }
        }
        overViewResponse.setTotalCountOther(totalCountOther);
        overViewResponse.setTotalCount10(totalCount10);
        overViewResponse.setTotalCount35(totalCount35);
        overViewResponse.setTotalCount110(totalCount110);
        overViewResponse.setTotalCount220(totalCount220);
        overViewResponse.setTotalCount500(totalCount500);
        overViewResponse.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overViewResponse.setIslandCount500(islandCount500);overViewResponse.setNoIslandCount500(noIslandCount500);
        overViewResponse.setIslandCount220(islandCount220);overViewResponse.setNoIslandCount220(noIslandCount220);
        overViewResponse.setIslandCount110(islandCount110);overViewResponse.setNoIslandCount110(noIslandCount110);
        overViewResponse.setIslandCount35(islandCount35);overViewResponse.setNoIslandCount35(noIslandCount35);
        overViewResponse.setIslandCount10(islandCount10);overViewResponse.setNoIslandCount10(noIslandCount10);
        overViewResponse.setIslandCountOther(islandCountOther);overViewResponse.setNoIslandCountOther(noIslandCountOther);
        overViewResponse.setIslandCount(islandCount500+islandCount220+islandCount110+islandCount35+islandCount10+islandCountOther);
        overViewResponse.setNoIslandCount(noIslandCount500+noIslandCount220+noIslandCount110+noIslandCount35+noIslandCount10+noIslandCountOther);

        overViewResponse.setOffCount10(offCount10);
        overViewResponse.setOffCount35(offCount35);
        overViewResponse.setOffCount110(offCount110);
        overViewResponse.setOffCount220(offCount220);
        overViewResponse.setOffCount500(offCount500);
        overViewResponse.setOffCountOther(offCountOther);
        overViewResponse.setOffCount(offCount10+offCount35+offCount110+offCount220+offCount500+offCountOther);

        overViewResponse.setOnCount10(onCount10);
        overViewResponse.setOnCount35(onCount35);
        overViewResponse.setOnCount110(onCount110);
        overViewResponse.setOnCount220(onCount220);
        overViewResponse.setOnCount500(onCount500);
        overViewResponse.setOnCountOther(onCountOther);
        overViewResponse.setOnCount(onCount10+onCount35+onCount110+onCount220+onCount500+onCountOther);

        return overViewResponse;
    }

    @Override
    public PaginationData execSldTransTwoTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt)) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Trans_two]->(F6:two_port_transformer)" +
                " where startid.id = \""+ companyId + "\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +
                " return F6.id as id,F6.Substation as substation,F6.name as name,F6.type_two as type,F6.volt as volt, F6.off as off, F6.Pimeas as p, F6.Qimeas as q, F6.nd as `node`, F6.Rstar as rStar, F6.Xstar as xStar,F6.t as t, F6.base_value as baseVoltage";
        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);
        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldTransTwoCimeConverterImpl sldTransTwoConverter = new SldTransTwoCimeConverterImpl();
        List<SldTransTwoCime> sldTransTwoCimeList = NebulaExecFunction.resolve(resultSet,sldTransTwoConverter);
        List<Object> objects = new ArrayList<>(sldTransTwoCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverviewTableTrans execSldTransTwoOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Trans_two]->(F6:two_port_transformer)" +
                " where startid.id = \""+ companyId +
                "\" return F6.id as id,F6.Substation as substation,F6.name as name,F6.type_two as type,F6.volt as volt, F6.off as off, F6.Pimeas as p, F6.Qimeas as q, F6.nd as `node`, F6.Rstar as rStar, F6.Xstar as xStar,F6.t as t, F6.base_value as baseVoltage";

        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);

        //数据转换
        SldTransTwoCimeConverterImpl sldTransTwoConverter = new SldTransTwoCimeConverterImpl();
        List<SldTransTwoCime> sldTransTwoCimeList = NebulaExecFunction.resolve(resultSet,sldTransTwoConverter);

        OverviewTableTrans overviewTableTrans = new OverviewTableTrans();
        Integer totalCount500 = 0;Integer totalCount220 = 0;Integer totalCount110 = 0;Integer totalCount35 = 0;Integer totalCount10 = 0;Integer totalCountOther = 0;
        Integer islandCount500 = 0;Integer islandCount220 = 0;Integer islandCount110 = 0;Integer islandCount35 = 0;Integer islandCount10 = 0;Integer islandCountOther = 0;
        Integer noIslandCount500 = 0;Integer noIslandCount220 = 0;Integer noIslandCount110 = 0;Integer noIslandCount35 = 0;Integer noIslandCount10 = 0;Integer noIslandCountOther = 0;
        Integer smaller500 = 0;Integer middle500 = 0;Integer larger500 = 0;
        Integer smaller220 = 0;Integer middle220 = 0;Integer larger220 = 0;
        Integer smaller110 = 0;Integer middle110 = 0;Integer larger110 = 0;
        Integer smaller35 = 0;Integer middle35 = 0;Integer larger35 = 0;
        Integer smaller10 = 0;Integer middle10 = 0;Integer larger10 = 0;
        Integer smallerOther = 0;Integer middleOther = 0;Integer largerOther = 0;
        Integer smallerTotal = 0;Integer middleTotal = 0;Integer largerTotal = 0;

        for (int i = 0; i < sldTransTwoCimeList.size(); i++) {
            Double volt = sldTransTwoCimeList.get(i).getVolt();
            Double rStar = sldTransTwoCimeList.get(i).getRStar();
            Double xStar = sldTransTwoCimeList.get(i).getXStar();
            Double value = rStar/xStar;
            if (volt >= 500){
                totalCount500++;
                if(value <= 0.5){
                    smaller500++;
                }else if (value > 0.5 && value <=0.8){
                    middle500++;
                }else if(value>0.8){
                    larger500++;
                }
            } else if (volt >= 220 && volt < 500) {
                totalCount220++;
                if(value <= 0.5){
                    smaller220++;
                }else if (value > 0.5 && value <=0.8){
                    middle220++;
                }else if(value>0.8){
                    larger220++;
                }
            } else if (volt >= 110 && volt < 220) {
                totalCount110++;
                if(value <= 0.5){
                    smaller110++;
                }else if (value > 0.5 && value <=0.8){
                    middle110++;
                }else if(value>0.8){
                    larger110++;
                }
            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;
                if(value <= 0.5){
                    smaller35++;
                }else if (value > 0.5 && value <=0.8){
                    middle35++;
                }else if(value>0.8){
                    larger35++;
                }
            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;
                if(value <= 0.5){
                    smaller10++;
                }else if (value > 0.5 && value <=0.8){
                    middle10++;
                }else if(value>0.8){
                    larger10++;
                }
            } else {
                totalCountOther++;
                if(value <= 0.5){
                    smallerOther++;
                }else if (value > 0.5 && value <=0.8){
                    middleOther++;
                }else if(value>0.8){
                    largerOther++;
                }
            }
        }
        overviewTableTrans.setTotalCountOther(totalCountOther);
        overviewTableTrans.setTotalCount10(totalCount10);
        overviewTableTrans.setTotalCount35(totalCount35);
        overviewTableTrans.setTotalCount110(totalCount110);
        overviewTableTrans.setTotalCount220(totalCount220);
        overviewTableTrans.setTotalCount500(totalCount500);
        overviewTableTrans.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overviewTableTrans.setIslandCount500(0);overviewTableTrans.setNoIslandCount500(0);
        overviewTableTrans.setIslandCount220(0);overviewTableTrans.setNoIslandCount220(0);
        overviewTableTrans.setIslandCount110(0);overviewTableTrans.setNoIslandCount110(0);
        overviewTableTrans.setIslandCount35(0);overviewTableTrans.setNoIslandCount35(0);
        overviewTableTrans.setIslandCount10(0);overviewTableTrans.setNoIslandCount10(0);
        overviewTableTrans.setIslandCountOther(0);overviewTableTrans.setNoIslandCountOther(0);
        overviewTableTrans.setIslandCount(0);overviewTableTrans.setNoIslandCount(0);

        overviewTableTrans.setSmaller500(smaller500);overviewTableTrans.setSmaller220(smaller220);overviewTableTrans.setSmaller110(smaller110);overviewTableTrans.setSmaller35(smaller35);overviewTableTrans.setSmaller10(smaller10);overviewTableTrans.setSmallerOther(smallerOther);overviewTableTrans.setSmallerTotal(smaller500+smaller220+smaller110+smaller35+smaller10+smallerOther);
        overviewTableTrans.setMiddle500(middle500);overviewTableTrans.setMiddle220(middle220);overviewTableTrans.setMiddle110(middle110);overviewTableTrans.setMiddle35(middle35);overviewTableTrans.setMiddle10(middle10);overviewTableTrans.setMiddleOther(middleOther);overviewTableTrans.setMiddleTotal(middle500+middle220+middle110+middle35+middle10+middleOther);
        overviewTableTrans.setLarger500(larger500);overviewTableTrans.setLarger220(larger220);overviewTableTrans.setLarger110(larger110);overviewTableTrans.setLarger35(larger35);overviewTableTrans.setLarger10(larger10);overviewTableTrans.setLargerOther(largerOther);overviewTableTrans.setLargerTotal(larger500+larger220+larger110+larger35+larger10+largerOther);


        return overviewTableTrans;
    }

    @Override
    public PaginationData execSldTransThreeTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt)) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Trans_three]->(F6:three_port_transformer)" +
                " where startid.id = \""+ companyId + "\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +
                " return F6.id as id,F6.Substation as substation,F6.name as name,F6.type_three as type,F6.volt as volt, F6.off as off, F6.Pimeas as p, F6.Qimeas as q, F6.nd as `node`, F6.Rstar as rStar, F6.Xstar as xStar,F6.t as t, F6.base_value as baseVoltage";
        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);
        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldTransThreeCimeConverterImpl sldTransThreeCimeConverter = new SldTransThreeCimeConverterImpl();
        List<SldTransThreeCime> sldTransThreeCimeList = NebulaExecFunction.resolve(resultSet,sldTransThreeCimeConverter);
        List<Object> objects = new ArrayList<>(sldTransThreeCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverviewTableTrans execSldTransThreeOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Trans_three]->(F6:three_port_transformer)" +
                " where startid.id = \""+ companyId +
                "\" return F6.id as id,F6.Substation as substation,F6.name as name,F6.type_three as type,F6.volt as volt, F6.off as off, F6.Pimeas as p, F6.Qimeas as q, F6.nd as `node`, F6.Rstar as rStar, F6.Xstar as xStar,F6.t as t, F6.base_value as baseVoltage";

        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);

        //数据转换
        SldTransThreeCimeConverterImpl sldTransThreeCimeConverter = new SldTransThreeCimeConverterImpl();
        List<SldTransThreeCime> sldTransThreeCimeList = NebulaExecFunction.resolve(resultSet,sldTransThreeCimeConverter);

        OverviewTableTrans overviewTableTrans = new OverviewTableTrans();
        Integer totalCount500 = 0;Integer totalCount220 = 0;Integer totalCount110 = 0;Integer totalCount35 = 0;Integer totalCount10 = 0;Integer totalCountOther = 0;
        Integer islandCount500 = 0;Integer islandCount220 = 0;Integer islandCount110 = 0;Integer islandCount35 = 0;Integer islandCount10 = 0;Integer islandCountOther = 0;
        Integer noIslandCount500 = 0;Integer noIslandCount220 = 0;Integer noIslandCount110 = 0;Integer noIslandCount35 = 0;Integer noIslandCount10 = 0;Integer noIslandCountOther = 0;
        Integer smaller500 = 0;Integer middle500 = 0;Integer larger500 = 0;
        Integer smaller220 = 0;Integer middle220 = 0;Integer larger220 = 0;
        Integer smaller110 = 0;Integer middle110 = 0;Integer larger110 = 0;
        Integer smaller35 = 0;Integer middle35 = 0;Integer larger35 = 0;
        Integer smaller10 = 0;Integer middle10 = 0;Integer larger10 = 0;
        Integer smallerOther = 0;Integer middleOther = 0;Integer largerOther = 0;
        Integer smallerTotal = 0;Integer middleTotal = 0;Integer largerTotal = 0;

        for (int i = 0; i < sldTransThreeCimeList.size(); i++) {
            Double volt = sldTransThreeCimeList.get(i).getVolt();
            Double rStar = sldTransThreeCimeList.get(i).getRStar();
            Double xStar = sldTransThreeCimeList.get(i).getXStar();
            Double value = rStar/xStar;
            if (volt >= 500){
                totalCount500++;
                if(value <= 0.5){
                    smaller500++;
                }else if (value > 0.5 && value <=0.8){
                    middle500++;
                }else if(value>0.8){
                    larger500++;
                }
            } else if (volt >= 220 && volt < 500) {
                totalCount220++;
                if(value <= 0.5){
                    smaller220++;
                }else if (value > 0.5 && value <=0.8){
                    middle220++;
                }else if(value>0.8){
                    larger220++;
                }
            } else if (volt >= 110 && volt < 220) {
                totalCount110++;
                if(value <= 0.5){
                    smaller110++;
                }else if (value > 0.5 && value <=0.8){
                    middle110++;
                }else if(value>0.8){
                    larger110++;
                }
            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;
                if(value <= 0.5){
                    smaller35++;
                }else if (value > 0.5 && value <=0.8){
                    middle35++;
                }else if(value>0.8){
                    larger35++;
                }
            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;
                if(value <= 0.5){
                    smaller10++;
                }else if (value > 0.5 && value <=0.8){
                    middle10++;
                }else if(value>0.8){
                    larger10++;
                }
            } else {
                totalCountOther++;
                if(value <= 0.5){
                    smallerOther++;
                }else if (value > 0.5 && value <=0.8){
                    middleOther++;
                }else if(value>0.8){
                    largerOther++;
                }
            }
        }
        overviewTableTrans.setTotalCountOther(totalCountOther);
        overviewTableTrans.setTotalCount10(totalCount10);
        overviewTableTrans.setTotalCount35(totalCount35);
        overviewTableTrans.setTotalCount110(totalCount110);
        overviewTableTrans.setTotalCount220(totalCount220);
        overviewTableTrans.setTotalCount500(totalCount500);
        overviewTableTrans.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overviewTableTrans.setIslandCount500(0);overviewTableTrans.setNoIslandCount500(0);
        overviewTableTrans.setIslandCount220(0);overviewTableTrans.setNoIslandCount220(0);
        overviewTableTrans.setIslandCount110(0);overviewTableTrans.setNoIslandCount110(0);
        overviewTableTrans.setIslandCount35(0);overviewTableTrans.setNoIslandCount35(0);
        overviewTableTrans.setIslandCount10(0);overviewTableTrans.setNoIslandCount10(0);
        overviewTableTrans.setIslandCountOther(0);overviewTableTrans.setNoIslandCountOther(0);
        overviewTableTrans.setIslandCount(0);overviewTableTrans.setNoIslandCount(0);

        overviewTableTrans.setSmaller500(smaller500);overviewTableTrans.setSmaller220(smaller220);overviewTableTrans.setSmaller110(smaller110);overviewTableTrans.setSmaller35(smaller35);overviewTableTrans.setSmaller10(smaller10);overviewTableTrans.setSmallerOther(smallerOther);overviewTableTrans.setSmallerTotal(smaller500+smaller220+smaller110+smaller35+smaller10+smallerOther);
        overviewTableTrans.setMiddle500(middle500);overviewTableTrans.setMiddle220(middle220);overviewTableTrans.setMiddle110(middle110);overviewTableTrans.setMiddle35(middle35);overviewTableTrans.setMiddle10(middle10);overviewTableTrans.setMiddleOther(middleOther);overviewTableTrans.setMiddleTotal(middle500+middle220+middle110+middle35+middle10+middleOther);
        overviewTableTrans.setLarger500(larger500);overviewTableTrans.setLarger220(larger220);overviewTableTrans.setLarger110(larger110);overviewTableTrans.setLarger35(larger35);overviewTableTrans.setLarger10(larger10);overviewTableTrans.setLargerOther(largerOther);overviewTableTrans.setLargerTotal(larger500+larger220+larger110+larger35+larger10+largerOther);


        return overviewTableTrans;
    }

    @Override
    public PaginationData execSldCpTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt) ) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Compensator_P]->(F6:C_P)" +
                " where startid.id = \""+ companyId + "\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +
                " return F6.id as id, F6.Substation as substation, F6.name as name, F6.volt as volt, F6.off as off, F6.Q_rate as ratedQ, F6.V_rate as retedV, F6.Qimeas as q, F6.nd as `node`, F6.base_value as baseVoltage";
        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);
        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldCpCimeConverterImpl sldCpCimeConverter = new SldCpCimeConverterImpl();
        List<SldCompensatorPCime> sldCompensatorPCimeList = NebulaExecFunction.resolve(resultSet,sldCpCimeConverter);
        List<Object> objects = new ArrayList<>(sldCompensatorPCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverViewResponse execSldCpOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Compensator_P]->(F6:C_P)" +
                " where startid.id = \""+ companyId +
                "\" return F6.id as id, F6.Substation as substation, F6.name as name, F6.volt as volt, F6.off as off, F6.Q_rate as ratedQ, F6.V_rate as retedV, F6.Qimeas as q, F6.nd as `node`, F6.base_value as baseVoltage";

        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);

        //数据转换
        SldCpCimeConverterImpl sldCpCimeConverter = new SldCpCimeConverterImpl();
        List<SldCompensatorPCime> sldCompensatorPCimeList = NebulaExecFunction.resolve(resultSet,sldCpCimeConverter);

        OverViewResponse overViewResponse = new OverViewResponse();
        Integer totalCount500 = 0;
        Integer totalCount220 = 0;
        Integer totalCount110 = 0;
        Integer totalCount35 = 0;
        Integer totalCount10 = 0;
        Integer totalCountOther = 0;

        Integer islandCount500 = 0;
        Integer islandCount220 = 0;
        Integer islandCount110 = 0;
        Integer islandCount35 = 0;
        Integer islandCount10 = 0;
        Integer islandCountOther = 0;

        Integer noIslandCount500 = 0;
        Integer noIslandCount220 = 0;
        Integer noIslandCount110 = 0;
        Integer noIslandCount35 = 0;
        Integer noIslandCount10 = 0;
        Integer noIslandCountOther = 0;

        Integer onCount500 = 0;
        Integer onCount220 = 0;
        Integer onCount110 = 0;
        Integer onCount35 = 0;
        Integer onCount10 = 0;
        Integer onCountOther = 0;

        Integer offCount500 = 0;
        Integer offCount220 = 0;
        Integer offCount110 = 0;
        Integer offCount35 = 0;
        Integer offCount10 = 0;
        Integer offCountOther = 0;
        for (int i = 0; i < sldCompensatorPCimeList.size(); i++) {
            Double volt = sldCompensatorPCimeList.get(i).getVolt();
            Integer point = "".equals(sldCompensatorPCimeList.get(i).getPoint()) ? 0 : Integer.parseInt(sldCompensatorPCimeList.get(i).getPoint());
            if (volt >= 500){
                totalCount500++;
                if(point == 0){
                    offCount500++;
                }else {
                    onCount500++;
                }
            } else if (volt >= 220 && volt < 500) {
                totalCount220++;
                if(point == 0){
                    offCount220++;
                }else {
                    onCount220++;
                }
            } else if (volt >= 110 && volt < 220) {
                totalCount110++;
                if(point == 0){
                    offCount110++;
                }else {
                    onCount110++;
                }
            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;
                if(point == 0){
                    offCount35++;
                }else {
                    onCount35++;
                }
            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;
                if(point == 0){
                    offCount10++;
                }else {
                    onCount10++;
                }
            } else {
                totalCountOther++;
                if(point == 0){
                    offCountOther++;
                }else {
                    onCountOther++;
                }
            }
        }
        overViewResponse.setTotalCountOther(totalCountOther);
        overViewResponse.setTotalCount10(totalCount10);
        overViewResponse.setTotalCount35(totalCount35);
        overViewResponse.setTotalCount110(totalCount110);
        overViewResponse.setTotalCount220(totalCount220);
        overViewResponse.setTotalCount500(totalCount500);
        overViewResponse.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overViewResponse.setIslandCount10(islandCount10);
        overViewResponse.setIslandCount35(islandCount35);
        overViewResponse.setIslandCount110(islandCount110);
        overViewResponse.setIslandCount220(islandCount220);
        overViewResponse.setIslandCount500(islandCount500);
        overViewResponse.setIslandCountOther(islandCountOther);
        overViewResponse.setIslandCount(islandCount10+islandCount35+islandCount110+islandCount220+islandCount500+islandCountOther);

        overViewResponse.setNoIslandCount10(noIslandCount10);
        overViewResponse.setNoIslandCount35(noIslandCount35);
        overViewResponse.setNoIslandCount110(noIslandCount110);
        overViewResponse.setNoIslandCount220(noIslandCount220);
        overViewResponse.setNoIslandCount500(noIslandCount500);
        overViewResponse.setNoIslandCountOther(noIslandCountOther);
        overViewResponse.setNoIslandCount(noIslandCount10+noIslandCount35+noIslandCount110+noIslandCount220+noIslandCount500+noIslandCountOther);

        overViewResponse.setOffCount10(offCount10);
        overViewResponse.setOffCount35(offCount35);
        overViewResponse.setOffCount110(offCount110);
        overViewResponse.setOffCount220(offCount220);
        overViewResponse.setOffCount500(offCount500);
        overViewResponse.setOffCountOther(offCountOther);
        overViewResponse.setOffCount(offCount10+offCount35+offCount110+offCount220+offCount500+offCountOther);

        overViewResponse.setOnCount10(onCount10);
        overViewResponse.setOnCount35(onCount35);
        overViewResponse.setOnCount110(onCount110);
        overViewResponse.setOnCount220(onCount220);
        overViewResponse.setOnCount500(onCount500);
        overViewResponse.setOnCountOther(onCountOther);
        overViewResponse.setOnCount(onCount10+onCount35+onCount110+onCount220+onCount500+onCountOther);

        return overViewResponse;
    }

    @Override
    public PaginationData execSldCsTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt)) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Compensator_S]->(F6:C_S)" +
                " where startid.id = \""+ companyId + "\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +
                " return F6.id as id, F6.Substation as substation, F6.name as name, F6.volt as volt, F6.ZK as zk, F6.off as off, " +
                "F6.Pimeas as ip, F6.Qimeas as iq, F6.Pjmeas as jp, " +
                "F6.Qjmeas as jq, F6.I_nd as inode, F6.J_nd as jnode, F6.cs_zk as cszk";
        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);
        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldCsCimeConverterImpl sldCsCimeConverter = new SldCsCimeConverterImpl();
        List<SldCompensatorSCime> sldCompensatorSCimeList = NebulaExecFunction.resolve(resultSet,sldCsCimeConverter);
        List<Object> objects = new ArrayList<>(sldCompensatorSCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverViewResponse execSldCsOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Compensator_S]->(F6:C_S)" +
                " where startid.id = \""+ companyId +
                "\" return F6.id as id, F6.Substation as substation, F6.name as name, F6.volt as volt, F6.ZK as zk, F6.off as off, " +
                "F6.Pimeas as ip, F6.Qimeas as iq, F6.Pjmeas as jp, " +
                "F6.Qjmeas as jq, F6.I_nd as inode, F6.J_nd as jnode, F6.cs_zk as cszk";

        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);

        //数据转换
        //数据转换
        SldCsCimeConverterImpl sldCsCimeConverter = new SldCsCimeConverterImpl();
        List<SldCompensatorSCime> sldCompensatorSCimeList = NebulaExecFunction.resolve(resultSet,sldCsCimeConverter);

        OverViewResponse overViewResponse = new OverViewResponse();
        Integer totalCount500 = 0;
        Integer totalCount220 = 0;
        Integer totalCount110 = 0;
        Integer totalCount35 = 0;
        Integer totalCount10 = 0;
        Integer totalCountOther = 0;

        Integer islandCount500 = 0;
        Integer islandCount220 = 0;
        Integer islandCount110 = 0;
        Integer islandCount35 = 0;
        Integer islandCount10 = 0;
        Integer islandCountOther = 0;

        Integer noIslandCount500 = 0;
        Integer noIslandCount220 = 0;
        Integer noIslandCount110 = 0;
        Integer noIslandCount35 = 0;
        Integer noIslandCount10 = 0;
        Integer noIslandCountOther = 0;

        Integer onCount500 = 0;
        Integer onCount220 = 0;
        Integer onCount110 = 0;
        Integer onCount35 = 0;
        Integer onCount10 = 0;
        Integer onCountOther = 0;

        Integer offCount500 = 0;
        Integer offCount220 = 0;
        Integer offCount110 = 0;
        Integer offCount35 = 0;
        Integer offCount10 = 0;
        Integer offCountOther = 0;
        for (int i = 0; i < sldCompensatorSCimeList.size(); i++) {
            Double volt = sldCompensatorSCimeList.get(i).getVolt();
            Integer point = "".equals(sldCompensatorSCimeList.get(i).getPoint()) ? 0 : Integer.parseInt(sldCompensatorSCimeList.get(i).getPoint());
            if (volt >= 500){
                totalCount500++;
                if(point == 0){
                    offCount500++;
                }else {
                    onCount500++;
                }
            } else if (volt >= 220 && volt < 500) {
                totalCount220++;
                if(point == 0){
                    offCount220++;
                }else {
                    onCount220++;
                }
            } else if (volt >= 110 && volt < 220) {
                totalCount110++;
                if(point == 0){
                    offCount110++;
                }else {
                    onCount110++;
                }
            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;
                if(point == 0){
                    offCount35++;
                }else {
                    onCount35++;
                }
            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;
                if(point == 0){
                    offCount10++;
                }else {
                    onCount10++;
                }
            } else {
                totalCountOther++;
                if(point == 0){
                    offCountOther++;
                }else {
                    onCountOther++;
                }
            }
        }
        overViewResponse.setTotalCountOther(totalCountOther);
        overViewResponse.setTotalCount10(totalCount10);
        overViewResponse.setTotalCount35(totalCount35);
        overViewResponse.setTotalCount110(totalCount110);
        overViewResponse.setTotalCount220(totalCount220);
        overViewResponse.setTotalCount500(totalCount500);
        overViewResponse.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overViewResponse.setIslandCount10(islandCount10);
        overViewResponse.setIslandCount35(islandCount35);
        overViewResponse.setIslandCount110(islandCount110);
        overViewResponse.setIslandCount220(islandCount220);
        overViewResponse.setIslandCount500(islandCount500);
        overViewResponse.setIslandCountOther(islandCountOther);
        overViewResponse.setIslandCount(islandCount10+islandCount35+islandCount110+islandCount220+islandCount500+islandCountOther);

        overViewResponse.setNoIslandCount10(noIslandCount10);
        overViewResponse.setNoIslandCount35(noIslandCount35);
        overViewResponse.setNoIslandCount110(noIslandCount110);
        overViewResponse.setNoIslandCount220(noIslandCount220);
        overViewResponse.setNoIslandCount500(noIslandCount500);
        overViewResponse.setNoIslandCountOther(noIslandCountOther);
        overViewResponse.setNoIslandCount(noIslandCount10+noIslandCount35+noIslandCount110+noIslandCount220+noIslandCount500+noIslandCountOther);

        overViewResponse.setOffCount10(offCount10);
        overViewResponse.setOffCount35(offCount35);
        overViewResponse.setOffCount110(offCount110);
        overViewResponse.setOffCount220(offCount220);
        overViewResponse.setOffCount500(offCount500);
        overViewResponse.setOffCountOther(offCountOther);
        overViewResponse.setOffCount(offCount10+offCount35+offCount110+offCount220+offCount500+offCountOther);

        overViewResponse.setOnCount10(onCount10);
        overViewResponse.setOnCount35(onCount35);
        overViewResponse.setOnCount110(onCount110);
        overViewResponse.setOnCount220(onCount220);
        overViewResponse.setOnCount500(onCount500);
        overViewResponse.setOnCountOther(onCountOther);
        overViewResponse.setOnCount(onCount10+onCount35+onCount110+onCount220+onCount500+onCountOther);

        return overViewResponse;
    }

    @Override
    public PaginationData execSldAclineTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt)) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Aclinedot]->(F6:ACline_dot)<-[e:aclinedot_aclinedot]-(F7:ACline_dot)" +
                " where startid.id = \""+ companyId + /*"\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +*/
                "\" return F6.id as sourceId, F7.id as targetId, e.id as eid, F6.Subname, F6.Substation, F7.Subname, F7.Substation, e.name, e.volt, e.Eq, e.R, e.X, e.B, e.I_node, e.J_node, "+
                "F6.off, F7.off, e.I_P, e.I_Q, e.J_P, e.J_Q, e.Ih, e.I_nd, e.J_nd, e.I_bs, e.J_bs, e.I_island, e.J_island, e.line_R, e.line_X, e.line_B, e.I_mRID, e.J_mRID, " +
                "F6.Pimeas, F6.Qimeas, F7.Pimeas, F7.Qimeas, F6.Rstar, F6.Xstar";
        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);
        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldAclineCimeConverterImpl sldAclineCimeConverter = new SldAclineCimeConverterImpl();
        List<SldAclineCime> sldAclineCimeList = NebulaExecFunction.resolve(resultSet,sldAclineCimeConverter);
        List<Object> objects = new ArrayList<>(sldAclineCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverviewTableAcline execSldAclineOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Aclinedot]->(F6:ACline_dot)<-[e:aclinedot_aclinedot]-(F7:ACline_dot)" +
                " where startid.id = \""+ companyId + /*"\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +*/
                "\" return F6.id as sourceId, F7.id as targetId, e.id as eid, F6.Subname, F6.Substation, F7.Subname, F7.Substation, e.name, e.volt, e.Eq, e.R, e.X, e.B, e.I_node, e.J_node, "+
                "F6.off, F7.off, e.I_P, e.I_Q, e.J_P, e.J_Q, e.Ih, e.I_nd, e.J_nd, e.I_bs, e.J_bs, e.I_island, e.J_island, e.line_R, e.line_X, e.line_B, e.I_mRID, e.J_mRID, " +
                "F6.Pimeas, F6.Qimeas, F7.Pimeas, F7.Qimeas, F6.Rstar, F6.Xstar";

        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);

        //数据转换
        SldAclineCimeConverterImpl sldAclineCimeConverter = new SldAclineCimeConverterImpl();
        List<SldAclineCime> sldAclineCimeList = NebulaExecFunction.resolve(resultSet,sldAclineCimeConverter);

        OverviewTableAcline overviewTableAcline = new OverviewTableAcline();
        Integer totalCount500 = 0;Integer totalCount220 = 0;Integer totalCount110 = 0;Integer totalCount35 = 0;Integer totalCount10 = 0;Integer totalCountOther = 0;
        Integer islandCount500 = 0;Integer islandCount220 = 0;Integer islandCount110 = 0;Integer islandCount35 = 0;Integer islandCount10 = 0;Integer islandCountOther = 0;
        Integer noIslandCount500 = 0;Integer noIslandCount220 = 0;Integer noIslandCount110 = 0;Integer noIslandCount35 = 0;Integer noIslandCount10 = 0;Integer noIslandCountOther = 0;
        Integer smaller500 = 0;Integer middle500 = 0;Integer larger500 = 0;
        Integer smaller220 = 0;Integer middle220 = 0;Integer larger220 = 0;
        Integer smaller110 = 0;Integer middle110 = 0;Integer larger110 = 0;
        Integer smaller35 = 0;Integer middle35 = 0;Integer larger35 = 0;
        Integer smaller10 = 0;Integer middle10 = 0;Integer larger10 = 0;
        Integer smallerOther = 0;Integer middleOther = 0;Integer largerOther = 0;
        Integer smallerTotal = 0;Integer middleTotal = 0;Integer largerTotal = 0;

        for (int i = 0; i < sldAclineCimeList.size(); i++) {
            Double volt = sldAclineCimeList.get(i).getVolt();
            Double rStar = sldAclineCimeList.get(i).getR();
            Double xStar = sldAclineCimeList.get(i).getX();
            Double value = rStar/xStar;
            if (volt >= 500){
                totalCount500++;
                if(value <= 0.5){
                    smaller500++;
                }else if (value > 0.5 && value <=0.8){
                    middle500++;
                }else if(value>0.8){
                    larger500++;
                }
            } else if (volt >= 220 && volt < 500) {
                totalCount220++;
                if(value <= 0.5){
                    smaller220++;
                }else if (value > 0.5 && value <=0.8){
                    middle220++;
                }else if(value>0.8){
                    larger220++;
                }
            } else if (volt >= 110 && volt < 220) {
                totalCount110++;
                if(value <= 0.5){
                    smaller110++;
                }else if (value > 0.5 && value <=0.8){
                    middle110++;
                }else if(value>0.8){
                    larger110++;
                }
            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;
                if(value <= 0.5){
                    smaller35++;
                }else if (value > 0.5 && value <=0.8){
                    middle35++;
                }else if(value>0.8){
                    larger35++;
                }
            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;
                if(value <= 0.5){
                    smaller10++;
                }else if (value > 0.5 && value <=0.8){
                    middle10++;
                }else if(value>0.8){
                    larger10++;
                }
            } else {
                totalCountOther++;
                if(value <= 0.5){
                    smallerOther++;
                }else if (value > 0.5 && value <=0.8){
                    middleOther++;
                }else if(value>0.8){
                    largerOther++;
                }
            }
        }
        overviewTableAcline.setTotalCountOther(totalCountOther);
        overviewTableAcline.setTotalCount10(totalCount10);
        overviewTableAcline.setTotalCount35(totalCount35);
        overviewTableAcline.setTotalCount110(totalCount110);
        overviewTableAcline.setTotalCount220(totalCount220);
        overviewTableAcline.setTotalCount500(totalCount500);
        overviewTableAcline.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overviewTableAcline.setIslandCount500(0);overviewTableAcline.setNoIslandCount500(0);
        overviewTableAcline.setIslandCount220(0);overviewTableAcline.setNoIslandCount220(0);
        overviewTableAcline.setIslandCount110(0);overviewTableAcline.setNoIslandCount110(0);
        overviewTableAcline.setIslandCount35(0);overviewTableAcline.setNoIslandCount35(0);
        overviewTableAcline.setIslandCount10(0);overviewTableAcline.setNoIslandCount10(0);
        overviewTableAcline.setIslandCountOther(0);overviewTableAcline.setNoIslandCountOther(0);
        overviewTableAcline.setIslandCount(0);overviewTableAcline.setNoIslandCount(0);

        overviewTableAcline.setSmaller500(smaller500);overviewTableAcline.setSmaller220(smaller220);overviewTableAcline.setSmaller110(smaller110);overviewTableAcline.setSmaller35(smaller35);overviewTableAcline.setSmaller10(smaller10);overviewTableAcline.setSmallerOther(smallerOther);overviewTableAcline.setSmallerTotal(smaller500+smaller220+smaller110+smaller35+smaller10+smallerOther);
        overviewTableAcline.setMiddle500(middle500);overviewTableAcline.setMiddle220(middle220);overviewTableAcline.setMiddle110(middle110);overviewTableAcline.setMiddle35(middle35);overviewTableAcline.setMiddle10(middle10);overviewTableAcline.setMiddleOther(middleOther);overviewTableAcline.setMiddleTotal(middle500+middle220+middle110+middle35+middle10+middleOther);
        overviewTableAcline.setLarger500(larger500);overviewTableAcline.setLarger220(larger220);overviewTableAcline.setLarger110(larger110);overviewTableAcline.setLarger35(larger35);overviewTableAcline.setLarger10(larger10);overviewTableAcline.setLargerOther(largerOther);overviewTableAcline.setLargerTotal(larger500+larger220+larger110+larger35+larger10+largerOther);


        return overviewTableAcline;
    }

    @Override
    public PaginationData execSldLoadTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt)) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Load]->(F6:l_oad)" +
                " where startid.id = \""+ companyId + "\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +
                " return F6.id, F6.Substation, F6.name as name, F6.volt as volt, F6.off as off, " +
                "F6.Pimeas, F6.Qimeas," +
                "F6.nd as `node`, F6.base_value";
        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);
        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldLoadCimeConverterImpl sldLoadCimeConverter = new SldLoadCimeConverterImpl();
        List<SldLoadCime> sldLoadCimeList = NebulaExecFunction.resolve(resultSet,sldLoadCimeConverter);
        List<Object> objects = new ArrayList<>(sldLoadCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverViewResponse execSldLoadOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Load]->(F6:l_oad)" +
                " where startid.id = \""+ companyId +
                "\" return F6.id, F6.Substation, F6.name as name, F6.volt as volt, F6.off as off, " +
                "F6.Pimeas, F6.Qimeas, " +
                "F6.nd as `node`, F6.base_value";
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);

        //数据转换
        SldLoadCimeConverterImpl sldLoadCimeConverter = new SldLoadCimeConverterImpl();
        List<SldLoadCime> sldLoadCimeList = NebulaExecFunction.resolve(resultSet,sldLoadCimeConverter);

        OverViewResponse overViewResponse = new OverViewResponse();
        Integer totalCount500 = 0;
        Integer totalCount220 = 0;
        Integer totalCount110 = 0;
        Integer totalCount35 = 0;
        Integer totalCount10 = 0;
        Integer totalCountOther = 0;

        Integer islandCount500 = 0;
        Integer islandCount220 = 0;
        Integer islandCount110 = 0;
        Integer islandCount35 = 0;
        Integer islandCount10 = 0;
        Integer islandCountOther = 0;

        Integer noIslandCount500 = 0;
        Integer noIslandCount220 = 0;
        Integer noIslandCount110 = 0;
        Integer noIslandCount35 = 0;
        Integer noIslandCount10 = 0;
        Integer noIslandCountOther = 0;

        for (int i = 0; i < sldLoadCimeList.size(); i++) {
            Double volt = sldLoadCimeList.get(i).getVolt();
            if (volt >= 500){
                totalCount500++;
            } else if (volt >= 220 && volt < 500) {
                totalCount220++;
            } else if (volt >= 110 && volt < 220) {
                totalCount110++;
            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;
            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;
            } else {
                totalCountOther++;
            }
        }
        overViewResponse.setTotalCountOther(totalCountOther);
        overViewResponse.setTotalCount10(totalCount10);
        overViewResponse.setTotalCount35(totalCount35);
        overViewResponse.setTotalCount110(totalCount110);
        overViewResponse.setTotalCount220(totalCount220);
        overViewResponse.setTotalCount500(totalCount500);
        overViewResponse.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overViewResponse.setIslandCount10(islandCount10);
        overViewResponse.setIslandCount35(islandCount35);
        overViewResponse.setIslandCount110(islandCount110);
        overViewResponse.setIslandCount220(islandCount220);
        overViewResponse.setIslandCount500(islandCount500);
        overViewResponse.setIslandCountOther(islandCountOther);
        overViewResponse.setIslandCount(islandCount10+islandCount35+islandCount110+islandCount220+islandCount500+islandCountOther);

        overViewResponse.setNoIslandCount10(noIslandCount10);
        overViewResponse.setNoIslandCount35(noIslandCount35);
        overViewResponse.setNoIslandCount110(noIslandCount110);
        overViewResponse.setNoIslandCount220(noIslandCount220);
        overViewResponse.setNoIslandCount500(noIslandCount500);
        overViewResponse.setNoIslandCountOther(noIslandCountOther);
        overViewResponse.setNoIslandCount(noIslandCount10+noIslandCount35+noIslandCount110+noIslandCount220+noIslandCount500+noIslandCountOther);

        return overViewResponse;
    }

    @Override
    public PaginationData execSldBusTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank) {
        PaginationData paginationData = new PaginationData();
        //查询query,不带分页、不带排序、不带筛选
        String min_volt = "-100000";
        String max_volt = "1000000";
        //若有电压分类需求，添加电压分类需求
        if (StringUtils.isNotBlank(volt)) {
            if ("500kV以上".equals(volt)){
                max_volt = "1000000";
                min_volt = "500";
            }else if ("220kV".equals(volt)) {
                max_volt = "500";
                min_volt = "219.9";
            }else if ("110kV".equals(volt)) {
                max_volt = "220";
                min_volt = "109.9";
            }else if ("35kV".equals(volt)) {
                max_volt = "110";
                min_volt = "34.5";
            }else if ("10kV".equals(volt)) {
                max_volt = "35";
                min_volt = "9.5";
            }else if ("其他".equals(volt)) {
                max_volt = "10";
                min_volt = "-100000";
            }
        }
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Bus]->(F6:BUS)" +
                " where startid.id = \""+ companyId + "\" and F6.volt >= " + min_volt + " and F6.volt < " + max_volt +
                " return F6.id, F6.Substation, F6.name as name, F6.volt as volt, F6.off as off, " +
                "F6.Pimeas, F6.Qimeas," +
                "F6.nd as `node`, F6.v_max, F6.v_min, F6.base_value";
        //查询所有数据条数
        int totalSize = NebulaExecFunction.executeQueryForTotalSize(client,BASE_QUERY);
        paginationData.setTotalSize(totalSize);
        Integer offset = pageSize * currentPage;
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        //若有排序需求，添加排序需求
        if (StringUtils.isNotBlank(rankColumn) && StringUtils.isNotBlank(rank)) {
            queryBuilder.append(" order by ").append(rankColumn).append(" ").append(rank);
        }
        //分页
        queryBuilder.append(" offset ").append(offset).append(" limit ").append(pageSize);
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        SldBusCimeConverterImpl sldBusCimeConverter = new SldBusCimeConverterImpl();
        List<SldBusCime> sldBusCimeList = NebulaExecFunction.resolve(resultSet,sldBusCimeConverter);
        List<Object> objects = new ArrayList<>(sldBusCimeList);
        paginationData.setData(objects);
        paginationData.setCurrentPage(currentPage);
        paginationData.setPageSize(pageSize);
        return paginationData;
    }

    @Override
    public OverViewResponse execSldBusOverview(String companyId) {
        //查询query,不带分页、不带排序
        String BASE_QUERY = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Bus]->(F6:BUS)" +
                " where startid.id = \""+ companyId +
                "\" return F6.id, F6.Substation, F6.name as name, F6.volt as volt, F6.off as off, " +
                "F6.Pimeas, F6.Qimeas," +
                "F6.nd as `node`, F6.v_max, F6.v_min, F6.base_value";
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,BASE_QUERY);

        //数据转换
        SldBusCimeConverterImpl sldBusCimeConverter = new SldBusCimeConverterImpl();
        List<SldBusCime> sldBusCimeList = NebulaExecFunction.resolve(resultSet,sldBusCimeConverter);

        OverViewResponse overViewResponse = new OverViewResponse();
        Integer totalCount500 = 0;
        Integer totalCount220 = 0;
        Integer totalCount110 = 0;
        Integer totalCount35 = 0;
        Integer totalCount10 = 0;
        Integer totalCountOther = 0;

        Integer islandCount500 = 0;
        Integer islandCount220 = 0;
        Integer islandCount110 = 0;
        Integer islandCount35 = 0;
        Integer islandCount10 = 0;
        Integer islandCountOther = 0;

        Integer noIslandCount500 = 0;
        Integer noIslandCount220 = 0;
        Integer noIslandCount110 = 0;
        Integer noIslandCount35 = 0;
        Integer noIslandCount10 = 0;
        Integer noIslandCountOther = 0;

        for (int i = 0; i < sldBusCimeList.size(); i++) {
            Double volt = sldBusCimeList.get(i).getVolt();
            if (volt >= 500){
                totalCount500++;
            } else if (volt >= 220 && volt < 500) {
                totalCount220++;
            } else if (volt >= 110 && volt < 220) {
                totalCount110++;
            } else if (volt >= 34.5 && volt < 110) {
                totalCount35++;
            } else if (volt >= 10 && volt < 34.5) {
                totalCount10++;
            } else {
                totalCountOther++;
            }
        }
        overViewResponse.setTotalCountOther(totalCountOther);
        overViewResponse.setTotalCount10(totalCount10);
        overViewResponse.setTotalCount35(totalCount35);
        overViewResponse.setTotalCount110(totalCount110);
        overViewResponse.setTotalCount220(totalCount220);
        overViewResponse.setTotalCount500(totalCount500);
        overViewResponse.setTotalCount(totalCountOther + totalCount10 + totalCount35 + totalCount110 + totalCount220 + totalCount500);

        overViewResponse.setIslandCount10(islandCount10);
        overViewResponse.setIslandCount35(islandCount35);
        overViewResponse.setIslandCount110(islandCount110);
        overViewResponse.setIslandCount220(islandCount220);
        overViewResponse.setIslandCount500(islandCount500);
        overViewResponse.setIslandCountOther(islandCountOther);
        overViewResponse.setIslandCount(islandCount10+islandCount35+islandCount110+islandCount220+islandCount500+islandCountOther);

        overViewResponse.setNoIslandCount10(noIslandCount10);
        overViewResponse.setNoIslandCount35(noIslandCount35);
        overViewResponse.setNoIslandCount110(noIslandCount110);
        overViewResponse.setNoIslandCount220(noIslandCount220);
        overViewResponse.setNoIslandCount500(noIslandCount500);
        overViewResponse.setNoIslandCountOther(noIslandCountOther);
        overViewResponse.setNoIslandCount(noIslandCount10+noIslandCount35+noIslandCount110+noIslandCount220+noIslandCount500+noIslandCountOther);

        return overViewResponse;
    }

}
