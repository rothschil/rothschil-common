package xyz.wongs.drunkard.alipay.model.result;

import com.alipay.api.response.AlipayTradeQueryResponse;
import xyz.wongs.drunkard.alipay.model.TradeStatus;

/** 当面付2.0查询应答
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @description //TODO
 * @github <a>https://github.com/rothschil</a>
 * @date 2021/9/23 - 10:09
 * @version 1.0.0
 */
public class AlipayF2FQueryResult implements Result {
    private TradeStatus tradeStatus;
    private AlipayTradeQueryResponse response;

    public AlipayF2FQueryResult(AlipayTradeQueryResponse response) {
        this.response = response;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public void setResponse(AlipayTradeQueryResponse response) {
        this.response = response;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public AlipayTradeQueryResponse getResponse() {
        return response;
    }

    @Override
    public boolean isTradeSuccess() {
        return response != null &&
                TradeStatus.SUCCESS.equals(tradeStatus);
    }
}