package cn.edu.bit.patentbackend.utils;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.HashMap;
import java.util.Map;

/*
* 提供 Java / JS Object 字段名 与 ES fieldname 之间的相互映射
* */
public class PatentFieldMapper {
    private static Map<String, String> obj2Doc;
    private static Map<String, String> doc2Obj;

    static {
        obj2Doc = new HashMap<>();
        doc2Obj = new HashMap<>();
        obj2Doc.put("title", "title");
        obj2Doc.put("abstractText", "abstract");
        obj2Doc.put("signoryItem", "signory_item");
        obj2Doc.put("patentCode", "title");
        obj2Doc.put("publicationNo", "publication_no");
        obj2Doc.put("publicationDate", "publication_date");
        obj2Doc.put("patentCode", "patent_code");
        obj2Doc.put("applicationDate", "application_date");
        obj2Doc.put("mainClassNumber", "main_class_list");
        obj2Doc.put("classNumber", "class_list");
        obj2Doc.put("mainClassNumber", "main_class_list");
        obj2Doc.put("applicant", "applicant");
        obj2Doc.put("inventor", "inventor");
        obj2Doc.put("applicant", "applicant");
        obj2Doc.put("agency", "agency");
        obj2Doc.put("applicantAddress", "applicant_address");
        obj2Doc.put("agent", "agent");
        for (Map.Entry<String, String> entry: obj2Doc.entrySet()){
            doc2Obj.put(entry.getValue(), entry.getKey());
        }
    }

    public static String getObjField(String docField){
        if(doc2Obj.get(docField) == null){
            // todo implement the exception
            throw new RuntimeException();
        }
        return doc2Obj.get(docField);
    }

    public static String getDocField(String objField){
        if(obj2Doc.get(objField) == null){
            // todo implement the exception
            throw new RuntimeException();
        }
        return obj2Doc.get(objField);
    }

    public static QueryBuilder simpleQuery(String field, String query){
        if(field.equals("abstractText")
                || field.equals("title")
                || field.equals("signoryItem")){
            return fuzzyQuery(field, query);
        }
        if(field.equals("agency")
                || field.equals("mainClassNo")
                || field.equals("classNo")
                || field.equals("applicant")
                || field.equals("inventor")
                || field.equals("publicationNo")
                || field.equals("patentCode")
                || field.equals("agent")){
            return exactQuery(field, query);
        }
        throw new RuntimeException();
    }

    public static QueryBuilder fuzzyQuery(String field, String query){
        if(field.equals("abstractText")
                || field.equals("title")
                || field.equals("signoryItem")){
            return QueryBuilders.matchQuery(getDocField(field), query).analyzer("ik_smart");
        }
        throw new RuntimeException();
    }

    public static QueryBuilder exactQuery(String field, String query){
        // 以下所用字段名是已转换成 ES 对应的字段名
        if(field.equals("publicationNo")
                || field.equals("patentCode")
                || field.equals("agent")){
            return QueryBuilders.termQuery(getDocField(field), query);
        }
        if(field.equals("agency")
                || field.equals("mainClassNo")
                || field.equals("classNo")
                || field.equals("applicant")
                || field.equals("inventor")
                || field.equals("abstract")
                || field.equals("title")
                || field.equals("signoryItem")){
            return QueryBuilders.matchQuery(getDocField(field), query).analyzer("keyword");
        }
        throw new RuntimeException();
    }

}
