package io.github.rothschil.common.base.dto;

import lombok.Data;

/** HTTP 请求相应的内容封装
 *
 * @author HeD
 * @date 2022/9/7 20:16
 */
@Data
public class RestBean {
    /**
     * http状态码
     */
    Integer code;
    /**
     * 响应内容
     */
    String resp;

    /**
     * 备注描述
     */
    String remark;


    public RestBean() {

    }

    public RestBean(Integer code, String resp) {
        this.code = code;
        this.resp = resp;
    }

    public RestBean(Integer code, String resp, String remark) {
        this.code = code;
        this.resp = resp;
        this.remark = remark;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RestBean fail() {
        RestBean restBean = new RestBean();
        restBean.setCode(500);
        return restBean;
    }

}
