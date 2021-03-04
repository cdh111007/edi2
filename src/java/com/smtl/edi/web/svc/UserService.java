package com.smtl.edi.web.svc;

/**
 *
 * @author nm
 */
import com.smtl.edi.web.jdbc.JdbcObjectWrapper;
import com.smtl.edi.web.page.JdbcPaginationHelper;
import com.smtl.edi.web.page.Page;
import com.smtl.edi.web.pojo.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class UserService extends BaseService {

    /**
     *
     * @param user
     * @param sql
     * @return
     */
    public int insert(User user, String sql) {
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);
        return namedParameterJdbcTemplate.update(sql, paramSource);
    }

    /**
     *
     * @param sql
     * @return
     */
    public List<User> getAll(String sql) {
        List<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                return JdbcObjectWrapper.userWrapper(rs);
            }
            
        });
        return users;
    }

    /**
     *
     * @param sql
     * @return
     */
    public List<User> getAllCodes(String sql) {
        List<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int i) throws SQLException {
                return JdbcObjectWrapper.userCodeWrapper(rs);
            }
            
        });
        Set<User> tmp = new HashSet<>();
        tmp.addAll(users);
        List<User> result = new ArrayList(tmp);
        Collections.sort(result, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        });
        return result;
    }

    /**
     *
     * @return
     */
    public List<User> getAll() {
        String sql = "select * from tc2_edi_cust_info  order by cst_code";
        return getAll(sql);
    }

    /**
     *
     * @return
     */
    public List<User> getAllCodes() {
        String sql = "select distinct cst_code,cst_name from tc2_edi_cust_info  order by cst_code";
        return getAllCodes(sql);
    }

    /**
     *
     * @param sql
     * @return
     */
    public User getOne(String sql) {
        User user = jdbcTemplate.query(sql, new ResultSetExtractor<User>() {
            @Override
            public User extractData(ResultSet rs) throws SQLException, DataAccessException {
                
                if (rs.next()) {
                    return JdbcObjectWrapper.userWrapper(rs);
                }
                return null;
                
            }
            
        });
        return user;
    }

    /**
     *
     * @param countsql
     * @param listsql
     * @param pageNo
     * @return
     */
    public Page<User> getPage(String countsql, String listsql, int pageNo) {
        
        JdbcPaginationHelper<User> JdbcPaginationHelper = new JdbcPaginationHelper<>();
        return JdbcPaginationHelper.fetchPage(jdbcTemplate, countsql, listsql, pageNo,
                new UserMapper());
    }

    /**
     *
     */
    class UserMapper implements ParameterizedRowMapper<User> {
        
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return JdbcObjectWrapper.userWrapper(rs);
        }
        
    }
}
