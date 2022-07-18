package cn.edu.bit.patentbackend.utils;

import cn.edu.bit.patentbackend.bean.AdvancedSearchCondition;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.*;

public class ExpressionUtil {

    private static Map<Character, Integer> priority;

    private static Map<String, Character> logicMap;

    public ExpressionUtil() {}

    static {
        priority = new HashMap<>();
        priority.put(')', -2);
        priority.put('|', -1);
        priority.put('&', 0);
        priority.put('!', 1);
        priority.put('(', -2);
        logicMap = new HashMap<>();
        logicMap.put("and", '&');
        logicMap.put("or", '|');
        logicMap.put("not", '!');
    }

    /*
    * 表达式预处理：
    *   去空格
    *   中文括号 -> 英文括号
    *   中文双引号 -> 英文双引号
    *   中文 &|！ -> 英文 &|!
    * */
    public static String preprocess(String expression){
        expression = expression.replace(" ", "")
                .replace("（", "(")
                .replace("）", ")")
                .replace("“", "\"")
                .replace("”", "\"")
                .replace("&", "&")
                .replace("|", "|")
                .replace("！", "!");
        return expression;
    }

    /*
    * 将高级检索条件转化为表达式
    * */
    public static String mapToExpression(){

        return "";
    }

    public static QueryBuilder mapToQuery(){

        return null;
    }

    public static Boolean isValidCondition(AdvancedSearchCondition condition){
        if(condition == null){
            return false;
        }
        if(condition.getQueryText().isEmpty()){
            return false;
        }
        return true;
    }

    /*
    * 将高级检索前端传递来的条件，转换成 QueryBuilder
    * */
    public static QueryBuilder condition2Query(ArrayList<AdvancedSearchCondition> conditions){
        if(conditions.isEmpty()){
            return new BoolQueryBuilder();
        }
        Stack<Character> opStack = new Stack<>();
        Stack<QueryBuilder> queryStack = new Stack<>();
        for (int i = 0; i < conditions.size(); i++) {
            AdvancedSearchCondition curCondition = conditions.get(i);
            if(!isValidCondition(curCondition)){
                continue;
            }
            String matchType = curCondition.getMatchType();
            Character logicOp = logicMap.get(curCondition.getLogicOp());
            String queryText = curCondition.getQueryText();
            String field = curCondition.getField();
            // 不为 1时才需要处理运算符
            if(i != 0){
                if(opStack.isEmpty()){
                    opStack.push(logicOp);
                }else{
                    if(priority.get(logicOp) >= priority.get(opStack.peek())){
                        opStack.push(logicOp);
                    }else{
                        while(!opStack.isEmpty() && priority.get(logicOp) < priority.get(opStack.peek())){
                            calOnce(opStack, queryStack);
                        }
                        opStack.push(logicOp);
                    }
                }
            }
            QueryBuilder curBuilder = null;
            if(matchType.equals("exact")){
                curBuilder = QueryBuilders.termQuery(field, queryText);
            }else if(matchType.equals("fuzzy")){
                curBuilder = QueryBuilders.matchQuery(field, queryText);
            }else{
                // todo 定义一个异常
                throw new RuntimeException();
            }
            queryStack.push(curBuilder);
        }
        while(!opStack.isEmpty() && queryStack.size() > 1){
            calOnce(opStack, queryStack);
        }
        if(queryStack.size() == 1){
            return queryStack.peek();
        }else {
            throw new RuntimeException();
        }
    }

    /*
    * 将不含操作符的单个查询条件转化为 QueryBuilder
    * */
    public static QueryBuilder string2Query(String expression){
//        "title = 烟花"
        expression.replace(" ", "");
        String[] splitRes = expression.split("=");
        QueryBuilder query = null;
        if(splitRes[1].charAt(0) == '\"'){
            query = QueryBuilders.termQuery(splitRes[0], splitRes[1].replace("\"", ""));
        }else{
            query = QueryBuilders.matchQuery(splitRes[0], splitRes[1]);
        }
        return query;
    }

    /*
    * 将表达式解析为 Elasticsearch QueryBuilder
    * */
    public static QueryBuilder expression2Query(String expression) {
        if(expression.isEmpty()){
             return new BoolQueryBuilder();
         }
        expression = preprocess(expression);
        Stack<Character> opStack = new Stack<>();
        Stack<QueryBuilder> queryStack = new Stack<>();
        int index = 0;
        while(index < expression.length()){
            if(isOperator(expression, index)){
                Character curOp = expression.charAt(index);
                if(curOp == ')'){
                    while(!opStack.isEmpty() && opStack.peek() != '('){
                       calOnce(opStack, queryStack);
                    }
                    opStack.pop();
                }else {
                    if(opStack.isEmpty()){
                        opStack.push(expression.charAt(index));
                    }else{
                        if(priority.get(curOp) >= priority.get(opStack.peek())){
                            opStack.push(curOp);
                        }else{
                            while(!opStack.isEmpty() && priority.get(curOp) < priority.get(opStack.peek())){
                                calOnce(opStack, queryStack);
                            }
                            opStack.push(curOp);
                        }
                    }
                }
                index++;
            }else{
                int end = index + 1;
                while(end < expression.length() && !isOperator(expression, end)){
                    end++;
                }
                String queryString = expression.substring(index, end);
                queryStack.push(string2Query(queryString));
                index = end;
            }
        }
        while(!opStack.isEmpty()){
            calOnce(opStack, queryStack);
        }
        return queryStack.peek();
    }

    private static QueryBuilder calcuQuery(QueryBuilder firstQ, QueryBuilder secondQ, Character operator) {
        BoolQueryBuilder resQuery = new BoolQueryBuilder();
        if(operator == '!'){
            resQuery.mustNot(firstQ);
        }else if(operator == '&'){
            resQuery.must(firstQ).must(secondQ);
        }else if(operator == '|'){
            resQuery.should(firstQ).should(secondQ);
        }
        return resQuery;
    }

    private static void calOnce(Stack<Character> opStack, Stack<QueryBuilder> queryStack){
        BoolQueryBuilder newQuery = new BoolQueryBuilder();
        char op = opStack.pop();
        QueryBuilder secondQuery = queryStack.pop();
        QueryBuilder firstQuery = queryStack.pop();
        if(op == '&'){
            newQuery.must(firstQuery).must(secondQuery);
        }else if(op == '|'){
            newQuery.should(firstQuery).should(secondQuery);
        }else if(op == '!'){
            newQuery.must(firstQuery).mustNot(secondQuery);
        }
        queryStack.push(newQuery);
    }

    private static Boolean isOperator(String expression, int index){
        Character c = expression.charAt(index);
        if(c == '(' || c == ')' ||  c == '!' || c == '&' || c == '|'){
            return true;
        }else{
            return false;
        }
    }

}
