package io.github.rothschil.common.base.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 请求
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@Data
@NoArgsConstructor
public class BaseReq extends AbsBaseReq {

    /**
     * 服务名
     */
    String serviceName;

    /**
     * 预处理
     *
     * @return
     */
    public boolean before() {
        return true;
    }
}
