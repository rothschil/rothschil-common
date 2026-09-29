package io.github.rothschil.common.utils;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.rothschil.common.base.dto.AmazTuple;
import io.github.rothschil.common.base.dto.RestBean;
import io.github.rothschil.common.base.vo.AbsBaseReq;
import io.github.rothschil.common.base.vo.AbsIvrVo;
import io.github.rothschil.common.base.vo.BaseResp;
import io.github.rothschil.common.base.vo.RequestHeaderVo;
import io.github.rothschil.common.constant.Constant;
import io.github.rothschil.common.exception.CommonException;
import io.github.rothschil.common.intf.IntfConf;
import io.github.rothschil.common.response.enums.Status;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 构建远程 RPC 访问的工具类，对 HTTP GET/POST 以及 SOAP 的封装
 * @author HeD
 * @author  <a href="mailto:WCNGS@QQ.COM">Sam</a>
 */
public class RestUtils extends BaseUtils{

    protected static final Logger log = LoggerFactory.getLogger(RestUtils.class);



    /** 根据请求实例获取响应
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfCode    接口信息
     * @param req   入参实例
     * @param baseRespClass 响应实例的类型
     * @param appendHttpHeaders 根据具体业务动态添加请求Header
     * @return T
     **/
    public static <T extends BaseResp> T post(String intfCode,AbsBaseReq req, Class<T> baseRespClass,HttpHeaders appendHttpHeaders) {
        IntfConf intfConf = getIntfConf(intfCode);
        RestBean restBean = RestUtils.post(intfConf, JSONUtil.toJsonStr(req), appendHttpHeaders);
        if (restBean.getCode() != HttpStatus.OK.value()) {
            throw new CommonException(Status.API_NOT_FOUND_EXCEPTION,restBean);
        }
        String respJson = restBean.getResp();
        T t = JSONUtil.toBean(respJson,baseRespClass);
        String remark = restBean.getRemark();
        t.setRemark(remark);
        return t;
    }


    /** 根据请求实例获取响应
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfConf    接口信息
     * @param req   入参实例
     * @param baseRespClass 响应实例的类型
     * @param appendHttpHeaders 根据具体业务动态添加请求Header
     * @return T
     **/
    public static <T extends BaseResp> T post(IntfConf intfConf, AbsBaseReq req, Class<T> baseRespClass, HttpHeaders appendHttpHeaders) {
        RestBean restBean = RestUtils.post(intfConf, JSONUtil.toJsonStr(req), appendHttpHeaders);
        if (restBean.getCode() != HttpStatus.OK.value()) {
            throw new CommonException(Status.API_NOT_FOUND_EXCEPTION,restBean);
        }
        String respJson = restBean.getResp();
        T t = JSONUtil.toBean(respJson,baseRespClass);
        String remark = restBean.getRemark();
        t.setRemark(remark);
        return t;
    }

    public static RestBean post(String intfCode, String json, HttpHeaders httpHeaders) {
        IntfConf intfConf = getIntfConf(intfCode);
        if (null == intfConf) {
            throw new CommonException(Status.TARGET_NOT_EXIST,"intfConf查询失败,接口未配置 intfCode:"+intfCode);
        }
        return post(intfConf, json, httpHeaders);
    }

    public static RestBean post(IntfConf intfConf, String json, HttpHeaders httpHeaders) {
        int timeout = getTimeOut(intfConf);
        //请求头
        String address = intfConf.getAddress();
        CloseableHttpClient httpClient = closeableHttpClient(timeout);
        //返回乱码处理
//        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        RestBean restBean = null;
        try {
            AmazTuple tuple=exchange(httpClient, httpHeaders, address, HttpMethod.POST, json, intfConf);
            restBean = getRestBean(tuple);
        } catch (Exception e) {
            restBean = new RestBean().fail();
        }
        return restBean;
    }



    /** 根据请求实例获取响应
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfCode    接口编码
     * @param params   入参实例
     * @param headerInfo 头信息
     * @return RestBean
     **/
    public static RestBean get(String intfCode, Map params, HttpHeaders headerInfo) {
        IntfConf intfConf = getIntfConf(intfCode);
        if (null == intfConf) {
            throw new CommonException(Status.TARGET_NOT_EXIST,"intfConf查询失败,接口未配置 intfCode:"+intfCode);
        }
        AtomicReference<String> stringAtomicReference = new AtomicReference<>("");
        params.forEach((k, v) -> stringAtomicReference.set(stringAtomicReference + "&" + k + "=" + v));
        String uri = stringAtomicReference.get();
        if (uri.startsWith("&")) {
            uri = uri.substring(1);
            uri = "?" + uri;
        }
        return get(intfConf, uri, headerInfo);
    }

    /** 根据请求实例获取响应
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfConf    接口信息
     * @param params   入参实例
     * @param headerInfo 头信息
     * @return RestBean
     **/
    public static RestBean get(IntfConf intfConf, Map params, HttpHeaders headerInfo) {
        AtomicReference<String> stringAtomicReference = new AtomicReference<>("");
        params.forEach((k, v) -> stringAtomicReference.set(stringAtomicReference + "&" + k + "=" + v));
        String uri = stringAtomicReference.get();
        if (uri.startsWith("&")) {
            uri = uri.substring(1);
            uri = "?" + uri;
        }
        return get(intfConf, uri, headerInfo);
    }

