package io.github.rothschil.common.base.persistence.repository;


import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/** 自定义参数
 * @program: common-ivr
 * @description:
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @create: 2025-11-14 14:33
 **/
@Data
public class Params {

    private final List<String> and;
    private final LinkedHashMap<String ,Object> andPara;

    private final List<String> or;
    private final LinkedHashMap<String ,Object> orPara;

    private final List<String> between;
    private final LinkedHashMap<String ,List> betweenPara;

    private final List<String> lessThan;
    private final LinkedHashMap<String ,Object> lessThanPara;

    private final List<String> moreThan;
    private final LinkedHashMap<String ,Object> moreThanPara;

    private final List<String> like;
    private final LinkedHashMap<String ,Object> likePara;

    private final List<String> notLike;
    private final LinkedHashMap<String ,Object> notLikePara;

    private final List<String> orderBy;
    private final LinkedHashMap<String ,Object> orderByPara;

    private final List<String> not;
    private final LinkedHashMap<String ,Object> notPara;

    private final List<String> in;
    private final LinkedHashMap<String ,List> inPara;

    private final List<String> notIn;
    private final LinkedHashMap<String ,List> notInPara;

    private final List<String> limit;
    private final LinkedHashMap<String ,List> limitPara;

    private Params(Bulider bulider){
        this.and = bulider.and;
        this.andPara = bulider.andPara;
        this.or = bulider.or;
        this.orPara = bulider.orPara;
        this.between = bulider.between;
        this.betweenPara = bulider.betweenPara;
        this.lessThan = bulider.lessThan;
        this.lessThanPara = bulider.lessThanPara;
        this.moreThan = bulider.moreThan;
        this.moreThanPara = bulider.moreThanPara;
        /*this.isNull = bulider.isNull;
        this.isNullPara = bulider.isNullPara;
        this.isNotNull = bulider.isNotNull;
        this.isNotNullPara = bulider.isNotNullPara;*/
        this.like = bulider.like;
        this.likePara = bulider.likePara;
        this.notLike = bulider.notLike;
        this.notLikePara = bulider.notLikePara;
        this.orderBy = bulider.orderBy;
        this.orderByPara = bulider.orderByPara;
        this.not = bulider.not;
        this.notPara = bulider.notPara;
        this.in = bulider.in;
        this.inPara = bulider.inPara;
        this.notIn = bulider.notIn;
        this.notInPara = bulider.notInPara;
        this.limit = bulider.limit;
        this.limitPara = bulider.limitPara;
    }

    public static Bulider builder() {
        return new Bulider();
    }

    public static class Bulider{
        private List<String> and;
        private LinkedHashMap<String ,Object> andPara;

        private List<String> or;
        private LinkedHashMap<String ,Object> orPara;

        private List<String> between;
        private LinkedHashMap<String ,List> betweenPara;

        private List<String> lessThan;
        private LinkedHashMap<String ,Object> lessThanPara;

        private List<String> moreThan;
        private LinkedHashMap<String ,Object> moreThanPara;

       /* private List<String> isNull;
        private LinkedHashMap<String ,Object> isNullPara;

        private List<String> isNotNull;
        private LinkedHashMap<String ,Object> isNotNullPara;*/

        private List<String> like;
        private LinkedHashMap<String ,Object> likePara;

        private List<String> notLike;
        private LinkedHashMap<String ,Object> notLikePara;

        private List<String> orderBy;
        private LinkedHashMap<String ,Object> orderByPara;

        private List<String> not;
        private LinkedHashMap<String ,Object> notPara;

        private List<String> in;
        private LinkedHashMap<String ,List> inPara;

        private List<String> notIn;
        private LinkedHashMap<String ,List> notInPara;

        private List<String> limit;
        private LinkedHashMap<String ,List> limitPara;

        public Bulider Euqal(String key , Object value){
            this.and = new ArrayList<>();
            this.andPara = new LinkedHashMap<>();
            this.and.add("=");
            this.andPara.put(key,value);
            return this;
        }

        public Bulider Or(String key , Object value){
            this.or = new ArrayList<>();
            this.orPara = new LinkedHashMap<>();
            this.or.add("or");
            this.orPara.put(key,value);
            return this;
        }

        public Bulider Between(String key , Object value1 , Object value2){
            this.between = new ArrayList<>();
            this.betweenPara = new LinkedHashMap<>();
            List value = new ArrayList();
            value.add(value1);
            value.add(value2);
            this.between.add("between");
            this.betweenPara.put(key,value);
            return this;
        }

        public Bulider LessThan(String key , Object value){
            this.lessThan = new ArrayList<>();
            this.lessThanPara = new LinkedHashMap<>();
            this.lessThan.add("<");
            this.lessThanPara.put(key,value);
            return this;
        }

        public Bulider MoreThan(String key , Object value){
            this.moreThan = new ArrayList<>();
            this.moreThanPara = new LinkedHashMap<>();
            this.moreThan.add(">");
            this.moreThanPara.put(key,value);
            return this;
        }

        public Bulider Like(String key , Object value){
            this.like = new ArrayList<>();
            this.likePara = new LinkedHashMap<>();
            this.like.add("like");
            this.likePara.put(key,value);
            return this;
        }

        public Bulider NotLike(String key , Object value){
            this.notLike = new ArrayList<>();
            this.notLikePara = new LinkedHashMap<>();
            this.notLike.add("notlike");
            this.notLikePara.put(key,value);
            return this;
        }

        public Bulider OrderBy(String key , Object value){
            this.orderBy = new ArrayList<>();
            this.orderByPara = new LinkedHashMap<>();
            this.orderBy.add("orderby");
            this.orderByPara.put(key,value);
            return this;
        }

        public Bulider NotEqual(String key , Object value){
            this.not = new ArrayList<>();
            this.notPara = new LinkedHashMap<>();
            this.not.add("!=");
            this.notPara.put(key,value);
            return this;
        }

        public Bulider In(String key , Object ... values){
            this.in = new ArrayList<>();
            this.inPara = new LinkedHashMap<>();
            List list = new ArrayList();
            for (Object value : values) {
                list.add(value);
            }
            this.in.add("in");
            this.inPara.put(key,list);
            return this;
        }

        public Bulider NotIn(String key , Object ... values){
            this.notIn = new ArrayList<>();
            this.notInPara = new LinkedHashMap<>();
            List list = new ArrayList();
            for (Object value : values) {
                list.add(value);
            }
            this.notIn.add("notin");
            this.notInPara.put(key,list);
            return this;
        }

        public Bulider Limit(Integer value1 , Integer value2){
            this.limit = new ArrayList<>();
            this.limitPara = new LinkedHashMap<>();
            List<Integer> list = new ArrayList();
            list.add(value1);
            list.add(value2);
            this.limit.add("limit");
            this.limitPara.put("limit",list);
            return this;
        }

        public Params build() {
            return new Params(this);
        }
    }

}
