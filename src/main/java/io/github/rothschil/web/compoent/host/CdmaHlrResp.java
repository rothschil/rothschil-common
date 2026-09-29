package io.github.rothschil.web.compoent.host;


import io.github.rothschil.common.base.vo.BaseResp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CdmaHlrResp extends BaseResp {

    /**
     * 主键生成策略（主键自增）
     */
    Long id;
    String location1;
    String location2;
    String phoneprefix;



}
