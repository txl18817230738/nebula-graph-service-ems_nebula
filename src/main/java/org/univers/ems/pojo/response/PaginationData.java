package org.univers.ems.pojo.response;

import lombok.Data;

import java.util.List;

/**
 * @author jie.xi
 */
@Data
public class PaginationData {

    private List<Object> data;

    private Integer pageSize;

    private Integer currentPage;

    private Integer totalSize;

}
