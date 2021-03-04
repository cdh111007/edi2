package com.smtl.edi.web.pojo;

/**
 *
 * @author nm
 */
public class UserCoperLink {

    private String cstCode;
    private String ctnOperator;
    private String delete;

    public UserCoperLink() {
    }

    public UserCoperLink(String cstCode, String ctnOperator) {
        this.cstCode = cstCode;
        this.ctnOperator = ctnOperator;
    }

    /**
     *
     * @return
     */
    public String getDelete() {
        return delete;
    }

    /**
     *
     * @param delete
     */
    public void setDelete(String delete) {
        this.delete = delete;
    }

    /**
     *
     * @return
     */
    public String getCstCode() {
        return cstCode;
    }

    /**
     *
     * @param cstCode
     */
    public void setCstCode(String cstCode) {
        this.cstCode = cstCode;
    }

    /**
     *
     * @return
     */
    public String getCtnOperator() {
        return ctnOperator;
    }

    /**
     *
     * @param ctnOperator
     */
    public void setCtnOperator(String ctnOperator) {
        this.ctnOperator = ctnOperator;
    }

    @Override
    public String toString() {
        return "UserCopercdLink{" + "cstcode=" + cstCode + ", copercd=" + ctnOperator + '}';
    }

}
