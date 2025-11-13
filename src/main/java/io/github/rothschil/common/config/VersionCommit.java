package io.github.rothschil.common.config;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.*;
import java.util.Properties;

/**
 * 记录版本，每次发版打包的时候自动生成git相关信息
 * gradle 生成git信息保存在类路径的version-commit.txt中
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public class VersionCommit {
    private static final Logger logger = LoggerFactory.getLogger(VersionCommit.class);
    private static final String VERSION_COMMIT_FILE_NAME = "version-commit.properties";

    //已实现的这些字段，还可以动态添加其他字段
    public static final String BRANCH        = "branch";
    public static final String COMMIT_ID     = "commitId";
    public static final String COMMIT_AUTHOR = "commitAuthor";
    public static final String COMMIT_TIME   = "commitTime";
    public static final String PACKAGE_TIME  = "packageTime";

    public static Map<String, String> fromFile(){
        Properties v = getVersionCommitInfo();
        if(null != v){
            HashMap<String, String> map = new HashMap<>(v.size());
            for (String n : v.stringPropertyNames()) {
                map.put(n, v.getProperty(n));
            }
            return map;
        }
        return null;
    }
    public static void print(){
        try {
            Map<String, String> map = fromFile();
            if(null != map){
                logger.info("start up successfully with version-commit: \r\n {}", map);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static Properties getVersionCommitInfo(){
        String fileName = getCommitInfoFileName();
        try (InputStream inputStream = VersionCommit.class.getClassLoader().getResourceAsStream(fileName);
        ) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        }catch (Exception e){
            //有异常的时候返回null
            return null;
        }
    }

    /**
     * 给一个修改的机会
     */
    private static String getCommitInfoFileName() {
        String fileName = VERSION_COMMIT_FILE_NAME;
        String v = System.getProperty("version.commit.file.name");
        if(StrUtil.isNotEmpty(v)){
            fileName = v;
        }
        return fileName;
    }
}



