package com.smtl.edi.web.svc;

import com.smtl.edi.web.jdbc.JdbcObjectWrapper;
import com.smtl.edi.web.page.JdbcPaginationHelper;
import com.smtl.edi.web.page.Page;
import com.smtl.edi.web.pojo.SenderSetting;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
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
@Transactional(rollbackFor = Exception.class)
public class SenderSettingService extends BaseService {



    /**
     *
     * @param setting
     * @param sql
     * @return
     */
    public int insert(SenderSetting setting, String sql) {
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(setting);
        return namedParameterJdbcTemplate.update(sql, paramSource);
    }

    /**
     *
     * @param sql
     * @return
     */
    public List<SenderSetting> getAll(String sql) {
        List<SenderSetting> settings = jdbcTemplate.query(sql, new RowMapper<SenderSetting>() {
            @Override
            public SenderSetting mapRow(ResultSet rs, int i) throws SQLException {
                return JdbcObjectWrapper.senderWrapper(rs);
            }

        });
        return settings;
    }

    /**
     *
     * @param sql
     * @return
     */
    public SenderSetting getOne(String sql) {
        SenderSetting setting = jdbcTemplate.query(sql, new ResultSetExtractor<SenderSetting>() {
            @Override
            public SenderSetting extractData(ResultSet rs) throws SQLException, DataAccessException {

                if (rs.next()) {
                    return JdbcObjectWrapper.senderWrapper(rs);
                }
                return null;

            }

        });
        return setting;
    }

    /**
     *
     * @param countsql
     * @param listsql
     * @param pageNo
     * @return
     */
    public Page<SenderSetting> getPage(String countsql, String listsql, int pageNo) {

        JdbcPaginationHelper<SenderSetting> JdbcPaginationHelper = new JdbcPaginationHelper<>();
        return JdbcPaginationHelper.fetchPage(jdbcTemplate, countsql, listsql, pageNo,
                new SenderSettingMapper());
    }

    /**
     *
     */
    class SenderSettingMapper implements ParameterizedRowMapper<SenderSetting> {

        @Override
        public SenderSetting mapRow(ResultSet rs, int rowNum) throws SQLException {
            return JdbcObjectWrapper.senderWrapper(rs);
        }

    }
}
