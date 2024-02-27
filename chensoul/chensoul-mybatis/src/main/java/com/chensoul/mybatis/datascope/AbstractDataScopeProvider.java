package com.chensoul.mybatis.datascope;

import java.util.List;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class AbstractDataScopeProvider implements IDataScopeProvider {

    /**
     * @param args
     * @param mappedStatement
     * @param process
     */
    public static void processStatements(Object[] args, MappedStatement mappedStatement, Process process) {
        DataScopeHelper.processStatements(args, mappedStatement, process);
    }

    /**
     * @param args
     * @param mappedStatement
     * @param sqlCommandType
     * @throws Exception
     */
    public void sqlRender(Object[] args, MappedStatement mappedStatement, SqlCommandType sqlCommandType)
            throws Exception {
        DataScopeProperty dataScopeProperty = DataScopeHelper.getDataScopeProperty(mappedStatement.getId());
        if (null != dataScopeProperty) {
            if (sqlCommandType == SqlCommandType.INSERT) {
                this.processInsert(args, mappedStatement, dataScopeProperty);
            } else if (sqlCommandType == SqlCommandType.UPDATE) {
                this.processUpdate(args, mappedStatement, dataScopeProperty);
            } else if (sqlCommandType == SqlCommandType.DELETE) {
                this.processDelete(args, mappedStatement, dataScopeProperty);
            } else if (sqlCommandType == SqlCommandType.SELECT) {
                this.processSelect(args, mappedStatement, dataScopeProperty);
            }
        }
    }

    /**
     * @param args
     * @param mappedStatement
     * @param dataScopeProperty
     */
    public void processInsert(Object[] args, MappedStatement mappedStatement, DataScopeProperty dataScopeProperty) {
    }

    /**
     * @param args
     * @param mappedStatement
     * @param dataScopeProperty
     */
    public void processUpdate(Object[] args, MappedStatement mappedStatement, DataScopeProperty dataScopeProperty) {
    }

    /**
     * @param args
     * @param mappedStatement
     * @param dataScopeProperty
     */
    public void processDelete(Object[] args, MappedStatement mappedStatement, DataScopeProperty dataScopeProperty) {
    }

    /**
     * @param args
     * @param mappedStatement
     * @param dataScopeProperty
     */
    public void processSelect(Object[] args, MappedStatement mappedStatement, DataScopeProperty dataScopeProperty) {
        processStatements(args, mappedStatement,
                (statement, index) -> this.processSelect((Select) statement, args, dataScopeProperty));
    }

    /**
     * @param select
     * @param args
     * @param dataScopeProperty
     */
    private void processSelect(Select select, Object[] args, DataScopeProperty dataScopeProperty) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            this.setWhere((PlainSelect) selectBody, args, dataScopeProperty);
        } else if (selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodyList = setOperationList.getSelects();
            selectBodyList.forEach(sb -> this.setWhere((PlainSelect) sb, args, dataScopeProperty));
        }

    }

    /**
     * @param plainSelect
     * @param args
     * @param dataScopeProperty
     */
    public abstract void setWhere(PlainSelect plainSelect, Object[] args, DataScopeProperty dataScopeProperty);

}
