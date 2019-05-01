package core.aspect;

import java.io.Reader;
import com.google.inject.*;
import com.google.inject.name.*;
import com.ibatis.common.resources.*;
import com.ibatis.sqlmap.client.*;

public class SqlMapClientProvider implements Provider<SqlMapClient> {

    @Inject    
    @Named("sqlMapConfigName")
    private String configName;
    
    public void setConfigName(String configName) {
        this.configName = configName;
    } 

    public SqlMapClient get() {
        SqlMapClient sqlMap;

        try {
            Reader reader = Resources.getResourceAsReader(configName);
            sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing SqlMapClient. Cause: " + e);
        }

        return sqlMap;
    }

}
