package com.smtl.edi.web.svc;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nm
 */
@Transactional(rollbackFor = Exception.class)
public class BaseService {

    /**
     *
     */
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     *
     */
    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    public boolean exists(String sql, Map<String, String> params) {
        SqlRowSet srs = namedParameterJdbcTemplate.queryForRowSet(sql, params);
        return srs.next();
    }

    public int update(String sql, Map<String, Object> paraMap) {
        return namedParameterJdbcTemplate.update(sql, paraMap);
    }
}
