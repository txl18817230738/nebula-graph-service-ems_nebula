package org.univers.ems.service;

import org.univers.ems.pojo.response.*;

/**
 * @author jie.xi
 */
public interface MeasDataAnalysisService {

    MeasDataAnaSubstationResponse execMeasMaintenanceAnalysisSubstationTable(String companyId, Boolean returnAll);

    MeasDataAnaUnitResponse execMeasMaintenanceAnalysisUnitTable(String companyId, Boolean returnAll);


    MeasDataAnaLoadResponse execMeasMaintenanceAnalysisLoadTable(String companyId, Boolean returnAll);

    MeasDataAnaCPResponse execMeasMaintenanceAnalysisCPTable(String companyId, Boolean returnAll);

    MeasDataAnaCSResponse execMeasMaintenanceAnalysisCSTable(String companyId, Boolean returnAll);

    MeasDataAnaBusResponse execMeasMaintenanceAnalysisBusTable(String companyId, Boolean returnAll);

    MeasDataAnaTrans3Response execMeasMaintenanceAnalysisTrans3Table(String companyId, Boolean returnAll);

    MeasDataAnaAreaResponse execMeasMaintenanceAnalysisAreaTable(String companyId, Boolean returnAll);
}
