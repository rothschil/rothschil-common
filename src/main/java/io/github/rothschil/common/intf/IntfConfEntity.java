package io.github.rothschil.common.intf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 接口地址配置(IntfConf)实体类
 *
 * @author HeD
 * @since 2022-09-03 16:23:56
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
// @TableName("intf_conf")
public class IntfConfEntity implements Serializable {

    private static final long serialVersionUID = 665666641904003064L;

    // @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 接口名
     */
    // @TableField("intf_name")
    private String interfaceName;
    /**
     * 地址
     */
    // @TableField("address")
    private String address;
    /**
     * 方法名
     */
    // @TableField("method_name")
    private String methodName;

    // @TableField("param_name")
    private String paramName;

    // @TableField("header_info")
    private String headerInfo;
    /**
     * 状态：1在用，0停用
     */
    // @TableField("state")
    private String state;
    /**
     * 超时
     */
    // @TableField("timeout")
    private int timeout;

    /**
     * 命名空间
     */
    // @TableField("name_space")
    private String namespace;
    /**
     * 备注
     */
    private String remark;

    /**
     * 私钥
     */
    // @TableField("private_key")
    private String privateKey;

    // @TableField("public_key")
    private String publicKey;

    /**
     * <b>参数类型</b>
     */
    // @TableField(exist=false)
    private ParamType paramType;
    /**
     * <b>接口类型</b>
     * <ul>
     *     <li>0：HTTP+JSON</li>
     *     <li>1：WebService</li>
     * </ul>
     */
    // @TableField(exist=false)
    private Integer serviceType;

    /**
     * <b>WebService配置<b/>
     */
    // @TableField(exist=false)
    private WebServiceConf webServiceConf;

    @Data
    public static class Param {

        /**
         * <b>参数名<b/>
         */
        private String name;

        /**
         * <b>参数属性<b/>
         */
        private Map<String, String> attributes;

        /**
         * <b>参数类型<b/>
         * <ul>
         *     <li>NORMAL(基础类型)</li>
         *     <li>XML</li>
         * </ul>
         */
        private ParamType type;

        /**
         * <b>请求报文根节点<b/>
         */
        private String reqRootName;

    }

    public enum ParamType {

        NORMAL,
        XML,
        JSON;

    }

    @Data
    public static class RespXmlJSONDeserializer {

        private String expression;

    }


    @Data
    public static class WebServiceConf {

        /**
         * <b>命名空间列表<b/>
         */
        private Map<String, String> namespaceDeclarations;

        /**
         * <b>方法属性<b/>
         */
        private Map<String, String> methodAttributes;

        /**
         * <b>参数属性<b/>
         */
        private Map<String, String> paramAttributes;

        /**
         * <b>多个参数</b>
         */
        private List<Param> params;

        /**
         * <b>响应XML反序列化</b>
         */
        private RespXmlJSONDeserializer respXmlJSONDeserializer;

        /**
         * <b>请求报文根节点<b/>
         */
        private String reqRootName;

        /**
         * <b>响应解析表达式<b/>
         */
        private String respExpression;

        /**
         * <b>响应报文根节点<b/>
         */
        private String respRootName;

        /**
         * <b>响应内容根节点<b/>
         */
        private String respTagName;

        /**
         * <b>响应内容参数节点<b/>
         */
        private String respParamTagName;


    }

}

