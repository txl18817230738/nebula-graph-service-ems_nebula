package org.univers.ems.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.univers.ems.pojo.response.MeasDataAnaSubstationResponse;
import org.univers.ems.pojo.response.ResponseMsg;
import org.univers.ems.pojo.response.SeAllInfoAreaResponse;
import org.univers.ems.service.SeAllInforService;

/**
 * @author jie.xi
 */
@RestController
public class SeAllInfoController {

    @Autowired
    private SeAllInforService seAllInforService;

    @RequestMapping("/se_all_info_area")
    public ResponseMsg seAllInfoArea(@RequestParam(name = "companyId") String companyId,
                                     @RequestParam(name = "returnAll") Boolean returnAll) {
        ResponseMsg responseMsg = new ResponseMsg();
        SeAllInfoAreaResponse result = seAllInforService.execSeAllInfoArea(companyId,returnAll);
        responseMsg.setData(result);
        return responseMsg;
    }
}
