package xyz.wongs.drunkard.task;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImageInfoCase{

    private static Logger LOG = LoggerFactory.getLogger(ImageInfoCase.class);

    @Test
    public void excuDict(){
        long start  = System.currentTimeMillis();
        new RunFileTask().run("E:\\Repertory\\Lightroom\\Exp");
        long end  = System.currentTimeMillis();
        LOG.info("耗时 cost ={} 秒",(end-start)/1000);
    }

}
