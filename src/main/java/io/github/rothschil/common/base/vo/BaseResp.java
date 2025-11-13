package io.github.rothschil.common.base.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 响应
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @since 1.0.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class BaseResp {


    public static String SUC = "0";


    public static String ERR = "1";

    @Builder.Default
    public String oRet = SUC;

    public String msg;

    public String remark;

    public BaseResp(String oRet) {
        this.oRet = oRet;
    }

    public void setSuccess() {
        oRet = SUC;
    }

    public void setErr() {
        oRet = ERR;
    }

    public boolean isSuccess() {
        return SUC.equals(oRet);
    }

    public static BaseResp fail() {
        BaseResp resp = new BaseResp();
        resp.setErr();
        return resp;
    }


    public boolean after() {
        return true;
    }

    public static <T extends BaseResp> T fail(Class<T> clz, String msg) {
        T t = null;
        try {
            t = clz.newInstance();
            t.setErr();
            t.msg = msg;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return t;

    }

}
