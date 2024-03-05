package com.chensoul.mybatis.spring.boot.datascope;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.chensoul.mybatis.spring.boot.annotation.DataScope;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class DataScopeHelper {
    private static Map<String, DataScopeProperty> dataScopePropertyMap;

    private static List<String> ignoreStatementIdList;

    /**
     *
     */
    private DataScopeHelper() {
    }

    /**
     *
     */
    public static void init() {
        dataScopePropertyMap = new ConcurrentHashMap<>();
        ignoreStatementIdList = new CopyOnWriteArrayList<>();
    }

    /**
     * @param mappedStatementId
     * @return
     */
    public static DataScopeProperty getDataScopeProperty(String mappedStatementId) {
        DataScopeProperty dataScopeProperty = dataScopePropertyMap.get(mappedStatementId);

        if (null != dataScopeProperty) {
            return dataScopeProperty;
        }

        String className = mappedStatementId.substring(0, mappedStatementId.lastIndexOf("."));
        if (null == className || DataScopeProperty.CHECK_INSTANCE == dataScopePropertyMap.get(className)) {
            return null;
        }

        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }

        DataScope dataScope = (DataScope) clazz.getAnnotation(DataScope.class);
        if (dataScope != null && dataScope.ignoreMethods() != null) {
            for (String method : dataScope.ignoreMethods()) {
                ignoreStatementIdList.add(className + "." + method);
            }
        }

        for (Method method : clazz.getMethods()) {
            DataScope methodDataScope = method.getAnnotation(DataScope.class);
            if (null != methodDataScope && !methodDataScope.ignore()
                && !ignoreStatementIdList.contains(className + "." + method)) {
                dataScopePropertyMap.put(className + "." + method.getName(), new DataScopeProperty(methodDataScope));
            }
        }

        dataScopePropertyMap.put(className,
                null == dataScope ? DataScopeProperty.CHECK_INSTANCE : new DataScopeProperty(dataScope));

        if (ignoreStatementIdList.contains(mappedStatementId)) {
            return null;
        } else {
            dataScopeProperty = dataScopePropertyMap.get(className);
        }

        return dataScopeProperty;
    }

    /**
     * @param args
     * @param mappedStatement
     * @param process
     */
    public static void processStatements(Object[] args, MappedStatement mappedStatement, Process process) {
        BoundSql boundSql = mappedStatement.getBoundSql(args[1]);
        String sql = boundSql.getSql();

        try {
            StringBuilder stringBuilder = new StringBuilder();
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            int i = 0;

            for (Iterator iterator = statements.getStatements().iterator(); iterator.hasNext(); ++i) {
                Statement statement = (Statement) iterator.next();
                if (i > 0) {
                    stringBuilder.append(";");
                }

                process.process(statement, i);
                stringBuilder.append(statement);
            }

            args[0] = MappedStatementHelper.build(mappedStatement, new BoundSql(mappedStatement.getConfiguration(),
                    stringBuilder.toString(), boundSql.getParameterMappings(), boundSql.getParameterObject()));

        } catch (JSQLParserException e) {
            throw ExceptionUtils.mpe("Failed to process, Error SQL: %s", e.getCause(), new Object[]{sql});
        }
    }

}
