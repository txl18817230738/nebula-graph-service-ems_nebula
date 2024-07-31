package org.univers.ems.service.impl;


import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.NebulaClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.univers.ems.nebula.util.NebulaExecFunction;
import org.univers.ems.pojo.entity.measanaly.*;
import org.univers.ems.pojo.entity.sld.SldBreakerCime;
import org.univers.ems.pojo.entity.sld.SldLoadCime;
import org.univers.ems.pojo.response.*;
import org.univers.ems.service.MeasDataAnalysisService;
import org.univers.ems.service.impl.measdataanaly.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jie.xi
 */
@Service
public class MeasDataAnalysisServiceImpl implements MeasDataAnalysisService {

    private final NebulaClient client;

    @Autowired
    public MeasDataAnalysisServiceImpl(NebulaClient client) {
        this.client = client;
    }

    @Override
    public MeasDataAnaUnitResponse execMeasMaintenanceAnalysisUnitTable(String companyId, Boolean returnAll) {
        MeasDataAnaUnitResponse result = new MeasDataAnaUnitResponse();
        String baseQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Unit]->(F6:`unit`) filter where startid.id = \"" + companyId + "\"\n" +
                "let\n" +
                "p_rate = case when F6.P_rate < 0.0 then 300.0 ELSE F6.P_rate end,\n" +
                "piMeas = case when F6.Pimeas is not NULL then F6.Pimeas ELSE 0.0 end\n" +
                "let\n" +
                "meas_overload = case when abs(p_rate) > 0 then abs(piMeas/p_rate)*100 else 0.0 end\n" +
                "let\n" +
                "subName = s.name, \n" +
                "overload_type = case when meas_overload >= 0.0 and meas_overload <= 20.0 then \"轻载\" when meas_overload > 20.0 and meas_overload <= 80.0 then \"正常\" when meas_overload > 80.0 and meas_overload <= 100.0 then \"重载\" when meas_overload > 100.0 then \"过载\" end,\n" +
                "volt_mark = case when F6.Gentype <> \"虚拟机组\" and F6.Gentype <> \"交换机组\" and F6.Ue_meas <> 0.0 then \"实测\" ELSE \"无实测\" end,\n" +
                "p_mark = case when F6.Gentype <> \"虚拟机组\" and F6.Gentype <> \"交换机组\" and F6.Pimeas <> 0.0 then \"实测\" else \"无实测\" end,\n" +
                "q_mark = case when F6.Gentype <> \"虚拟机组\" and F6.Gentype <> \"交换机组\" and F6.Qimeas <> 0.0 then \"实测\" else \"无实测\" end\n" +
                "return F6.name as name, subName, s.strid as subId, F6.Gentype as genType, F6.volt as volt, F6.base_value as baseValue, volt_mark, F6.Ue_meas as ueMeas,p_mark,F6.Pimeas as piMeas,meas_overload,q_mark,F6.Qimeas as qiMeas, overload_type, F6.meas_t_next as measTNext";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        if(!returnAll){
            queryBuilder.append(" limit 50");
        }
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        MeasAnalyUnitConverterImpl measAnalyUnitConverter = new MeasAnalyUnitConverterImpl();
        List<MeasDataAnaUnit> measDataAnaUnitList = NebulaExecFunction.resolve(resultSet,measAnalyUnitConverter);
        result.setTableData(measDataAnaUnitList);
        String overviewQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Unit]->(F6:`unit`) where startid.gridid = \"113715890758877224\"\n" +
                "let \n" +
                "p_rate = case when F6.P_rate <= 0.0 then 300.0 ELSE F6.P_rate end,\n" +
                "piMeas = case when F6.Pimeas is not NULL then F6.Pimeas ELSE 0.0 end\n" +
                "let \n" +
                "meas_overload = case when p_rate > 0.0 then (piMeas/p_rate)*100.0 ELSE 0.0 end\n" +
                "let\n" +
                "overload_type_less = case when meas_overload >= 0.0 and meas_overload <= 20.0 then 1 else 0 end,\n" +
                "overload_type_midlle = case when meas_overload > 20.0 and meas_overload <= 80.0 then 1 else 0 end,\n" +
                "overload_type_large = case when  meas_overload > 80.0 and meas_overload <= 100.0 then 1 else 0 end,\n" +
                "overload_type_overload = case when meas_overload > 100.0 then 1 else 0 end,\n" +
                "neg = case when piMeas < 0.0 then abs(piMeas) ELSE 0.0 end,\n" +
                "pos = case when piMeas >= 0.0 then piMeas ELSE 0.0 end\n" +
                " return case when F6.Gentype = \"火电机组\" then \"火电\" when F6.Gentype = \"水电机组\" then \"水电\"  when F6.Gentype = \"风电机组\" then \"风电\" when F6.Gentype = \"光伏机组\" then \"光伏\"  " +
                "when F6.Gentype = \"虚拟机组\" then \"虚拟\" when F6.Gentype = \"交换机组\" then \"交换\" when F6.Gentype = \"核电机组\" then \"核电\" " +
                "when F6.Gentype = \"抽蓄机组\" then \"抽蓄\" else \"其他机组\" end as unitType, count(*) as total, sum(p_rate) as cap, sum(neg) as neg, sum(pos) as pos, sum(overload_type_less) as over_load_less, " +
                "sum(overload_type_midlle) as over_load_middle, sum(overload_type_large) as over_load_large, sum(overload_type_overload) as over_load_overload " +
                "group by unitType Next return unitType, total, cap, neg, pos, over_load_less, over_load_middle, over_load_large, over_load_overload";
        ResultSet overviewResultSet = NebulaExecFunction.executeQueryForData(client,overviewQuery);
        //数据转换
        MeasAnalyUnitOverviewConverterImpl measAnalyUnitOverviewConverter = new MeasAnalyUnitOverviewConverterImpl();
        List<MeasDataUnitOverview> measDataUnitOverviewList = NebulaExecFunction.resolve(overviewResultSet,measAnalyUnitOverviewConverter);
        result.setOverviewList(measDataUnitOverviewList);
        return result;
    }

    @Override
    public MeasDataAnaLoadResponse execMeasMaintenanceAnalysisLoadTable(String companyId, Boolean returnAll) {
        MeasDataAnaLoadResponse result = new MeasDataAnaLoadResponse();
        String baseQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Load]->(t:`l_oad`) filter where startid.id = \"113715890758877224\" and t.island = 1\n" +
                "let\n" +
                "p_mark = case when t.Pimeas <> 0.0  then \"实测\" else \"无实测\" end,\n" +
                "p_mark_num = case when t.Pimeas <> 0.0 then 1 else 0 end,\n" +
                "q_mark = case when t.Qimeas <> 0.0 then \"实测\" else \"无实测\" end,\n" +
                "q_mark_num = case when t.Qimeas <> 0.0 then 1 else 0 end\n" +
                "return t.name as name, s.name as subName, t.busName as busName, t.volt, p_mark, t.meas_t_next, t.Pimeas, q_mark, t.Qimeas";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        if(!returnAll){
            queryBuilder.append(" limit 50");
        }
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        MeasAnalyLoadConverterImpl measAnalyLoadConverter = new MeasAnalyLoadConverterImpl();
        List<MeasDataAnaLoad> measDataAnaLoadList = NebulaExecFunction.resolve(resultSet,measAnalyLoadConverter);
        result.setTableData(measDataAnaLoadList);
        String overviewQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Load]->(t:`l_oad`) where startid.gridid = \"113715890758877224\"\n" +
                "let\n" +
                " pimeas = case when t.Pimeas is not NULL then t.Pimeas else 0.0 end\n" +
                "let\n" +
                " p_meas_neg = case when pimeas < 0.0 then abs(t.Pimeas) else 0.0 end,\n" +
                " p_meas_pos = case when pimeas > 0.0 then t.Pimeas else 0.0 end,\n" +
                "\n" +
                " less_0_p_meas = case when pimeas < 0.0 then 1 else 0 end,\n" +
                " small_p_meas = case when pimeas >= 0.0 and pimeas < 10.0 then 1 else 0 end,\n" +
                " middle_p_meas = case when pimeas >=10.0 and pimeas < 50.0 then 1 else 0 end,\n" +
                " large_p_meas = case when pimeas >= 50.0 then 1 else 0 end\n" +
                " return case when t.volt >= 500.0 then \"500\" when t.volt >= 220.0 and t.volt < 500.0 then \"220\" when t.volt >= 110.0 and t.volt < 220.0 then \"110\" " +
                "when t.volt >= 35.0 and t.volt < 110.0 then \"35\" when t.volt >= 10.0 and t.volt < 35.0 then \"10\" else \"other\" end as voltLevel, count(*) as total, " +
                "sum(t.P) as cal, sum(p_meas_neg) as neg_meas_total, sum(p_meas_pos) as pos_meas_total, sum(less_0_p_meas) as less_load, sum(small_p_meas) as small_load, " +
                "sum(middle_p_meas) as middle_load, sum(large_p_meas) as large_load group by voltLevel Next return voltLevel, total,  pos_meas_total, neg_meas_total,cal, " +
                "less_load, small_load, middle_load, large_load";
        ResultSet overviewResultSet = NebulaExecFunction.executeQueryForData(client,overviewQuery);
        //数据转换
        MeasAnalyLoadOverviewConverterImpl measAnalyLoadOverviewConverter = new MeasAnalyLoadOverviewConverterImpl();
        List<MeasDataLoadOverview> measDataLoadOverviewList = NebulaExecFunction.resolve(overviewResultSet,measAnalyLoadOverviewConverter);
        result.setOverviewList(measDataLoadOverviewList);
        return result;
    }

    @Override
    public MeasDataAnaCPResponse execMeasMaintenanceAnalysisCPTable(String companyId, Boolean returnAll) {
        MeasDataAnaCPResponse result = new MeasDataAnaCPResponse();
        String baseQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Compensator_P]->(t:`C_P`) filter where startid.id = \"113715890758877224\"\n" +
                "let\n" +
                "q_mark = case when t.Qimeas <> 0.0 then \"实测\" else \"无实测\" end,\n" +
                "q_meas = case when t.Qimeas <> 0.0 then t.Qimeas else 0.0 end\n" +
                "return t.name as name, s.name as subName, t.volt, q_mark, q_meas";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        if(!returnAll){
            queryBuilder.append(" limit 50");
        }
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        MeasAnalyCPConverterImpl measAnalyCPConverter = new MeasAnalyCPConverterImpl();
        List<MeasDataAnaCP> measDataAnaCPList = NebulaExecFunction.resolve(resultSet,measAnalyCPConverter);
        result.setTableData(measDataAnaCPList);
        String overviewQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Compensator_P]->(t:`C_P`) where startid.gridid = \"113715890758877224\"\n" +
                "let\n" +
                " qimeas = case when t.Qimeas is not NULL then t.Qimeas else 0.0 end\n" +
                "let\n" +
                " less_0_q_meas = case when qimeas < 0.0 then 1 else 0 end,\n" +
                " equal_0_q_meas = case when qimeas = 0.0 then 1 else 0 end,\n" +
                " large_0_q_meas = case when qimeas > 0.0 then 1 else 0 end\n" +
                " return case when t.volt >= 500.0 then \"500\" when t.volt >= 220.0 and t.volt < 500.0 then \"220\" " +
                "when t.volt >= 110.0 and t.volt < 220.0 then \"110\" when t.volt >= 35.0 and t.volt < 110.0 then \"35\" " +
                "when t.volt >= 10.0 and t.volt < 35.0 then \"10\" else \"other\" end as voltLevel, count(*) as total, sum(less_0_q_meas) as less_0_total, " +
                "sum(equal_0_q_meas) as equal_0_total, sum(large_0_q_meas) as large_0_total group by voltLevel Next " +
                "return voltLevel, total, less_0_total, equal_0_total, large_0_total";
        ResultSet overviewResultSet = NebulaExecFunction.executeQueryForData(client,overviewQuery);
        //数据转换
        MeasAnalyCPOverviewConverterImpl measAnalyCPOverviewConverter = new MeasAnalyCPOverviewConverterImpl();
        List<MeasDataCPOverview> measDataCPOverviewList = NebulaExecFunction.resolve(overviewResultSet,measAnalyCPOverviewConverter);
        result.setOverviewList(measDataCPOverviewList);
        return result;
    }

    @Override
    public MeasDataAnaCSResponse execMeasMaintenanceAnalysisCSTable(String companyId, Boolean returnAll) {
        MeasDataAnaCSResponse result = new MeasDataAnaCSResponse();
        String baseQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Compensator_S]->(t:`C_S`) filter where startid.id = \"113715890758877224\"\n" +
                "let\n" +
                "q_mark = case when t.Qimeas <> 0.0 then \"实测\" else \"无实测\" end,\n" +
                "q_meas = case when t.Qimeas <> 0.0 then t.Qimeas else 0.0 end\n" +
                "return t.name as name, s.name as subName, t.volt, q_mark, q_meas";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        if(!returnAll){
            queryBuilder.append(" limit 50");
        }
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        MeasAnalyCSConverterImpl measAnalyCSConverter = new MeasAnalyCSConverterImpl();
        List<MeasDataAnaCS> measDataAnaCSList = NebulaExecFunction.resolve(resultSet,measAnalyCSConverter);
        result.setTableData(measDataAnaCSList);
        String overviewQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Compensator_S]->(t:`C_S`) where startid.gridid = \"113715890758877224\"\n" +
                "let\n" +
                " qimeas = case when t.Qimeas is not NULL then t.Qimeas else 0.0 end\n" +
                "let\n" +
                " less_0_q_meas = case when qimeas < 0.0 then 1 else 0 end,\n" +
                " equal_0_q_meas = case when qimeas = 0.0 then 1 else 0 end,\n" +
                " large_0_q_meas = case when qimeas > 0.0 then 1 else 0 end\n" +
                " return case when t.volt >= 500.0 then \"500\" when t.volt >= 220.0 and t.volt < 500.0 then \"220\" when t.volt >= 110.0 and t.volt < 220.0 then \"110\" " +
                "when t.volt >= 35.0 and t.volt < 110.0 then \"35\" when t.volt >= 10.0 and t.volt < 35.0 then \"10\" else \"other\" end as voltLevel, count(*) as total, " +
                "sum(less_0_q_meas) as less_0_total, sum(equal_0_q_meas) as equal_0_total, sum(large_0_q_meas) as large_0_total group by voltLevel Next " +
                "return voltLevel, total, less_0_total, equal_0_total, large_0_total";
        ResultSet overviewResultSet = NebulaExecFunction.executeQueryForData(client,overviewQuery);
        //数据转换
        MeasAnalyCSOverviewConverterImpl measAnalyCSOverviewConverter = new MeasAnalyCSOverviewConverterImpl();
        List<MeasDataCSOverview> measDataCSOverviewList = NebulaExecFunction.resolve(overviewResultSet,measAnalyCSOverviewConverter);
        result.setOverviewList(measDataCSOverviewList);
        return result;
    }

    @Override
    public MeasDataAnaBusResponse execMeasMaintenanceAnalysisBusTable(String companyId, Boolean returnAll) {
        MeasDataAnaBusResponse result = new MeasDataAnaBusResponse();
        String baseQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Bus]->(t:`BUS`)<-[e:topo_bus]-(t2:TopoND) filter where startid.id = \"113715890758877224\" and t.island = 1\n" +
                "return t.name, t.Substation, s.strid, t2.Ri_V as voltWeight, t2.Ri_vP as bus_P_weight, t2.Ri_vQ as bus_Q_weight, t.volt, t.Pimeas as bus_v, t2.ZJP, t2.ZJQ";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        if(!returnAll){
            queryBuilder.append(" limit 50");
        }
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        MeasAnalyBusConverterImpl measAnalyBusConverter = new MeasAnalyBusConverterImpl();
        List<MeasDataAnaBus> measDataAnaBusList = NebulaExecFunction.resolve(resultSet,measAnalyBusConverter);
        result.setTableData(measDataAnaBusList);
        String overviewQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation)-[:connected_Sub_Bus]->(t:`BUS`)<-[e:topo_bus]-(t2:TopoND) where startid.gridid = \"113715890758877224\"\n" +
                "let\n" +
                " p0_q0 = case when t2.ZJP = 1 and t2.ZJQ = 1 then 1 else 0 end,\n" +
                " p1_q1 = case when t2.ZJP = 0 and t.ZJQ = 0 then 1 else 0 end,\n" +
                " p0 = case when t.ZJP = 1 and t.ZJQ = 0 then 1 else 0 end\n" +
                " return case when t.volt >= 500.0 then \"500\" when t.volt >= 220.0 and t.volt < 500.0 then \"220\" " +
                "when t.volt >= 110.0 and t.volt < 220.0 then \"110\" when t.volt >= 35.0 and t.volt < 110.0 then \"35\" " +
                "when t.volt >= 10.0 and t.volt < 35.0 then \"10\" else \"other\" end as voltLevel, count(*) as total, " +
                "sum(p0_q0) as p0_q0_count, sum(p1_q1) as p1_q1_count, sum(p0) as p0_count group by voltLevel Next return voltLevel, total, p0_q0_count, p1_q1_count, p0_count";
        ResultSet overviewResultSet = NebulaExecFunction.executeQueryForData(client,overviewQuery);
        //数据转换
        MeasAnalyBusOverviewConverterImpl measAnalyBusOverviewConverter = new MeasAnalyBusOverviewConverterImpl();
        List<MeasDataBusOverview> measDataBusOverviewList = NebulaExecFunction.resolve(overviewResultSet,measAnalyBusOverviewConverter);
        result.setOverviewList(measDataBusOverviewList);
        return result;
    }

    @Override
    public MeasDataAnaTrans3Response execMeasMaintenanceAnalysisTrans3Table(String companyId, Boolean returnAll) {
        MeasDataAnaTrans3Response result = new MeasDataAnaTrans3Response();
        return result;
    }

    @Override
    public MeasDataAnaAreaResponse execMeasMaintenanceAnalysisAreaTable(String companyId, Boolean returnAll) {
        MeasDataAnaAreaResponse result = new MeasDataAnaAreaResponse();
        String baseQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(s:gridcom)<-[e:gridcom_gridcom_stat] -(t:)  filter where startid.id = \"113715890758877224\"\n" +
                "let\n" +
                "meas_coverage = s.count_sub_meas/s.count_sub,\n" +
                "indu_standard_coverage = t.parameter1*100\n" +
                "return t.name, indu_standard_coverage, meas_coverage,  s.line_p_coverage_rate, s.line_q_coverage_rate, s.unit_p_coverage_rate, " +
                "s.unit_q_coverage_rate, s.load_p_coverage_rate, s.load_q_coverage_rate, s.trans2_p_coverage_rate, \n" +
                "s.trans2_q_coverage_rate, s.trans3_p_coverage_rate, s.trans3_q_coverage_rate, s.cp_coverage_rate, s.bus_meas_coverage_rate";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        if(!returnAll){
            queryBuilder.append(" limit 50");
        }
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        MeasAnalyAreaConverterImpl measAnalyAreaConverter = new MeasAnalyAreaConverterImpl();
        List<MeasDataAnaArea> measDataAnaAreaList = NebulaExecFunction.resolve(resultSet,measAnalyAreaConverter);
        result.setTableData(measDataAnaAreaList);
        String overviewQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(s:gridcom) filter where startid.id = \"113715890758877224\"\n" +
                "return s.line_p_meas, s.line_q_meas, s.unit_p_meas, s.unit_q_meas, s.load_p_meas, s.load_q_meas, s.trans2_p_meas, " +
                "s.trans2_q_meas, s.trans3_p_meas, s.trans3_q_meas, s.cp_q_meas, s.bus_volt_meas";
        ResultSet overviewResultSet = NebulaExecFunction.executeQueryForData(client,overviewQuery);
        //数据转换
        MeasAnalyAreaOverviewConverterImpl measAnalyAreaOverviewConverter = new MeasAnalyAreaOverviewConverterImpl();
        List<MeasDataAreaOverview> measDataAreaOverviewList = NebulaExecFunction.resolve(overviewResultSet,measAnalyAreaOverviewConverter);
        result.setOverviewList(measDataAreaOverviewList);
        return result;
    }

    @Override
    public MeasDataAnaSubstationResponse execMeasMaintenanceAnalysisSubstationTable(String companyId, Boolean returnAll) {
        MeasDataAnaSubstationResponse result = new MeasDataAnaSubstationResponse();
        String baseQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation) filter where startid.id = \"113715890758877224\"\n" +
                "return s.name as name, startid.name as area, s.total_coverage as totalCoverage,s.volt as volt, s.acline_dot_count as aclineDotCount, " +
                "s.acline_dot_meas_count as aclineDotMeasCount, s.trans2_count as trans2Count, s.trans2_meas_count as trans2MeasCount, " +
                "s.trans3_count as trans3Count, s.trans3_meas_count as trans3MeasCount, s.unit_count as unitCount, s.unit_meas_count as unitMeasCount, " +
                "s.load_count as loadCount, s.load_meas_count as loadMeasCount, s.bus_count as busCount, s.bus_meas_count as busMeasCount";
        StringBuilder queryBuilder = new StringBuilder(baseQuery);
        if(!returnAll){
            queryBuilder.append(" limit 50");
        }
        ResultSet resultSet = NebulaExecFunction.executeQueryForData(client,queryBuilder.toString());
        //数据转换
        MeasAnalySubstationConverterImpl measAnalySubstationConverter = new MeasAnalySubstationConverterImpl();
        List<MeasDataAnaSubstation> measDataAnaSubstationList = NebulaExecFunction.resolve(resultSet,measAnalySubstationConverter);
        result.setTableData(measDataAnaSubstationList);
        String overviewQuery = "use ems match (startid:gridcom)-[:gridcom_gridcom]->{0,}(:gridcom)-[:connected_grid_sub]->(s:Substation) filter where startid.id = \"113715890758877224\"\n" +
                "let\n" +
                "coverageLess50 = case when s.total_coverage <= 0.5 then 1 else 0 end,\n" +
                "coverageLess80 = case when s.total_coverage > 0.5 and s.total_coverage <= 0.8 then 1 else 0 end,\n" +
                "coverageLarge80 = case when s.total_coverage > 0.8 then 1 else 0 end\n" +
                "return case when s.volt >= 500.0 then \"500\" when s.volt >= 220.0 and s.volt < 500.0 then \"220\" " +
                "when s.volt >= 110.0 and s.volt < 220.0 then \"110\" when s.volt >= 35.0 and s.volt < 110.0 then \"35\" " +
                "when s.volt >= 10.0 and s.volt < 35.0 then \"10\" else \"other\" end as voltLevel, count(*) as total, " +
                "sum(coverageLess50) as coverageLess50Count, sum(coverageLess80) as coverageLess80Count, sum(coverageLarge80) as coverageLarge80Count group by voltLevel " +
                "Next return voltLevel, total,  coverageLess50Count, coverageLess80Count, coverageLarge80Count";
        ResultSet overviewResultSet = NebulaExecFunction.executeQueryForData(client,overviewQuery);
        //数据转换
        MeasAnalySubstationOverviewConverterImpl measAnalySubstationOverviewConverter = new MeasAnalySubstationOverviewConverterImpl();
        List<MeasDataSubstationOverview> measDataSubstationOverviewList = NebulaExecFunction.resolve(overviewResultSet,measAnalySubstationOverviewConverter);
        result.setOverviewList(measDataSubstationOverviewList);
        return result;
    }
}
