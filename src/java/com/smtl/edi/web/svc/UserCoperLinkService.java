package com.smtl.edi.web.svc;

import com.smtl.edi.web.jdbc.JdbcObjectWrapper;
import com.smtl.edi.web.page.JdbcPaginationHelper;
import com.smtl.edi.web.page.Page;
import com.smtl.edi.web.pojo.UserCoperLink;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nm
 */
@Service
public class UserCoperLinkService extends BaseService {

    /**
     *
     * @param link
     * @param sql
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int update(final UserCoperLink link, String sql) {
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(link);
        return namedParameterJdbcTemplate.update(sql, paramSource);
    }

    /**
     *
     * @param sql
     * @return
     */
    public UserCoperLink getOne(String sql) {
        UserCoperLink link = jdbcTemplate.query(sql, new ResultSetExtractor<UserCoperLink>() {
            @Override
            public UserCoperLink extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return JdbcObjectWrapper.userCoperLinkWrapper(rs);
                }
                return null;
            }

        });
        return link;
    }

    /**
     *
     * @param countsql
     * @param listsql
     * @param pageNo
     * @return
     */
    public Page<UserCoperLink> getPage(String countsql, String listsql, int pageNo) {

        JdbcPaginationHelper<UserCoperLink> JdbcPaginationHelper = new JdbcPaginationHelper<>();
        return JdbcPaginationHelper.fetchPage(jdbcTemplate, countsql, listsql, pageNo,
                new UserCoperLinkMapper());
    }

    /**
     *
     */
    class UserCoperLinkMapper implements ParameterizedRowMapper<UserCoperLink> {

        @Override
        public UserCoperLink mapRow(ResultSet rs, int rowNum) throws SQLException {
            return JdbcObjectWrapper.userCoperLinkWrapper(rs);
        }

    }
}
