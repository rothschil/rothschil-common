package io.github.rothschil.common.utils;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import io.github.rothschil.common.base.dto.AmazTuple;
import io.github.rothschil.common.base.dto.RestBean;
import io.github.rothschil.common.base.vo.AbsIvrVo;
import io.github.rothschil.common.base.vo.RequestHeaderVo;
import io.github.rothschil.common.constant.Constant;
import io.github.rothschil.common.handler.IntfLog;
import io.github.rothschil.common.intf.IntfConf;
import io.github.rothschil.common.intf.IntfConfService;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;


/** 修订枚举
* @description: TODO
* @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
* @date 2025/5/15 22:28
* @version 1.0
*/
public abstract class BaseUtils {

    protected static final Logger log = LoggerFactory.getLogger(BaseUtils.class);

//    protected static CloseableHttpClient closeableHttpClient;
//
//    static {
//        closeableHttpClient = SpringUtil.getBean("closeableHttpClient");
//    }


//    /**
//     * 配置工厂
//     * @param timeout 超时时间
//     * @return  RestTemplate
//     */
//    protected static RestTemplate getRestTemplate(Integer timeout) {
//        RestTemplate restTemplate = new RestTemplate();
//        CloseableHttpClient httpClient = closeableHttpClient(timeout);
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//        requestFactory.setConnectTimeout(timeout);
//        restTemplate.setRequestFactory(requestFactory);
//        return restTemplate;
//    }


    protected static CloseableHttpClient closeableHttpClient(Integer timeout) {
        return HttpClients.createDefault();
    }

    /** 构建超时时间，如果没有配置，则启用 5 秒
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfConf  http配置实例
     * @return int  具体超时时间
     **/
    protected static int getTimeOut(IntfConf intfConf){
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
    protected static IntfConf getIntfConf(String intfCode) {
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
    protected static void printConsoleLog(IntfConf intfConf, String address, String json, String body, long procTime){
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
     * @param httpClient  RestTemplate实例
     * @param intfConf  接口实例
     * @param url   目标地址
     * @param json  请求内容，JSON格式
     * @param httpHeaders   Http头信息
     * @param method    HttpMethod请求方式
     * @return cn.ffcs.up.common.base.dto.AmazTuple
     **/
    protected static AmazTuple execute(CloseableHttpClient httpClient, IntfConf intfConf, String url, String json, HttpHeaders httpHeaders, HttpMethod method) {
        String errMsg = Constant.HTTP_ERR_MSG_DEFAULT;
        long beginTime = System.currentTimeMillis();
        long costTime;
        String body=null;
        String remark;
        ClassicHttpRequest request = new HttpGet(url);

        try {
            if (httpHeaders.containsKey("POST")) {
                request = new HttpPost(url);
            }

            CloseableHttpResponse response = httpClient.execute(request);
            if(ObjectUtil.isEmpty(response)){
                org.apache.hc.core5.http.HttpEntity entity  = response.getEntity();
                if(ObjectUtil.isEmpty(entity)){
                    body = EntityUtils.toString(response.getEntity());
                }
            }
            costTime = System.currentTimeMillis() - beginTime;
            return new AmazTuple(body, "", null);
        } catch (ResourceAccessException exception) {
            errMsg = "[" + url + "] Address Cannot Be Accessed! Error Messsage " + exception.getMessage();
        } catch (HttpStatusCodeException exception) {
            HttpStatusCode statusCode = exception.getStatusCode();
            errMsg = "[" + url + "] ,HttpStatus Value " + statusCode.value() + " ;Error Messsage " + exception.getMessage();
        } catch (Exception e) {
            errMsg = "Exception " + TextUtil.exToStr(e);
        }
        body = null != body ? body : errMsg;
        costTime = System.currentTimeMillis() - beginTime;
        printConsoleLog(intfConf, url, json, body, costTime);
        remark = "[" + url + "] Request Time is " + costTime + " ms; However, The Timeout Is Set To " + intfConf.getTimeout() + " ms.";
        return new AmazTuple(body, remark, null);
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
    @Deprecated
    protected static AmazTuple postResponseEntity(RestTemplate restTemplate, IntfConf intfConf, String address, String json, HttpHeaders httpHeaders, HttpMethod method){

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

}
