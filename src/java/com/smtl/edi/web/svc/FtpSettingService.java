package com.smtl.edi.web.svc;

import com.smtl.edi.web.jdbc.JdbcObjectWrapper;
import com.smtl.edi.web.page.JdbcPaginationHelper;
import com.smtl.edi.web.page.Page;
import com.smtl.edi.web.pojo.FtpSetting;
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
public class FtpSettingService extends BaseService {

    /**
     *
     * @param sql
     * @return
     */
    public List<FtpSetting> getAll(String sql) {
        List<FtpSetting> ftps = jdbcTemplate.query(sql, new RowMapper<FtpSetting>() {
            @Override
            public FtpSetting mapRow(ResultSet rs, int i) throws SQLException {
                return JdbcObjectWrapper.ftpWrapper(rs);
            }

        });
        return ftps;
    }

    /**
     *
     * @param countsql
     * @param listsql
     * @param pageNo
     * @return
     */
    public Page<FtpSetting> getPage(String countsql, String listsql, int pageNo) {

        JdbcPaginationHelper<FtpSetting> JdbcPaginationHelper = new JdbcPaginationHelper<>();
        return JdbcPaginationHelper.fetchPage(jdbcTemplate, countsql, listsql, pageNo,
                new FtpMapper());
    }

    /**
     *
     */
    class FtpMapper implements ParameterizedRowMapper<FtpSetting> {

        @Override
        public FtpSetting mapRow(ResultSet rs, int rowNum) throws SQLException {
            return JdbcObjectWrapper.ftpWrapper(rs);
        }

    }

    /**
     *
     * @param ftp
     * @param sql
     * @return
     */
    public int insert(FtpSetting ftp, String sql) {
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(ftp);
        return namedParameterJdbcTemplate.update(sql, paramSource);
    }

    /**
     *
     * @param sql
     * @return
     */
    public FtpSetting getOne(String sql) {
        FtpSetting setting = jdbcTemplate.query(sql, new ResultSetExtractor<FtpSetting>() {
            @Override
            public FtpSetting extractData(ResultSet rs) throws SQLException, DataAccessException {

                if (rs.next()) {
                    return JdbcObjectWrapper.ftpWrapper(rs);
                }
                return null;

            }

        });
        return setting;
    }

}
