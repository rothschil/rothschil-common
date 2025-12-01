package io.github.rothschil.common.base.persistence.repository;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: common-ivr
 * @description:
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @create: 2025-11-14 14:36
 **/
public class RepositoryUtils {

    public static int concatSqlForObj(int index, List param, StringBuilder sql, List<String> symbols, LinkedHashMap<String, Object> params) {
        //拼接sql时需要的参数索引
        int index1 = index;
        //取list中的键值对是需要的索引
        int x = 0;
        if (symbols != null && symbols.size() != 0) {
            for (String symbol : symbols) {
                //通过转换list获取键值对
                List<Map.Entry<String, Object>> indexedList = new ArrayList<Map.Entry<String, Object>>(params.entrySet());
                Map.Entry<String, Object> entry = indexedList.get(x);
                String key = entry.getKey();
                Object value = entry.getValue();

                index1++;
                x++;

                if (symbol.equals("or")) {
                    sql.append(" or u.").append(key).append("=?").append(index1);
                } else if (symbol.equals("<")) {
                    sql.append(" and u.").append(key).append("<?").append(index1);
                } else if (symbol.equals(">")) {
                    sql.append(" and u.").append(key).append(">?").append(index1);
                } else if (symbol.equals("like")) {
                    sql.append(" and u.").append(key).append(" like ?").append(index1);
                } else if (symbol.equals("notlike")) {
                    sql.append(" and u.").append(key).append(" not like ?").append(index1);
                } else if (symbol.equals("!=")) {
                    sql.append(" and u.").append(key).append(" != ?").append(index1);
                } else if (symbol.equals("orderby")) {
                    //val的值可取asc 和 desc
                    sql.append(" ORDER BY ").append(key).append(" ").append(value.toString());
                    return index1;
                } else {
                    sql.append(" and u." + key + symbol + "?" + index1);
                }
                param.add(value);
            }
        }
        return index1;
    }

    public static int concatSqlForList(int index, List param, StringBuilder sql, List<String> symbols, LinkedHashMap<String, List> params) {
        //拼接sql时需要的参数索引
        int index1 = index;
        //取list中的键值对是需要的索引
        int x = 0;
        if (symbols != null && symbols.size() != 0) {
            for (String symbol : symbols) {
                //通过转换list获取键值对
                List<Map.Entry<String, List>> indexedList = new ArrayList<Map.Entry<String, List>>(params.entrySet());
                Map.Entry<String, List> entry = indexedList.get(x);
                String key = entry.getKey();
                List value = entry.getValue();

                index1++;
                x++;

                if (symbol.equals("between")) {
                    sql.append(" and u.").append(key).append(" between ?").append(index1).append(" and ?").append(index1 + 1);
                    index1++;
                } else if (symbol.equals("in") || symbol.equals("notin")) {
                    sql.append(" and u.").append(key);
                    if (symbol.equals("in")) {
                        sql.append(" in (?");
                    } else {
                        sql.append(" not in (?");
                    }
                    sql.append(index1);
                    param.add(value.get(0));
                    for (int i = 1; i < value.size(); i++) {
                        index1++;
                        sql.append(",?").append(index1);
                        param.add(value.get(i));
                    }
                    sql.append(")");
                    return index1;
                }
                param.add(value.get(0));
                param.add(value.get(1));
            }
        }
        return index1;
    }
}
