package io.github.rothschil.common.utils.sqlite;import lombok.extern.slf4j.Slf4j;import org.springframework.util.ResourceUtils;import java.io.*;import java.sql.Connection;import java.sql.SQLException;@Slf4jpublic class SqliteUtils {    /**     * 初始化项目db     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>     * @param connection    连接     * @param tableName 数据表名称     **/    public static void initProDb(Connection connection,String tableName){        //判断数据表是否存在        boolean hasPro = false;        try {            hasPro = true;            //测试数据表是否存在            connection.prepareStatement("select * from "+tableName).execute();        }catch (SQLException e){            //不存在            log.debug("table pro is not exist");            hasPro = false;        }        //不存在时创建db        if(!hasPro) {            log.debug("Start init {} db",tableName);            File file = null;            try {                //读取初始化数据sql                file = ResourceUtils.getFile("classpath:sql/init.sql");            } catch (FileNotFoundException e) {                e.printStackTrace();            }            //获取sql            String sql = "";            FileInputStream fis = null;            InputStreamReader isr = null;            try {                fis = new FileInputStream(file);                isr = new InputStreamReader(fis, "UTF-8");                BufferedReader bf = new BufferedReader(isr);                String content = "";                StringBuilder sb = new StringBuilder();                while (true) {                    content = bf.readLine();                    if (content == null) {                        break;                    }                    sb.append(content.trim());                }                sql = sb.toString();            } catch (IOException e) {                e.printStackTrace();            } finally {                try {                    assert isr != null;                    isr.close();                    assert fis != null;                    fis.close();                } catch (IOException e) {                    e.printStackTrace();                }            }            //分割sql            String[] sqls = sql.split(";");            try {                for (String str : sqls) {                    //开始初始化数据库                    connection.setAutoCommit(false);                    connection.prepareStatement(str).execute();                }                //提交sql                connection.commit();            } catch (SQLException e) {                e.printStackTrace();            } finally {                try {                    connection.close();                } catch (SQLException e) {                    e.printStackTrace();                }            }            log.debug("finish init pro db>>>");        }else {            log.debug("pro db is exist");        }    }    public static void initDb(Connection connection,String... sqls){        log.debug("Start initDb");        try {            for(String str:sqls) {                connection.setAutoCommit(false);                connection.prepareStatement(str).execute();            }            connection.commit();        } catch (SQLException e) {            e.printStackTrace();        }finally {            try {                connection.close();            } catch (SQLException e) {                e.printStackTrace();            }        }        log.debug("finish initDb>>>");    }    public static void initSqliteFile(String filePath){        File file = new File(filePath);        File dir = file.getParentFile();        if(!dir.exists()){            dir.mkdirs();        }        if(!file.exists()){            try {                file.createNewFile();            } catch (IOException e) {                e.printStackTrace();            }        }    }    public static String getFilePath(String url){        url = url.replace("jdbc:sqlite:", "");        return url;    }}