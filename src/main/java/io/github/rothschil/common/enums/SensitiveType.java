package io.github.rothschil.common.enums;


/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/
public enum SensitiveType {

    /** 手机号：保留前3后4，中间打码 */
    PHONE,

    /** 身份证号：保留前6后4，中间打码 */
    ID_CARD,

    /** 银行卡号：只保留后4位，前面全打码（仅处理纯数字字符串） */
    BANK_CARD
}
