package xyz.wongs.drunkard.base.po;

import java.io.Serializable;


/**
 * @author WCNGS@QQ.COM
 * @date 2020/8/2 13:32
 * @since 1.0.0
*/
public abstract class SuperPo<ID extends Serializable> implements Serializable {

    /** 无
     * @Description
     * @return ID
     * @date 2020/8/2 13:23
    */
    public abstract ID getId();

    /** 无
     * @Description
     * @param id
     * @date 2020/8/2 13:22
     */
    public abstract void setId(ID id);
}
