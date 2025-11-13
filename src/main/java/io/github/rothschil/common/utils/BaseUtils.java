package io.github.rothschil.common.utils;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.github.rothschil.common.base.dto.AmazTuple;
import io.github.rothschil.common.base.dto.RestBean;
import io.github.rothschil.common.base.vo.AbsIvrVo;
import io.github.rothschil.common.base.vo.RequestHeaderVo;
import io.github.rothschil.common.constant.Constant;
import io.github.rothschil.common.handler.IntfLog;
import io.github.rothschil.common.intf.IntfConfEntity;
import io.github.rothschil.common.intf.IntfConfService;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/** 修订枚举
* @description: TODO
* @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
* @date 2025/5/15 22:28
* @version 1.0
*/
public abstract class BaseUtils {

    protected static final Logger log = LoggerFactory.getLogger(BaseUtils.class);


    /**
     * 配置工厂
     * @param timeout 超时时间
     * @return  RestTemplate
     */
    protected static RestTemplate getRestTemplate(Integer timeout) {
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(timeout);
//        return new RestTemplate(requestFactory);
        RestTemplate restTemplate = new RestTemplate();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(timeout);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    /** 构建超时时间，如果没有配置，则启用 5 秒
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfConf  http配置实例
     * @return int  具体超时时间
     **/
    protected static int getTimeOut(IntfConfEntity intfConf){
        int timeCout = intfConf.getTimeout();
        if(timeCout<1200){
            timeCout = 2500;
        }
        return timeCout;
    }

    /** 获取接口详细实例
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @date 2025/5/15 10:10
     * @param intfCode  接口信息
     * @return cn.ffcs.up.common.intf.IntfConfEntity
     **/
    protected static IntfConfEntity getIntfConf(String intfCode) {
        IntfConfService intfConfService;
        synchronized ("itfConfService_class") {
            intfConfService = SpringUtil.getBean(IntfConfService.class);
        }
        return intfConfService.getIntf(intfCode);
    }

    /**
     * 格式化打印，方便 <b>logstash</b> 分词处理
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfConf  接口信息
     * @param json  请求入参
     * @param body  响应内容
     * @param procTime  耗时
     **/
    protected static void printConsoleLog(IntfConfEntity intfConf,String address, String json,String body,long procTime){
        Object obj = UserTransmittableUtils.get();
        String transId ="UNKNOW";
        if(obj instanceof RequestHeaderVo){
            RequestHeaderVo headerVo = (RequestHeaderVo)obj;
            transId  = headerVo.getCallId();
        }
        if(obj instanceof AbsIvrVo){
            AbsIvrVo ivrVo = (AbsIvrVo)obj;
            transId  = ivrVo.tranId;
        }
        log.warn("{}\n[IntfName]\n{}\n[IntfDesc]\n{}\n[IntfUrl]\n{}\n[RequesetBody]\n{}\n[ResponseBody]\n{}\n[CostTime]\n{}ms\n",transId, intfConf.getInterfaceName(), intfConf.getRemark(), address, json, body, procTime);
    }

    /**
     * 请求发生前
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfLog   交互日志实例
     * @param restBean  响应Bean
     * @param start 开始时间
     **/
    protected static void afterBuildIntfLog(IntfLog intfLog, RestBean restBean, long start){
        afterBuildIntfLog(intfLog, JSONUtil.toJsonStr(restBean.getResp()),start,restBean.getCode()+"");
    }

    /**
     * 请求发生前
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfLog   交互日志实例
     * @param restBean  响应Bean
     * @param start 开始时间
     * @param state 交互的状态
     **/
    private static void afterBuildIntfLog(IntfLog intfLog, String restBean, long start,String state){
        if(ObjectUtil.isEmpty(intfLog)){
            return ;
        }
        long end = System.currentTimeMillis();
        intfLog.setProcTime(end-start);
        intfLog.setRespTime(new Date());
        intfLog.setState(state);
        intfLog.setRespData(restBean);
        // CompletableFuture.runAsync(()-> {
        //     if (null == APPLOG_QUEUE) {
        //         synchronized ("APPLOG_QUEUE_CLAZZ") {
        //             SpringUtil.getBean(AppLogQueue.class);
        //         }
        //     }
        //     APPLOG_QUEUE.addQueue(intfLog);
        // });
    }


    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @date 2025/5/15 11:05
     * @param tuple
     * @return cn.ffcs.up.common.base.dto.RestBean
     **/
    protected static RestBean getRestBean(AmazTuple tuple) {
        RestBean restBean = new RestBean();
        ResponseEntity<String> exchange = (ResponseEntity)tuple.fp;
        if (exchange == null) {
            restBean.setCode(500);
            restBean.setResp("对外请求异常");
        } else {
            restBean.setCode(exchange.getStatusCodeValue());
            restBean.setResp(exchange.getBody());
        }
        restBean.setRemark(tuple.st.toString());
        return restBean;
    }


    /** Post 请求处理
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param restTemplate  RestTemplate实例
     * @param intfConf  接口实例
     * @param address   目标地址
     * @param json  请求内容，JSON格式
     * @param httpHeaders   Http头信息
     * @param method    HttpMethod请求方式
     * @return cn.ffcs.up.common.base.dto.AmazTuple
     **/
    protected static AmazTuple postResponseEntity(RestTemplate restTemplate, IntfConfEntity intfConf, String address, String json, HttpHeaders httpHeaders, HttpMethod method){

        ResponseEntity<String> exchange = null;
        HttpEntity<String> requestEntity = new HttpEntity<>(json, httpHeaders);
        String errMsg= Constant.HTTP_ERR_MSG_DEFAULT;
        long beginTime = System.currentTimeMillis();
        long end;
        long costTime;
        String remark;
        try {
            if (httpHeaders.containsKey("POST")) {
                method = HttpMethod.POST;
            }
            exchange = restTemplate.exchange(address, method, requestEntity, String.class);
            int statusCodeValue = exchange.getStatusCodeValue();
            if (statusCodeValue == 429) {
                HttpHeaders headers = exchange.getHeaders();
                String rate = headers.getFirst("X-RateLimit-Reset");
                log.warn("429 Too Many Requests,X-RateLimit-Reset:{},{}秒后重新请求", rate, rate);
                costTime = System.currentTimeMillis() - beginTime;
                remark = "["+address+"] Request time is "+costTime +" ms";
                return new AmazTuple(exchange,remark,null);
            }
        } catch (ResourceAccessException exception) {
            errMsg="["+address+"] Address Cannot Be Accessed! Error Messsage "+exception.getMessage();
        } catch (HttpStatusCodeException exception) {
            HttpStatusCode statusCode = exception.getStatusCode();
            errMsg="["+address+ "] ,HttpStatus Value "+statusCode.value() +" ;Error Messsage "+exception.getMessage();
        } catch (Exception e) {
            errMsg="Exception "+TextUtil.exToStr(e);
        }
        String body;
        body = null != exchange ? exchange.getBody() : errMsg;
        costTime = System.currentTimeMillis() - beginTime;
        printConsoleLog(intfConf, address, json,body,costTime);
        if(ObjectUtil.isEmpty(exchange)){
            exchange = new ResponseEntity(body,HttpStatus.PRECONDITION_FAILED);
        }
        remark = "["+address+"] Request Time is "+costTime +" ms; However, The Timeout Is Set To "+intfConf.getTimeout()+" ms.";
        return new AmazTuple(exchange,remark,null);
    }

    /**
     * 对外封装 WebService 的调用<br/>
     *
     * 1、nameSpaceHeadersMap 设置内容如下
     * <pre>
     * Map<String, String> nameSpaceHeadersMap = new LinkedHashMap();
     * nameSpaceHeadersMap.put("xsi", "<a href="http://www.w3.org/2001/XMLSchema-instance">...</a>");
     * nameSpaceHeadersMap.put("xsd", "http://www.w3.org/2001/XMLSchema");
     * nameSpaceHeadersMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
     * </pre>
     *
     *
     * 2、soapClient.getMessage().getSOAPPart().getEnvelope().addNamespaceDeclaration(header.getKey(), header.getValue());
     * <pre>
     * </pre>
     *
     * 3、mapMethod 设置内容如下
     * <pre>
     *  soapClient.setMethod("impl:excuteResult","http://impl.service.label.webservice.timesontransfar.com");
     *
     *  soapClient.setMethod(key,mapMethod.get(key));
     * </pre>
     *
     * 4、mapMethodEle 设置内容如下
     * <pre>
     * methodEle.setAttribute("soapenv:encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
     * </pre>
     *
     * 5、mapSoapElement 设置内容如下
     * <pre>
     * soapElement.setAttribute("xsi:type", "soapenc:string");
     * soapElement.setAttribute("xmlns:soapenc", "http://schemas.xmlsoap.org/soap/encoding/");
     * </pre>
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param xmlContent   请求XMl报文提体
     * @param intfConfEntity    接口配置，用于获取Http的 地址 和 请求 Header信息
     * @param nameSpaceHeadersMap  命名空间
     * @param mapMethod 执行方法名
     * @param mapMethodEle  方法元素
     * @param childElement  子节点
     * @param mapSoapElement    SOAP元素节点
     * @return RestBean
     *
     **/
    protected static SoapClient post2Webservice(String xmlContent, IntfConfEntity intfConfEntity,
                                             Map<String, String> nameSpaceHeadersMap,
                                             Map<String, String> mapMethod,
                                             Map<String, String> mapMethodEle,
                                             String childElement,
                                             Map<String, String> mapSoapElement) {
        SoapClient soapClient = null;
        Map<String,String> map = JSON.parseObject(intfConfEntity.getHeaderInfo(), new TypeReference<HashMap<String, String>>() {});
        try {
            soapClient = SoapClient.create(intfConfEntity.getAddress()).header("SOAPAction","application/soap+xml;charset=utf-8").headerMap(map,true)
                    .setReadTimeout(intfConfEntity.getTimeout())
                    .setConnectionTimeout(intfConfEntity.getTimeout());
            for (Map.Entry<String, String> header : nameSpaceHeadersMap.entrySet()) {
                try {
                    soapClient.getMessage().getSOAPPart().getEnvelope().addNamespaceDeclaration(header.getKey(), header.getValue());
                } catch (SOAPException e) {
                    return null;
                }
            }
            // 设置方法
            if(!mapMethod.isEmpty()){
                for(String key:mapMethod.keySet()){
                    soapClient.setMethod(key,mapMethod.get(key));
                }
            }
            SOAPBodyElement methodEle = soapClient.getMethodEle();
            if(!mapMethodEle.isEmpty()){
                for(String key:mapMethodEle.keySet()){
                    methodEle.setAttribute(key,mapMethodEle.get(key));
                }
            }

            SOAPElement soapElement = methodEle.addChildElement(childElement);
            if(!mapSoapElement.isEmpty()){
                for(String key:mapSoapElement.keySet()){
                    soapElement.setAttribute(key, mapSoapElement.get(key));
                }
            }
            soapElement.setValue(xmlContent);
        } catch (Exception e) {
            log.error("");
        }
        return soapClient;
    }


}
