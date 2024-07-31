package org.univers.ems.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.univers.ems.exception.FileParsingException;
import org.univers.ems.pojo.entity.influx.InsertParams;
import org.univers.ems.influxdb.model.restlt.ResponseData;
import org.univers.ems.influxdb.model.restlt.SuccessResponseData;
import org.univers.ems.pojo.request.WriteMeasCsvRequest;
import org.univers.ems.service.InfluxService;

/**
 * @author jie.xi
 */
@RestController
public class InfluxDbController {

    @Autowired
    private InfluxService influxService;

    /**
      * description: 时许数据库插入测试
      */
    @PostMapping("/influx/insert")
    public ResponseData insert(@RequestBody InsertParams insertParams) {
        influxService.insert(insertParams);
        return new SuccessResponseData();
    }

    @PostMapping("/influx/queue")
    public ResponseData queue() {
        return new SuccessResponseData(influxService.queue());
    }


    @PostMapping("/influx/meas/insert")
    public ResponseData insertMeas() throws FileParsingException {
        String filePath="C:\\Users\\jie.xi\\Desktop\\当前工作\\meas_v2.csv";
        influxService.insertMeas(filePath);
        return new SuccessResponseData("success");
    }

    @PostMapping("/influx/discrete/insert")
    public ResponseData insertDiscrete() throws FileParsingException {
        String filePath="C:\\Users\\jie.xi\\Desktop\\当前工作\\discrete_v2.csv";
        influxService.insertDiscrete(filePath);
        return new SuccessResponseData("success");
    }

    @PostMapping("/influx/meas/csv")
    public ResponseData writeMeasCsv(@RequestBody WriteMeasCsvRequest writeMeasCsvRequest) {
        influxService.writeMeasCSV(writeMeasCsvRequest.getTagValue());
        return new SuccessResponseData("success");
    }
}
