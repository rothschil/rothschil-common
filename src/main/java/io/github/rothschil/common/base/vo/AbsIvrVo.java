package io.github.rothschil.common.base.vo;

/**
 *  公司  和 话务流水 被叫区号
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public class AbsIvrVo {

    public AbsIvrVo() {
    }

    public AbsIvrVo(String caller, String serviceName, String companyId, String tranId, String subTranId, String areaId) {
        this.caller=caller;
        this.serviceName = serviceName;
        this.companyId = companyId;
        this.tranId = tranId;
        this.subTranId = subTranId;
        this.areaId = areaId;
    }

    /**
     * 主叫号码
     */
    public String caller;

    /**
     * 业务名称
     */
    public String serviceName;
    /**
     * 公司ID
     */
    public String companyId;

    /**
     * 流水号
     */
    public String tranId;

    /**
     * 子流水
     */
    public String subTranId;

    /**
     * 被叫区号
     */
    public String areaId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }

    public String getSubTranId() {
        return subTranId;
    }

    public void setSubTranId(String subTranId) {
        this.subTranId = subTranId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }
}
