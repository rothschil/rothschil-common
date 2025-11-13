package io.github.rothschil.common.enums;


import io.github.rothschil.common.constant.Constant;

/**
 * 为了应对江苏大区对接口进行反向代理出来两套地址<br/>
 * <ui>
 *     <li>吉山机房80段</li>
 *     <li>仪征机房70段</li>
 * </ui>
 *
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public enum EnvironmentEnums {

    /**
     * 主环境
     */
    MASTER(Constant.IPADDRESS_MASTR),

    /**
     * 备用环境
     */
    SLAVE(Constant.IPADDRESS_SLAVE);

    private String code;

    EnvironmentEnums(String code) {
        this.code = code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
