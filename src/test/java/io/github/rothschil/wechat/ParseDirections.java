package io.github.rothschil.wechat;


import io.github.rothschil.AbstractBaseSimpleCase;

import java.io.File;

/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/
public class ParseDirections extends AbstractBaseSimpleCase {

    public static void main(String[] args) {
        File originFile = new File("G:\\Repertory\\Tencent\\WeChat\\xwechat_files\\wxid_wx134g6phllm12_831d\\msg\\file");


    }


    protected void parse(File originFile){
        if(originFile.isDirectory()){
            File[] files = originFile.listFiles();
            for (File f : files) {

            }
        }
    }

    protected void printFileList(File file){

    }
}
