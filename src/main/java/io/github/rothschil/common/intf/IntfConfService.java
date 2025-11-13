package io.github.rothschil.common.intf;

import cn.hutool.core.util.ObjectUtil;
import io.github.rothschil.common.constant.Constant;
import io.github.rothschil.common.exception.CommonException;
import io.github.rothschil.common.response.enums.Status;
import io.github.rothschil.common.utils.NativeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;

/**
 * 接口配置的服务实例，考虑是读库的操作，需要将此表做成优先从缓存表中读取
 *
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @since 1.0.0
 */
@Slf4j
@Service
public class IntfConfService {

    @Value("${config.intf.swtich:false}")
    private boolean swtich;

    /** 查询出来，此处接口参数为 保留参数，没有实际意义，为缓存可以识别
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @param interfaceName  接口编码
     * @return IntfConfEntity>
     **/
    public IntfConfEntity getIntfData(String interfaceName){
        IntfConfEntity conf=null;
        return conf;
    }

    public void exchangeIpaddress(IntfConfEntity conf){
        // 处理一些特殊地址，例如集团地址等
        if(ObjectUtil.isNotNull(conf.getNamespace())){
            return ;
        }
        String dbConfig = conf.getAddress();
        String currrent = NativeUtil.ipNetworkSegment();
        // 接口配置地址与当前不在一个网段
        if(!dbConfig.contains(currrent)){
            try {
                URL url = new URL(dbConfig);
                String host = url.getHost();
                // 是否为 IP 数字地址而非域名
                if(!host.matches(Constant.IP_REGEX)){
                    return ;
                }
                int sourceIndex = host.lastIndexOf(Constant.POINT);
                String sourceUrlSuffix= host.substring(0,sourceIndex);

                int targetIndex = currrent.lastIndexOf(Constant.POINT);
                String targetUrlSuffix= currrent.substring(0,targetIndex);
                conf.setAddress(conf.getAddress().replace(sourceUrlSuffix,targetUrlSuffix));
            } catch (Exception e) {
                throw new CommonException(Status.API_NOT_FOUND_EXCEPTION,Constant.TURS_URL_ERR+":"+dbConfig);
            }
        }
    }

    /** 根据 KEY MAP 遍历
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @param interfaceName 接口编码
     * @return IntfConfEntity
     **/
    public IntfConfEntity getIntf(String interfaceName){
        IntfConfEntity conf = getIntfData(interfaceName);
        if(ObjectUtil.isNull(conf)){
            throw new RuntimeException(interfaceName +"[远程调用的接口未配置]");
        }
        return conf;
    }

}
