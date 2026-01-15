package io.github.rothschil.common.constant;

import org.springframework.stereotype.Controller;

import java.util.HashMap;

/**
 * 通用常量信息
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public interface Constant {


    String TRACE_ID = "traceId";

    String CTG_REQUEST_ID ="X-CTG-Request-ID";

    String ORIG_CLIENT_IP="";

    String USER_AGENT="User-Agent";

    String HOST="Host";

    String DB_TYPE ="db.dtype";

    /**
     *  标识常量，用于在 {@link Controller} 层面中统一处理 返回值
     */
    String RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN";

    String HTTP_ERR_MSG_DEFAULT = "请求或者响应异常";

    String IP_REGEX = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
    String TURS_URL_ERR = "地址转换URL错误";


    /**
     * 数字字符串1
     */
    String NUM_1 = "1";

    /**
     * 数字字符串2
     */
    String NUM_2 = "2";

    String ON_WAY = "301100";

    /**
     * 数字字符串3
     */
    String NUM_3 = "3";

    /**
     * 数字字符串4
     */
    String NUM_4 = "4";

    /**
     * 数字字符串4
     */
    String NUM_5 = "5";

    /**
     * 数字字符串4
     */
    String NUM_6 = "6";


    /**
     * 空格 space
     */
    String SPACE = " ";

    /**
     * 数字字符串4
     */
    String NUM_7 = "7";

    /**
     * 数字字符串4
     */
    String NUM_8 = "8";

    /**
     * 数字字符串9
     */
    String NUM_9 = "9";


    /**
     * IP网卡信息，主环境
     */
    String IPADDRESS_MASTR = "80";

    /**
     * IP网卡信息，备用环境
     */
    String IPADDRESS_SLAVE = "70";


    String CURRENT = "current";
    String PAGE_SIZE = "pageSize";
    String VALID = "valid";
    String SORTER = "sorter";
    String EMPTY_SORTER = "{}";
    String ASCEND = "ascend";
    String QUESTION = "?";
    String POINT = ".";
    String UNKNOWN = "unknown";
    String COMMA = ",";
    String LIST_TYPE = "list";
    Integer VALID_FALSE = 0;
    Integer VALID_TRUE = 1;

    String URL_PREFIX = "common/";

    /**
     * 签名
     */
    String SIGNATURE = "";

    /**
     * mysql
     */
    String MYSQL = "mysql";

    /**
     * postgresql
     */
    String POSTGRESQL = "postgresql";

    /**
     * 生成java类
     */
    String GENERATOR_STRING = "#\\u4EE3\\u7801\\u751F\\u6210\\u5668\\uFF0C\\u914D\\u7F6E\\u4FE1\\u606F\n" +
            "mainPath=\n" +
            "#\\u5305\\u540D\n" +
            "package=com.supcon.mare\n" +
            "moduleName=tankinfo\n" +
            "#\\u4F5C\\u8005\n" +
            "author=zhaoxu\n" +
            "#\\u8868\\u524D\\u7F00(\\u7C7B\\u540D\\u4E0D\\u4F1A\\u5305\\u542B\\u8868\\u524D\\u7F00)\n" +
            "tablePrefix=tb_\n" +
            "#\\u7C7B\\u578B\\u8F6C\\u6362\\uFF0C\\u914D\\u7F6E\\u4FE1\\u606F\n" +
            "tinyint=Integer\n" +
            "smallint=Integer\n" +
            "mediumint=Integer\n" +
            "int=Integer\n" +
            "integer=Integer\n" +
            "bigint=Long\n" +
            "float=Float\n" +
            "double=Double\n" +
            "decimal=BigDecimal\n" +
            "bit=Boolean\n" +
            "char=String\n" +
            "varchar=String\n" +
            "varchar(255)=String\n" +
            "tinytext=String\n" +
            "text=String\n" +
            "mediumtext=String\n" +
            "longtext=String\n" +
            "date=LocalDateTime\n" +
            "datetime=LocalDateTime\n" +
            "character\\u0020varying=String\n" +
            "timestamp=LocalDateTime\n";

    /**
     * 生成jdbc配置
     */
    String JDBC_TYPE_STRING = "tinyint=TINYINT\n" +
            "smallint=SMALLINT\n" +
            "mediumint=MEDIUMINT\n" +
            "int=INTEGER\n" +
            "integer=INTEGER\n" +
            "bigint=BIGINT\n" +
            "float=FLOAT\n" +
            "double=DOUBLE\n" +
            "decimal=DECIMAL\n" +
            "bit=BIT\n" +
            "char=CHAR\n" +
            "varchar=VARCHAR\n" +
            "tinytext=VARCHAR\n" +
            "text=VARCHAR\n" +
            "mediumtext=VARCHAR\n" +
            "longtext=VARCHAR\n" +
            "date=DATE\n" +
            "datetime=DATETIME\n" +
            "timestamp=TIMESTAMP\n" +
            "character\\u0020varying=VARCHAR\n" +
            "blob=BLOB\n" +
            "longblob=LONGBLOB\n";

    HashMap<String, String> GENERATE_FFILE_MAP = new HashMap<String, String>() {
        {
            put("api.ts.vm", "import request from '@/router/axios'\n" +
                    "\n" +
                    "/**\n" +
                    " * 新增$!{comments}\n" +
                    " * @param obj $!{comments}\n" +
                    " */\n" +
                    "export function add$!{className}($!{lowerAttrName}) {\n" +
                    "    return request({\n" +
                    "        url: '/$!{mainPath}/$!{lowerAttrName}',\n" +
                    "        method: 'post',\n" +
                    "        data: $!{lowerAttrName}\n" +
                    "    })\n" +
                    "}\n" +
                    "\n" +
                    "/**\n" +
                    " * 通过id删除$!{comments}\n" +
                    " * @param ids 主键,多个逗号隔开\n" +
                    " */\n" +
                    "export function del$!{className}(ids) {\n" +
                    "    return request({\n" +
                    "        url: '/$!{mainPath}/$!{lowerAttrName}/' + ids,\n" +
                    "        method: 'delete'\n" +
                    "    })\n" +
                    "}\n" +
                    "\n" +
                    "/**\n" +
                    " * 修改$!{comments}\n" +
                    " * @param obj $!{comments}\n" +
                    " */\n" +
                    "export function put$!{className}($!{lowerAttrName}) {\n" +
                    "    return request({\n" +
                    "        url: '/$!{mainPath}/$!{lowerAttrName}',\n" +
                    "        method: 'put',\n" +
                    "        data: $!{lowerAttrName}\n" +
                    "    })\n" +
                    "}\n" +
                    "\n" +
                    "/**\n" +
                    " * 分页查询$!{comments}\n" +
                    " * @param query 分页查询条件\n" +
                    " */\n" +
                    "export function findByPage(params) {\n" +
                    "    return request({\n" +
                    "        url: '/$!{mainPath}/$!{lowerAttrName}/findByPage',\n" +
                    "        method: 'get',\n" +
                    "        params: params\n" +
                    "    })\n" +
                    "}\n" +
                    "\n" +
                    "/**\n" +
                    " * 全属性条件查询$!{comments}\n" +
                    " * @param id 主键\n" +
                    " */\n" +
                    "export function findAllByConditions(params) {\n" +
                    "    return request({\n" +
                    "        url: '/$!{mainPath}/$!{lowerAttrName}/findAll',\n" +
                    "        method: 'get',\n" +
                    "        params: params\n" +
                    "    })\n" +
                    "}\n" +
                    "\n" +
                    "/**\n" +
                    " * 通过条件查询一个对象$!{comments}\n" +
                    " */\n" +
                    "export function findByAttr(attr, condition) {\n" +
                    "    return request({\n" +
                    "        url: '/$!{mainPath}/$!{lowerAttrName}/' + attr + '/' + condition,\n" +
                    "        method: 'get'\n" +
                    "    })\n" +
                    "}\n" +
                    "\n" +
                    "/**\n" +
                    " * 通过条件查询多个对象$!{comments}\n" +
                    " */\n" +
                    "export function findByAttrs(attr, condition, conditionType) {\n" +
                    "    return request({\n" +
                    "        url: '/$!{mainPath}/$!{lowerAttrName}/list/' + attr + '/' + condition + '?conditionType=' + conditionType,\n" +
                    "        method: 'get'\n" +
                    "    })\n" +
                    "}\n" +
                    "\n" );
            put("Controller.java.vm", "package $!{package}.$!{moduleName}.controller;\n" +
                    "\n" +
                    "import com.zx.utils.controller.BaseControllerModel;\n" +
                    "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                    "import org.springframework.web.bind.annotation.RestController;\n" +
                    "import io.swagger.annotations.Api;\n" +
                    "import lombok.extern.slf4j.Slf4j;\n" +
                    "import $!{package}.$!{moduleName}.entity.$!{className}Entity;\n" +
                    "\n" +
                    "/**\n" +
                    " * <p>\n" +
                    " * $!{comments}\n" +
                    " * </p>\n" +
                    " *\n" +
                    " * @author $!{author}\n" +
                    " * @date Created in $!{datetime}\n" +
                    " */\n" +
                    "@Slf4j\n" +
                    "@RestController\n" +
                    "@RequestMapping(\"/$!{mainPath}\")\n" +
                    "@Api(tags = {\"$!{className}Controller\"}, description = \"$!{comments}\")\n" +
                    "public class $!{className}Controller extends BaseControllerModel<$!{className}Entity, $!{className}Entity> {\n" +
                    "\n" +
                    "}" );
            put("Entity.java.vm", "package $!{package}.$!{moduleName}.entity;\n" +
                    "\n" +
                    "#if($!{hasBigDecimal})\n" +
                    "import java.math.BigDecimal;\n" +
                    "#end\n" +
                    "import java.time.LocalDateTime;\n" +
                    "import java.io.Serializable;\n" +
                    "import java.util.Date;\n" +
                    "import javax.persistence.*;\n" +
                    "import io.swagger.annotations.ApiModelProperty;\n" +
                    "import org.springframework.data.annotation.CreatedDate;\n" +
                    "import org.springframework.data.annotation.LastModifiedDate;\n" +
                    "import org.springframework.data.jpa.domain.support.AuditingEntityListener;\n" +
                    "import com.fasterxml.jackson.annotation.JsonIgnoreProperties;\n" +
                    "import lombok.Data;\n" +
                    "import lombok.NoArgsConstructor;\n" +
                    "/**\n" +
                    " * <p>\n" +
                    " * $!{comments}\n" +
                    " * </p>\n" +
                    " *\n" +
                    " * @author $!{author}\n" +
                    " * @date Created in $!{datetime}\n" +
                    " */\n" +
                    "@Entity\n" +
                    "@Table(name = \"$!{tableName}\")\n" +
                    "@JsonIgnoreProperties(ignoreUnknown = true)\n" +
                    "@Data\n" +
                    "@NoArgsConstructor\n" +
                    "@EntityListeners(AuditingEntityListener.class)\n" +
                    "public class $!{className}Entity implements Serializable {\n" +
                    "    private static final long serialVersionUID = 1L;\n" +
                    "\n" +
                    "  #foreach ($!column in $!columns)\n" +
                    "    /**\n" +
                    "     * $!column.comments\n" +
                    "     */\n" +
                    "    @Schema(description = \"$!column.comments\")\n" +
                    "    #if($!column.columnName == $!pk.columnName)\n" +
                    "    @Id\n" +
                    "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                    "    private $!column.attrType $!column.lowerAttrName;\n" +
                    "    #else\n" +
                    "    @Column(name = \"$!{column.columnName}\", nullable = $!{column.nullAbled})\n" +
                    "    private $!column.attrType $!column.lowerAttrName;\n" +
                    "    #end\n" +
                    "\n" +
                    "  #end\n" +
                    "\n" +
                    "    @Schema(description = \"创建时间\")\n" +
                    "    @CreatedDate\n" +
                    "    @Column(name = \"gmt_create\", updatable = false)\n" +
                    "    @Temporal(TemporalType.TIMESTAMP)\n" +
                    "    private Date gmtCreate;\n" +
                    "    @Schema(description = \"修改时间\")\n" +
                    "    @LastModifiedDate\n" +
                    "    @Column(name = \"gmt_modified\", insertable = false)\n" +
                    "    @Temporal(TemporalType.TIMESTAMP)\n" +
                    "    private Date gmtModified;\n" +
                    "\n" +
                    "    @Schema(description = \"状态值\")\n" +
                    "    @Column(name = \"valid\", columnDefinition = \"integer\")\n" +
                    "    private Integer valid;\n" +
                    "}\n" );
            put("Repository.java.vm", "package $!{package}.$!{moduleName}.repository;\n" +
                    "\n" +
                    "import $!{package}.$!{moduleName}.entity.$!{className}Entity;\n" +
                    "import com.zx.utils.repository.BaseRepository;\n" +
                    "import org.springframework.stereotype.Repository;\n" +
                    "\n" +
                    "/**\n" +
                    " * <p>\n" +
                    " * $!{comments}\n" +
                    " * </p>\n" +
                    " *\n" +
                    " * @author $!{author}\n" +
                    " * @date Created in $!{datetime}\n" +
                    " */\n" +
                    "@Repository\n" +
                    "public interface $!{className}Repository extends BaseRepository<$!{className}Entity, Long> {\n" +
                    "\n" +
                    "}\n" );
        }
    };

    String REQ_OBJ = "reqObj";
}
