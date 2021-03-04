package com.smtl.edi.web.page;

/**
 *
 * @author nm
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 *
 * @author nm
 * @param <T>
 */
public class JdbcPaginationHelper<T> {

    private static final int DEFAULT_PAGE_SIZE = 30;

    /**
     *
     * @param jdbcTemplate
     * @param countSql
     * @param dataSql
     * @param pageNo
     * @param rowMapper
     * @return
     */
    public Page<T> fetchPage(final JdbcTemplate jdbcTemplate, final String countSql, final String dataSql,
            final int pageNo, final ParameterizedRowMapper<T> rowMapper) {
        return fetchPage(jdbcTemplate, countSql, dataSql, pageNo, DEFAULT_PAGE_SIZE, rowMapper);
    }

    /**
     *
     * @param jdbcTemplate
     * @param countSql
     * @param dataSql
     * @param pageNo
     * @param pageSize
     * @param rowMapper
     * @return
     */
    public Page<T> fetchPage(final JdbcTemplate jdbcTemplate, final String countSql, String dataSql,
            int pageNo, final int pageSize, final ParameterizedRowMapper<T> rowMapper) {

        if (pageNo <= 0) {
            pageNo = 1;
        }

        int rowCount = jdbcTemplate.queryForObject(countSql, Integer.class);

        final Page<T> page = new Page<>();
        page.setPageNo(pageNo);
        page.setTotalCount(rowCount);
        page.setPageSize(pageSize);

        int firstResult = page.getFirstResult();

        dataSql = "select * from "
                + "(select row_.*,rownum rownum_ "
                + "from ( " + dataSql + " ) row_ "
                + "where rownum <= " + pageSize * pageNo + " ) "
                + "where rownum_ >= " + firstResult;

        jdbcTemplate.query(dataSql, new ResultSetExtractor<Page<T>>() {
            @Override
            public Page<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
                final List<T> pageItems = page.getResult();
                int currentRow = 0;
                while (rs.next()) {
                    pageItems.add(rowMapper.mapRow(rs, currentRow));
                    currentRow++;
                }
                page.setResult(pageItems);
                return page;
            }
        });
        return page;
    }

}
