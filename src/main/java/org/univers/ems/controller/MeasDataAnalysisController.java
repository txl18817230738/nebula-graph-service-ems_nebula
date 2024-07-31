package org.univers.ems.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.univers.ems.influxdb.model.restlt.ResponseData;
import org.univers.ems.influxdb.model.restlt.SuccessResponseData;
import org.univers.ems.pojo.response.*;
import org.univers.ems.service.MeasDataAnalysisService;

/**
 * @author jie.xi
 */
@RestController
public class MeasDataAnalysisController {


    @Autowired
    MeasDataAnalysisService measDataAnalysisService;


    @RequestMapping("/meas_maintenance_substation")
    public ResponseMsg measMaintenanceSubstation(@RequestParam(name = "companyId") String companyId,
                                                  @RequestParam(name = "returnAll") Boolean returnAll) {
        ResponseMsg responseMsg = new ResponseMsg();
        MeasDataAnaSubstationResponse result = measDataAnalysisService.execMeasMaintenanceAnalysisSubstationTable(companyId,returnAll);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping("/meas_maintenance_unit")
    public ResponseMsg measMaintenanceUnit(@RequestParam(name = "companyId") String companyId,
                                            @RequestParam(name = "returnAll") Boolean returnAll) {
        ResponseMsg responseMsg = new ResponseMsg();
        MeasDataAnaUnitResponse result = measDataAnalysisService.execMeasMaintenanceAnalysisUnitTable(companyId,returnAll);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping("/meas_maintenance_load")
    public ResponseMsg measMaintenanceLoad(@RequestParam(name = "companyId") String companyId,
                                           @RequestParam(name = "returnAll") Boolean returnAll) {
        ResponseMsg responseMsg = new ResponseMsg();
        MeasDataAnaLoadResponse result = measDataAnalysisService.execMeasMaintenanceAnalysisLoadTable(companyId,returnAll);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping("/meas_maintenance_cp")
    public ResponseMsg measMaintenanceCP(@RequestParam(name = "companyId") String companyId,
                                           @RequestParam(name = "returnAll") Boolean returnAll) {
        ResponseMsg responseMsg = new ResponseMsg();
        MeasDataAnaCPResponse result = measDataAnalysisService.execMeasMaintenanceAnalysisCPTable(companyId,returnAll);
        responseMsg.setData(result);
        return responseMsg;
    }


    @RequestMapping("/meas_maintenance_cs")
    public ResponseMsg measMaintenanceCS(@RequestParam(name = "companyId") String companyId,
                                         @RequestParam(name = "returnAll") Boolean returnAll) {
        ResponseMsg responseMsg = new ResponseMsg();
        MeasDataAnaCSResponse result = measDataAnalysisService.execMeasMaintenanceAnalysisCSTable(companyId,returnAll);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping("/meas_maintenance_bus")
    public ResponseMsg measMaintenanceBus(@RequestParam(name = "companyId") String companyId,
                                         @RequestParam(name = "returnAll") Boolean returnAll) {
        ResponseMsg responseMsg = new ResponseMsg();
        MeasDataAnaBusResponse result = measDataAnalysisService.execMeasMaintenanceAnalysisBusTable(companyId,returnAll);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping("/meas_maintenance_trans3")
    public ResponseMsg measMaintenanceTrans3(@RequestParam(name = "companyId") String companyId,
                                          @RequestParam(name = "returnAll") Boolean returnAll) {
        ResponseMsg responseMsg = new ResponseMsg();
        MeasDataAnaTrans3Response result = measDataAnalysisService.execMeasMaintenanceAnalysisTrans3Table(companyId,returnAll);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping("/meas_maintenance_area")
    public ResponseMsg measMaintenanceArea(@RequestParam(name = "companyId") String companyId,
                                             @RequestParam(name = "returnAll") Boolean returnAll) {
        ResponseMsg responseMsg = new ResponseMsg();
        MeasDataAnaAreaResponse result = measDataAnalysisService.execMeasMaintenanceAnalysisAreaTable(companyId,returnAll);
        responseMsg.setData(result);
        return responseMsg;
    }
}
