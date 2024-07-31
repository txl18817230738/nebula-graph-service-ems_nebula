package org.univers.ems.service;

import org.univers.ems.pojo.response.*;

/**
 * @author jie.xi
 *
 * 节点开关图的service
 *
 */
public interface SldService {
    /**
     * @author jie.xi
     * sld_breaker_cime.gsql相关
     */
    PaginationData execSldBreakerTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverViewResponse execSldBreakerOverview(String companyId);

    PaginationData execSldDisconnectorTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverViewResponse execSldDisconnectorOverview(String companyId);

    PaginationData execSldUnitTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverViewResponse execSldUnitOverview(String companyId);

    OverviewTableSubstation execSldSubstationTableOverview(String companyId);

    PaginationData execSldAclineDotTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverViewResponse execSldAclineDotOverview(String companyId);

    PaginationData execSldTransTwoTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverviewTableTrans execSldTransTwoOverview(String companyId);

    PaginationData execSldTransThreeTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverviewTableTrans execSldTransThreeOverview(String companyId);

    PaginationData execSldCpTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverViewResponse execSldCpOverview(String companyId);

    PaginationData execSldCsTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverViewResponse execSldCsOverview(String companyId);

    PaginationData execSldAclineTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverviewTableAcline execSldAclineOverview(String companyId);

    PaginationData execSldLoadTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverViewResponse execSldLoadOverview(String companyId);

    PaginationData execSldBusTable(String companyId, String volt, Integer currentPage, Integer pageSize, String rankColumn, String rank);

    OverViewResponse execSldBusOverview(String companyId);
}
