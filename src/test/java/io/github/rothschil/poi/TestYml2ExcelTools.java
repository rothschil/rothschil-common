package io.github.rothschil.poi;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class TestYml2ExcelTools {
    private final static List<String> KEYS = Arrays.asList("interfaceName","address","remark");
    private static LinkedHashMap properties;


    public static void getYamlMap(String yamlName) {
        InputStream in = null;
        try {
            Yaml yaml = new Yaml();
            in = TestYml2ExcelTools.class.getClassLoader().getResourceAsStream(yamlName);
            properties = yaml.loadAs(in, LinkedHashMap.class);
        } catch (Exception e) {
            log.error("[Exception] {}",e.getMessage());
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                log.error("[Exception] {}",e.getMessage());
            }
        }
    }

    public static void getValueByKey(String yamlName) {
        getYamlMap(yamlName);
        Iterator it = properties.entrySet().iterator();
        List<IntfBo> datas = new ArrayList<>(100);
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            IntfBo bo = new IntfBo();
//            bo.setInterfaceName(entry.getKey().toString());
            Map<String,String> val = (Map)entry.getValue();
            Set<String> vki =val.keySet();
            for (String vl:vki) {
                if(KEYS.get(0).equals(vl)){
                    bo.setInterfaceName(val.get(vl));
                }
                if(KEYS.get(1).equals(vl)){
                    bo.setAddress(val.get(vl));
                }
                if(KEYS.get(2).equals(vl)){
                    bo.setRemark(val.get(vl));
                }
            }
            datas.add(bo);
        }
        excelTools(datas);
    }

    public static void excelTools(List<IntfBo> datas) {
        // 创建需要写入的数据列表
//        List<IntfBo> datas = new ArrayList<>(2);
//        ExcelDataVO dataVO = new ExcelDataVO();
//        dataVO.setName("小明");
//        dataVO.setAge(18);
//        dataVO.setLocation("广州");
//        dataVO.setJob("大学生");
//        ExcelDataVO dataVO2 = new ExcelDataVO();
//        dataVO2.setName("小花");
//        dataVO2.setAge(19);
//        dataVO2.setLocation("深圳");
//        dataVO2.setJob("大学生");
//        dataVOList.add(dataVO);
//        dataVOList.add(dataVO2);

        // 写入数据到工作簿对象内
        Workbook workbook = ExcelWriter.exportData(datas);

        // 以文件的形式输出工作簿对象
        FileOutputStream fileOut = null;
        try {
            String exportFilePath = "I:\\log/writeExample.xlsx";
            File exportFile = new File(exportFilePath);
            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }

            fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {
            log.warn("输出Excel时发生错误，错误原因：" + e.getMessage());
        } finally {
            try {
                if (null != fileOut) {
                    fileOut.close();
                }
                workbook.close();
            } catch (IOException e) {
                log.warn("关闭输出流时发生错误，错误原因：" + e.getMessage());
            }
        }
    }


    public static void main(String[] args) {
        TestYml2ExcelTools.getValueByKey("gansu.yml");

    }
}