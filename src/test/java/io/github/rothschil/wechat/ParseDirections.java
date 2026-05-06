package io.github.rothschil.wechat;


import cn.hutool.core.io.FileUtil;
import io.github.rothschil.AbstractBaseSimpleCase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/
public class ParseDirections extends AbstractBaseSimpleCase {

    static Set<String> SET_SUFFIX_NAME =new HashSet<String>() {{
        add("XLSX");
        add("XLS");
        add("PPT");
        add("PPTX");
        add("DOC");
        add("DOCX");
    }};

    public static void main(String[] args) {
        File originFile = new File("G:\\Repertory\\Tencent\\WeChat\\xwechat_files\\wxid_wx134g6phllm12_831d\\msg\\file");
        String targetFile = "I:\\fileList.xlsx";

        ParseDirections  parseDirections = new ParseDirections();
        parseDirections.parse(originFile,targetFile);

    }


    protected void parse(File originFile,String targetFile){
        List<PrizeDo> list = new ArrayList<>();
        if(originFile.isDirectory()){
            File[] files = originFile.listFiles();
            assert files != null;
            for (File ifs : files) {
                if (ifs.isDirectory()) {
                    parse(ifs, targetFile);
                } else {
                    String suffixName = FileUtil.getSuffix(ifs);
                    String upperName = suffixName.toUpperCase();
                    if(SET_SUFFIX_NAME.contains(upperName)){
                        PrizeDo prizeDo = new PrizeDo();
                        String fileName = ifs.getName();
                        String filePath = ifs.getAbsolutePath();
                        prizeDo.setName(fileName);
                        prizeDo.setPath(filePath);
                        list.add(prizeDo);
                    }
                }
            }
        }
        if(!list.isEmpty()){
            printFileList(targetFile,list);
        }
    }



    protected void printFileList(String targetFile,List<PrizeDo> list){
        ExcelUtils.writeExcel(targetFile, list,PrizeDo.class);

    }
}
