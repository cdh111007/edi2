package com.smtl.edi.web.svc;

import com.smtl.edi.web.jdbc.JdbcObjectWrapper;
import com.smtl.edi.web.page.JdbcPaginationHelper;
import com.smtl.edi.web.page.Page;
import com.smtl.edi.web.pojo.Coarri;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nm
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CoarriService extends BaseService {

    /**
     *
     * @param countsql
     * @param listsql
     * @param pageNo
     * @return
     */
    public Page<Coarri> getPage(String countsql, String listsql, int pageNo) {

        JdbcPaginationHelper<Coarri> JdbcPaginationHelper = new JdbcPaginationHelper<>();
        return JdbcPaginationHelper.fetchPage(jdbcTemplate, countsql, listsql, pageNo,
                new CoarriMapper());
    }

    /**
     *
     */
    class CoarriMapper implements ParameterizedRowMapper<Coarri> {

        @Override
        public Coarri mapRow(ResultSet rs, int rowNum) throws SQLException {
            return JdbcObjectWrapper.coarriWrapper(rs);
        }

    }
}
