package xyz.wongs.drunkard.design.iterator;

/** 定义访问和遍历聚合元素的接口，通常包含 hasNext()、first()、next() 等方法
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 2021/10/8 - 18:40
 * @since 1.0.0
 */
public interface Iterator {
    Object first();
    Object next();
    boolean hasNext();
}