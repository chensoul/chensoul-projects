package com.chensoul.mybatis.spring.boot.datascope;

import com.chensoul.mybatis.spring.boot.annotation.DataColumn;
import com.chensoul.mybatis.spring.boot.annotation.DataScope;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class DataScopeProperty {

    public static final DataScopeProperty CHECK_INSTANCE = new DataScopeProperty();

    private String type;

    private List<DataColumnProperty> columns;

    /**
     *
     */
    public DataScopeProperty() {
    }

    /**
     * @param dataScope
     */
    public DataScopeProperty(DataScope dataScope) {
        this.type = dataScope.type();
        this.setColumns(dataScope.value());
    }

    /**
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param var1
     */
    public void setType(String var1) {
        this.type = var1;
    }

    /**
     * @return
     */
    public List<DataColumnProperty> getColumns() {
        return this.columns;
    }

    /**
     * @param dataColumns
     */
    public void setColumns(DataColumn[] dataColumns) {
        if (null != dataColumns && dataColumns.length > 0) {
            this.columns = Arrays.stream(dataColumns)
                    .map((dataColumn) -> new DataColumnProperty(dataColumn.alias(), dataColumn.name()))
                    .collect(Collectors.toList());
        }

    }

}
