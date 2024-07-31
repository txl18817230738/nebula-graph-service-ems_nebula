package org.univers.ems.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.univers.ems.constants.Constants;
import org.univers.ems.pojo.response.*;
import org.univers.ems.service.SldService;


/**
 * @author jie.xi
 *
 * 节点开关图的controller，前端请求的入口
 *
 */
@RestController
@CrossOrigin
public class SldController {


    @Autowired
    private SldService sldService;

    @RequestMapping(value = "/sld_breaker_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldBreakerTable(@RequestParam(name = "companyId") String companyId,
                                       @RequestParam(name = "volt") String volt,
                                       @RequestParam(name = "currentPage") Integer currentPage,
                                       @RequestParam(name = "pageSize") Integer pageSize,
                                       @RequestParam(name = "rankColumn") String rankColumn,
                                       @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldBreakerTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_breaker_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldBreakerOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverViewResponse overViewResponse = sldService.execSldBreakerOverview(companyId);
        responseMsg.setData(overViewResponse);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_disconnector_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldDisconnectorTable(@RequestParam(name = "companyId") String companyId,
                                       @RequestParam(name = "volt") String volt,
                                       @RequestParam(name = "currentPage") Integer currentPage,
                                       @RequestParam(name = "pageSize") Integer pageSize,
                                       @RequestParam(name = "rankColumn") String rankColumn,
                                       @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldDisconnectorTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_disconnector_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldDisconnectorOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverViewResponse overViewResponse = sldService.execSldDisconnectorOverview(companyId);
        responseMsg.setData(overViewResponse);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_unit_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldUnitTable(@RequestParam(name = "companyId") String companyId,
                                            @RequestParam(name = "volt") String volt,
                                            @RequestParam(name = "currentPage") Integer currentPage,
                                            @RequestParam(name = "pageSize") Integer pageSize,
                                            @RequestParam(name = "rankColumn") String rankColumn,
                                            @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldUnitTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_unit_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldUnitOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverViewResponse overViewResponse = sldService.execSldUnitOverview(companyId);
        responseMsg.setData(overViewResponse);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_substation_table_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldSubstationTableOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverviewTableSubstation result = sldService.execSldSubstationTableOverview(companyId);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_acline_dot_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldAclineDotTable(@RequestParam(name = "companyId") String companyId,
                                    @RequestParam(name = "volt") String volt,
                                    @RequestParam(name = "currentPage") Integer currentPage,
                                    @RequestParam(name = "pageSize") Integer pageSize,
                                    @RequestParam(name = "rankColumn") String rankColumn,
                                    @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldAclineDotTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_acline_dot_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldAclineDotOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverViewResponse overViewResponse = sldService.execSldAclineDotOverview(companyId);
        responseMsg.setData(overViewResponse);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_trans_two_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldTransTwoTable(@RequestParam(name = "companyId") String companyId,
                                         @RequestParam(name = "volt") String volt,
                                         @RequestParam(name = "currentPage") Integer currentPage,
                                         @RequestParam(name = "pageSize") Integer pageSize,
                                         @RequestParam(name = "rankColumn") String rankColumn,
                                         @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldTransTwoTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_trans_two_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldTransTwoOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverviewTableTrans overviewTableTrans = sldService.execSldTransTwoOverview(companyId);
        responseMsg.setData(overviewTableTrans);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_trans_three_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldTransThreeTable(@RequestParam(name = "companyId") String companyId,
                                         @RequestParam(name = "volt") String volt,
                                         @RequestParam(name = "currentPage") Integer currentPage,
                                         @RequestParam(name = "pageSize") Integer pageSize,
                                         @RequestParam(name = "rankColumn") String rankColumn,
                                         @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldTransThreeTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_trans_three_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldTransThreeOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverviewTableTrans overViewResponse = sldService.execSldTransThreeOverview(companyId);
        responseMsg.setData(overViewResponse);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_cp_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldCpTable(@RequestParam(name = "companyId") String companyId,
                                          @RequestParam(name = "volt") String volt,
                                          @RequestParam(name = "currentPage") Integer currentPage,
                                          @RequestParam(name = "pageSize") Integer pageSize,
                                          @RequestParam(name = "rankColumn") String rankColumn,
                                          @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldCpTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_cp_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldCpOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverViewResponse overViewResponse = sldService.execSldCpOverview(companyId);
        responseMsg.setData(overViewResponse);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_cs_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldCsTable(@RequestParam(name = "companyId") String companyId,
                                  @RequestParam(name = "volt") String volt,
                                  @RequestParam(name = "currentPage") Integer currentPage,
                                  @RequestParam(name = "pageSize") Integer pageSize,
                                  @RequestParam(name = "rankColumn") String rankColumn,
                                  @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldCsTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_cs_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldCsOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverViewResponse overViewResponse = sldService.execSldCsOverview(companyId);
        responseMsg.setData(overViewResponse);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_acline_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldAclineTable(@RequestParam(name = "companyId") String companyId,
                                  @RequestParam(name = "volt") String volt,
                                  @RequestParam(name = "currentPage") Integer currentPage,
                                  @RequestParam(name = "pageSize") Integer pageSize,
                                  @RequestParam(name = "rankColumn") String rankColumn,
                                  @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldAclineTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_acline_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldAclineOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverviewTableAcline overViewResponse = sldService.execSldAclineOverview(companyId);
        responseMsg.setData(overViewResponse);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_load_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldLoadTable(@RequestParam(name = "companyId") String companyId,
                                      @RequestParam(name = "volt") String volt,
                                      @RequestParam(name = "currentPage") Integer currentPage,
                                      @RequestParam(name = "pageSize") Integer pageSize,
                                      @RequestParam(name = "rankColumn") String rankColumn,
                                      @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldLoadTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_load_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldLoadOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverViewResponse overViewResponse = sldService.execSldLoadOverview(companyId);
        responseMsg.setData(overViewResponse);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_bus_table", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldBusTable(@RequestParam(name = "companyId") String companyId,
                                      @RequestParam(name = "volt") String volt,
                                      @RequestParam(name = "currentPage") Integer currentPage,
                                      @RequestParam(name = "pageSize") Integer pageSize,
                                      @RequestParam(name = "rankColumn") String rankColumn,
                                      @RequestParam(name = "rank") String rank) {
        ResponseMsg responseMsg = new ResponseMsg();
        PaginationData result = sldService.execSldBusTable(companyId,volt,currentPage,pageSize,rankColumn,rank);
        responseMsg.setData(result);
        return responseMsg;
    }

    @RequestMapping(value = "/sld_bus_overview", produces = Constants.APPLICATION_JSON)
    public ResponseMsg sldBusOverview(@RequestParam(name = "companyId") String companyId) {
        ResponseMsg responseMsg = new ResponseMsg();
        OverViewResponse overViewResponse = sldService.execSldBusOverview(companyId);
        responseMsg.setData(overViewResponse);
        return responseMsg;
    }
}
