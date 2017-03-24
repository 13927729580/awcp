package org.szcloud.framework.workflow.core.core.jdbc;

public interface DataSourceInfo {
    String getName();

    void setName(String name);

    void validate();
}
