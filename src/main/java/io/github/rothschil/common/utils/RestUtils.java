package io.github.rothschil.common.utils;


import cn.hutool.core.util.JAXBUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.webservice.SoapClient;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import io.github.rothschil.common.base.dto.AmazTuple;
import io.github.rothschil.common.base.dto.RestBean;
import io.github.rothschil.common.base.vo.AbsBaseReq;
import io.github.rothschil.common.base.vo.AbsIvrVo;
import io.github.rothschil.common.base.vo.BaseResp;
import io.github.rothschil.common.base.vo.RequestHeaderVo;
import io.github.rothschil.common.constant.Constant;
import io.github.rothschil.common.exception.CommonException;
import io.github.rothschil.common.intf.IntfConfEntity;
import io.github.rothschil.common.response.enums.Status;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
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
        IntfConfEntity intfConf = getIntfConf(intfCode);
        RestBean restBean = RestUtils.post(intfConf, JSON.toJSONString(req), appendHttpHeaders);
        if (restBean.getCode() != HttpStatus.OK.value()) {
            throw new CommonException(Status.API_NOT_FOUND_EXCEPTION,restBean);
        }
        String respJson = restBean.getResp();
        T t = com.alibaba.fastjson.JSONObject.parseObject(respJson, baseRespClass);
        String remark = restBean.getRemark();
        t.setRemark(remark);
        return t;
    }


    /** 根据请求实例获取响应
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param intfConfEntity    接口信息
     * @param req   入参实例
     * @param baseRespClass 响应实例的类型
     * @param appendHttpHeaders 根据具体业务动态添加请求Header
     * @return T
     **/
    public static <T extends BaseResp> T post(IntfConfEntity intfConfEntity,AbsBaseReq req, Class<T> baseRespClass,HttpHeaders appendHttpHeaders) {
        RestBean restBean = RestUtils.post(intfConfEntity, JSON.toJSONString(req), appendHttpHeaders);
        if (restBean.getCode() != HttpStatus.OK.value()) {
            throw new CommonException(Status.API_NOT_FOUND_EXCEPTION,restBean);
        }
        String respJson = restBean.getResp();
        T t = com.alibaba.fastjson.JSONObject.parseObject(respJson, baseRespClass);
        String remark = restBean.getRemark();
        t.setRemark(remark);
        return t;
    }

    public static RestBean post(String intfCode, String json, HttpHeaders httpHeaders) {
        IntfConfEntity intfConf = getIntfConf(intfCode);
        if (null == intfConf) {
            throw new CommonException(Status.TARGET_NOT_EXIST,"intfConf查询失败,接口未配置 intfCode:"+intfCode);
        }
        return post(intfConf, json, httpHeaders);
    }

    public static RestBean post(IntfConfEntity intfConf, String json, HttpHeaders httpHeaders) {
        int timeout = getTimeOut(intfConf);
        //请求头
        String address = intfConf.getAddress();
        RestTemplate restTemplate = getRestTemplate(timeout);
        //返回乱码处理
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        RestBean restBean = null;
        try {
            AmazTuple tuple=exchange(restTemplate, httpHeaders, address, HttpMethod.POST, json, intfConf);
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
        IntfConfEntity intfConf = getIntfConf(intfCode);
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
    public static RestBean get(IntfConfEntity intfConf, Map params, HttpHeaders headerInfo) {
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
     * @param intfConfEntity    接口信息
     * @param params   入参实例
     * @param baseRespClass 响应实例的类型
     * @return T
     **/
    public static <T extends BaseResp> T get(IntfConfEntity intfConfEntity,Map<String,Object> params, Class<T> baseRespClass) {
        RestBean restBean = get(intfConfEntity, params, new HttpHeaders());
        if (restBean.getCode() != HttpStatus.OK.value()) {
            throw new CommonException(Status.FAILURE,restBean);
        }
        String respJson = restBean.getResp();
        return com.alibaba.fastjson.JSONObject.parseObject(respJson, baseRespClass);
    }

    public static RestBean postSpecialHeader(String serviceName, Map map, HttpHeaders headerInfo) {
        IntfConfEntity intfConf = getIntfConf(serviceName);
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

    public static RestBean get(IntfConfEntity intfConf, String uri, HttpHeaders header) {
        String address = intfConf.getAddress() + uri;
        int timeout = getTimeOut(intfConf);
        RestTemplate restTemplate = getRestTemplate(timeout);
        AmazTuple tuple = exchange(restTemplate, header, address, HttpMethod.GET, "", intfConf);
        return getRestBean(tuple);
    }



    /**
     * 对外封装 WebService 的调用
     *
     * 1、nameSpaceHeadersMap 设置内容如下
     * <pre>
     * Map<String, String> nameSpaceHeadersMap = new LinkedHashMap();
     * nameSpaceHeadersMap.put("xsi", "<a href="http://www.w3.org/2001/XMLSchema-instance">...</a>");
     * nameSpaceHeadersMap.put("xsd", "<a href="http://www.w3.org/2001/XMLSchema">...</a>");
     * nameSpaceHeadersMap.put("soapenv", "<a href="http://schemas.xmlsoap.org/soap/envelope/">...</a>");
     *
     *
     * 2、soapClient.getMessage().getSOAPPart().getEnvelope().addNamespaceDeclaration(header.getKey(), header.getValue());
     * </pre>
     *
     * 3、mapMethod 设置内容如下
     * <pre>
     *  soapClient.setMethod("impl:excuteResult","<a href="http://impl.service.label.webservice.timesontransfar.com">...</a>");
     *
     *  soapClient.setMethod(key,mapMethod.get(key));
     * </pre>
     *
     * 4、mapMethodEle 设置内容如下
     * <pre>
     * methodEle.setAttribute("soapenv:encodingStyle", "<a href="http://schemas.xmlsoap.org/soap/encoding/">...</a>");
     * </pre>
     *
     * 5、mapSoapElement 设置内容如下
     * <pre>
     * soapElement.setAttribute("xsi:type", "soapenc:string");
     * soapElement.setAttribute("xmlns:soapenc", "<a href="http://schemas.xmlsoap.org/soap/encoding/">...</a>");
     * </pre>
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param req   请求实体
     * @param intfConfEntity    接口配置，用于获取Http的 地址 和 请求 Header信息
     * @param nameSpaceHeadersMap  命名空间
     * @param mapMethod 执行方法名
     * @param mapMethodEle  方法元素
     * @param childElement  子节点
     * @param mapSoapElement    SOAP元素节点
     * @return RestBean
     **/
    public static SoapClient post2Webservice(Object req,IntfConfEntity intfConfEntity,
                                             Map<String, String> nameSpaceHeadersMap,
                                             Map<String, String> mapMethod,
                                             Map<String, String> mapMethodEle,
                                             String childElement,
                                             Map<String, String> mapSoapElement) {

        String xmlContent = JAXBUtil.beanToXml(req);
        return post2Webservice(xmlContent,intfConfEntity,nameSpaceHeadersMap,mapMethod,mapMethodEle,childElement,mapSoapElement);
    }

    public static SoapClient post2Webservice(Object req,IntfConfEntity intfConfEntity,
                                             Charset charset,
                                             Map<String, String> nameSpaceHeadersMap,
                                             Map<String, String> mapMethod,
                                             Map<String, String> mapMethodEle,
                                             String childElement,
                                             Map<String, String> mapSoapElement) {
        String xmlContent = null;
        if (charset == null){
            xmlContent = JAXBUtil.beanToXml(req);
        }else{
            xmlContent = JAXBUtil.beanToXml(req, charset,true);
        }
        return post2Webservice(xmlContent,intfConfEntity,nameSpaceHeadersMap,mapMethod,mapMethodEle,childElement,mapSoapElement);
    }

    public static SoapClient post2Webservice(Object req,
                                             String intfCode,
                                             Charset charset,
                                             Map<String, String> nameSpaceHeadersMap,
                                             Map<String, String> mapMethod,
                                             Map<String, String> mapMethodEle,
                                             String childElement,
                                             Map<String, String> mapSoapElement) {
        IntfConfEntity intfConf = getIntfConf(intfCode);
        String xmlContent = null;
        if (charset == null){
            xmlContent = JAXBUtil.beanToXml(req);
        }else{
            xmlContent = JAXBUtil.beanToXml(req, charset,true);
        }
        return post2Webservice(xmlContent,intfConf,nameSpaceHeadersMap,mapMethod,mapMethodEle,childElement,mapSoapElement);
    }

    /**
     * 对外封装 WebService 的调用
     *
     * 1、nameSpaceHeadersMap 设置内容如下
     * <pre>
     * Map<String, String> nameSpaceHeadersMap = new LinkedHashMap();
     * nameSpaceHeadersMap.put("xsi", "<a href="http://www.w3.org/2001/XMLSchema-instance">...</a>");
     * nameSpaceHeadersMap.put("xsd", "http://www.w3.org/2001/XMLSchema");
     * nameSpaceHeadersMap.put("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
     *
     *
     * 2、soapClient.getMessage().getSOAPPart().getEnvelope().addNamespaceDeclaration(header.getKey(), header.getValue());
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
     * @param req   请求实体
     * @param intfConfEntity    接口配置，用于获取Http的 地址 和 请求 Header信息
     * @param nameSpaceHeadersMap  命名空间
     * @param mapMethod 执行方法名
     * @param mapMethodEle  方法元素
     * @param childElement  子节点
     * @param mapSoapElement    SOAP元素节点
     * @return RestBean
     **/
    public static String post2WebserviceContent(Object req,IntfConfEntity intfConfEntity,
                                                Map<String, String> nameSpaceHeadersMap,
                                                Map<String, String> mapMethod,
                                                Map<String, String> mapMethodEle,
                                                String childElement,
                                                Map<String, String> mapSoapElement) {


        String xmlContent = JAXBUtil.beanToXml(req);
        SoapClient soapClient = post2Webservice(xmlContent,intfConfEntity,nameSpaceHeadersMap,mapMethod,mapMethodEle,childElement,mapSoapElement);
        String soapResp = soapClient.send(false);
        return soapResp;
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
                                                   HttpMethod method, String json, IntfConfEntity intfConf) {
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


}