    /** 根据请求实例获取响应
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfConf    接口信息
     * @param params   入参实例
     * @param baseRespClass 响应实例的类型
     * @return T
     **/
    public static <T extends BaseResp> T get(IntfConf intfConf, Map<String,Object> params, Class<T> baseRespClass) {
        RestBean restBean = get(intfConf, params, new HttpHeaders());
        if (restBean.getCode() != HttpStatus.OK.value()) {
            throw new CommonException(Status.FAILURE,restBean);
        }
        String respJson = restBean.getResp();
        T t =JSONUtil.toBean(respJson,baseRespClass);
        String remark = restBean.getRemark();
        t.setRemark(remark);
        return t;
    }

    public static RestBean postSpecialHeader(String serviceName, Map map, HttpHeaders headerInfo) {
        IntfConf intfConf = getIntfConf(serviceName);
        if (null == intfConf) {
            throw new CommonException(Status.TARGET_NOT_EXIST,"intfConf查询失败,接口未配置 serviceName:"+serviceName);
        }
        AtomicReference<String> stringAtomicReference = new AtomicReference<>("");
        map.forEach((k, v) -> stringAtomicReference.set(stringAtomicReference + "&" + k + "=" + v));
        String uri = stringAtomicReference.get();
        if (uri.startsWith("&")) {
            uri = uri.substring(1);
            uri = "?" + uri;
        }
        return post(intfConf, uri, headerInfo);
    }

    public static RestBean get(IntfConf intfConf, String uri, HttpHeaders header) {
        String address = intfConf.getAddress() + uri;
        int timeout = getTimeOut(intfConf);
//        RestTemplate restTemplate = getRestTemplate(timeout);
        CloseableHttpClient httpClient = closeableHttpClient(timeout);

        AmazTuple tuple = exchange(httpClient, header, address, HttpMethod.GET, "", intfConf);
        return getRestBean(tuple);
    }


    /** 底层接口实现
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @param restTemplate  {@link RestTemplate}
     * @param httpHeaders   头信息
     * @param address   地址
     * @param method    方法类型
     * @param json  JSON方法
     * @param intfConf  接口实例
     * @return String>
     **/
    private static AmazTuple exchange(RestTemplate restTemplate, HttpHeaders httpHeaders, String address,
                                                   HttpMethod method, String json, IntfConf intfConf) {
        if (StringUtils.isBlank(address)) {
            log.error("[ Address ] {} Address is empty! ",address);
            throw new CommonException(Status.NULL_POINTER_EXCEPTION,"Address "+address+" The Address is empty Or not configured");
        }
        String headerInfo = intfConf.getHeaderInfo();
        if (StringUtils.isNotBlank(headerInfo)) {
            JSONObject jsonObject = JSONUtil.parseObj(headerInfo);
            if (!jsonObject.isEmpty()) {
                jsonObject.forEach((k, v) -> {
                    v = ObjectUtil.isEmpty(v) ? "" : v;
                    httpHeaders.set(k, (String) v);
                });
                String transId  ="UNKNOW";
                // 补充 X-CTG-Request-ID 作为同EOP交互凭据
                Object obj = UserTransmittableUtils.get();
                if(obj instanceof RequestHeaderVo){
                    RequestHeaderVo headerVo = (RequestHeaderVo)obj;
                    transId  = headerVo.getCallId();
                }else if(obj instanceof AbsIvrVo){
                    AbsIvrVo ivrVo = (AbsIvrVo)obj;
                    transId  = ivrVo.tranId;
                } else{
                    transId = DateUtils.transId(8);
                }
                httpHeaders.set(Constant.CTG_REQUEST_ID,transId);
            }
        }
        if (ObjectUtil.isNull(httpHeaders.getContentType())) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        return postResponseEntity(restTemplate,intfConf,address,json,httpHeaders,method);
    }

    /** 底层接口实现
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @param httpClient  {@link RestTemplate}
     * @param httpHeaders   头信息
     * @param address   地址
     * @param method    方法类型
     * @param json  JSON方法
     * @param intfConf  接口实例
     * @return String>
     **/
    private static AmazTuple exchange(CloseableHttpClient httpClient, HttpHeaders httpHeaders, String address,
                                      HttpMethod method, String json, IntfConf intfConf) {
        if (StringUtils.isBlank(address)) {
            log.error("[ Address ] {} Address is empty! ",address);
            throw new CommonException(Status.NULL_POINTER_EXCEPTION,"Address "+address+" The Address is empty Or not configured");
        }
        String headerInfo = intfConf.getHeaderInfo();
        if (StringUtils.isNotBlank(headerInfo)) {
            JSONObject jsonObject = JSONUtil.parseObj(headerInfo);
            if (!jsonObject.isEmpty()) {
                jsonObject.forEach((k, v) -> {
                    v = ObjectUtil.isEmpty(v) ? "" : v;
                    httpHeaders.set(k, (String) v);
                });
                String transId  ="UNKNOW";
                // 补充 X-CTG-Request-ID 作为同EOP交互凭据
                Object obj = UserTransmittableUtils.get();
                if(obj instanceof RequestHeaderVo){
                    RequestHeaderVo headerVo = (RequestHeaderVo)obj;
                    transId  = headerVo.getCallId();
                }else if(obj instanceof AbsIvrVo){
                    AbsIvrVo ivrVo = (AbsIvrVo)obj;
                    transId  = ivrVo.tranId;
                } else{
                    transId = DateUtils.transId(8);
                }
                httpHeaders.set(Constant.CTG_REQUEST_ID,transId);
            }
        }
        if (ObjectUtil.isNull(httpHeaders.getContentType())) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        return execute(httpClient,intfConf,address,json,httpHeaders,method);
    }


}

