package com.smtl.edi.web.page;

/**
 *
 * @author nm
 */
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

/**
 * @param <T>
 */
public class Page<T> {

    /**
     *
     */
    public static final String ASC = "asc";

    /**
     *
     */
    public static final String DESC = "desc";

    /**
     *
     */
    protected int pageNo = 1;

    /**
     *
     */
    protected int pageSize = 1;

    /**
     *
     */
    protected String orderBy = null;

    /**
     *
     */
    protected String order = null;

    /**
     *
     */
    protected boolean autoCount = true;

    /**
     *
     */
    protected long pages;

    /**
     *
     */
    protected List<T> result = Lists.newArrayList();

    /**
     *
     */
    protected long totalCount = -1;

    /**
     *
     * @return
     */
    public long getPages() {
        return getTotalPages();
    }

    /**
     *
     */
    public Page() {
    }

    /**
     *
     * @param pageSize
     */
    public Page(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     *
     *
     * @return
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     *
     *
     * @param pageNo
     */
    public void setPageNo(final int pageNo) {
        this.pageNo = pageNo;

        if (pageNo < 1) {
            this.pageNo = 1;
        }
    }

    /**
     *
     * @param thePageNo
     * @return
     */
    public Page<T> pageNo(final int thePageNo) {
        setPageNo(thePageNo);
        return this;
    }

    /**
     *
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     *
     *
     * @param pageSize
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;

        if (pageSize < 1) {
            this.pageSize = 1;
        }
    }

    /**
     *
     * @param thePageSize
     * @return
     */
    public Page<T> pageSize(final int thePageSize) {
        setPageSize(thePageSize);
        return this;
    }

    /**
     *
     *
     * @return
     */
    public int getFirstResult() {
        return ((pageNo - 1) * pageSize) + 1;
    }

    /**
     *
     *
     * @return
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     *
     *
     * @param orderBy
     */
    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     *
     * @param theOrderBy
     * @return
     */
    public Page<T> orderBy(final String theOrderBy) {
        setOrderBy(theOrderBy);
        return this;
    }

    /**
     *
     *
     * @return
     */
    public String getOrder() {
        return order;
    }

    /**
     *
     *
     * @param order
     */
    public void setOrder(final String order) {
        //检查order字符串的合法值
        String[] orders = StringUtils.split(StringUtils.lowerCase(order), ',');
        if (orders != null) {
            for (String orderStr : orders) {
                if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
                    throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
                }
            }
        }

        this.order = StringUtils.lowerCase(order);
    }

    /**
     *
     * @param theOrder
     * @return
     */
    public Page<T> order(final String theOrder) {
        setOrder(theOrder);
        return this;
    }

    /**
     *
     *
     * @return
     */
    public boolean isOrderSetted() {
        return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
    }

    /**
     *
     *
     * @return
     */
    public boolean isAutoCount() {
        return autoCount;
    }

    /**
     *
     *
     * @param autoCount
     */
    public void setAutoCount(final boolean autoCount) {
        this.autoCount = autoCount;
    }

    /**
     *
     * @param theAutoCount
     * @return
     */
    public Page<T> autoCount(final boolean theAutoCount) {
        setAutoCount(theAutoCount);
        return this;
    }

    /**
     *
     *
     * @return
     */
    public List<T> getResult() {
        return result;
    }

    /**
     *
     *
     * @param result
     */
    public void setResult(final List<T> result) {
        this.result = result;
    }

    /**
     *
     *
     * @return
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     *
     *
     * @param totalCount
     */
    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     *
     *
     * @return
     */
    public long getTotalPages() {
        if (totalCount < 0) {
            return -1;
        }

        long count = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            count++;
        }
        return count;
    }

    /**
     *
     *
     * @return
     */
    public boolean isHasNext() {
        return (pageNo + 1 <= getTotalPages());
    }

    /**
     *
     *
     * @return
     */
    public int getNextPage() {
        if (isHasNext()) {
            return pageNo + 1;
        } else {
            return pageNo;
        }
    }

    /**
     *
     *
     * @return
     */
    public boolean isHasPre() {
        return (pageNo - 1 >= 1);
    }

    /**
     *
     *
     * @return
     */
    public int getPrePage() {
        if (isHasPre()) {
            return pageNo - 1;
        } else {
            return pageNo;
        }
    }
}
