package org.univers.ems.service;

import org.univers.ems.pojo.response.SeAllInfoAreaResponse;

public interface SeAllInforService {
    SeAllInfoAreaResponse execSeAllInfoArea(String companyId, Boolean returnAll);
}
