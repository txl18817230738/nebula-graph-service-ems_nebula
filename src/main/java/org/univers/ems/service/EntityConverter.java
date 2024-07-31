package org.univers.ems.service;

import com.vesoft.nebula.client.graph.data.ResultSet;

/**
 * @author jie.xi
 */
@FunctionalInterface
public interface EntityConverter<T> {
    T convert(ResultSet.Record record);
}