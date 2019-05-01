package core.service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.ibatis.sqlmap.client.SqlMapClient;

import core.aspect.Transactional;
import core.exception.ApplicationException;
import core.model.GenericEntity;

public class GenericService <T extends GenericEntity<ID>,ID extends Serializable> {
	
	private Class<T> type;
    
    static final ThreadLocal<Boolean> transactionallyScoped = new ThreadLocal<Boolean>() {
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };
    
    protected SqlMapClient sqlMap;
    protected boolean swallowException = true;

    public SqlMapClient getSqlMap() {
        return sqlMap;
    }
    
    @Inject
    public void setSqlMap(SqlMapClient sqlMap) {
        this.sqlMap = sqlMap;
    }    

    public boolean isSwallowException() {
        return swallowException;
    }

    public void setSwallowException(boolean swallow) {
        swallowException = swallow;
    }

    public static boolean isInTransaction() {
        return transactionallyScoped.get().booleanValue();
    }
    
    public static void startTransaction() {
        transactionallyScoped.set(Boolean.TRUE);
    }
    
    public static void endTransaction() {
        transactionallyScoped.set(Boolean.FALSE);
    }
    
    public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type; 
	}
    
    
    /* generic */
	
	@Transactional
	public Object find(String sqlId, Object parameter) {
		return queryForObject(sqlId, parameter);
	}
    
	@Transactional
	public Object queryForObject(String sqlId, Object parameter) {
        Object result = null;
        try {
            result = sqlMap.queryForObject(sqlId, parameter);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
        return result;
    }
    
    @Transactional
    public List list(String sqlId, Object parameter) {
    	return queryForList(sqlId, parameter);
    }
    
    @Transactional
    public List queryForList(String sqlId, Object parameter) {
        List result = new ArrayList(0);
        try {
            result = sqlMap.queryForList(sqlId, parameter);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
        return result;
    }
    
    @Transactional
    public Object insert(String sqlId, Object parameter) {
        Object result = null;
        try {
            result = sqlMap.insert(sqlId, parameter);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
        return result;
    }
    
    @Transactional
    public int update(String sqlId, Object parameter) {
        int result = 0;
        try {
            result = sqlMap.update(sqlId, parameter);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
        return result;
    }
    
    @Transactional
    public int delete(String sqlId, Object parameter) {
        int result = 0;
        try {
            result = sqlMap.delete(sqlId, parameter);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
        return result;
    }
    
    
    /* parameterized */
    
    @Transactional
    public T find(ID id) {
        T result = null;
        try {
            result = (T) sqlMap.queryForObject(type.getSimpleName() + ".find", id);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
        return result;
    }
    
    @Transactional
    public List<T> list() {
        List<T> result = new ArrayList<T>(0);
        try {
            result = sqlMap.queryForList(type.getSimpleName() + ".list");
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
        return result;
    }
    
    @Transactional
    public ID save(T entity) {
        ID id = null;
        try {
        	if (entity.getId() == null) {
        		id = (ID) sqlMap.insert(type.getSimpleName() + ".insert", entity);
        	} else {        		
        		int affected = sqlMap.update(type.getSimpleName() + ".update", entity);
        		id = entity.getId();
        	}
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
        return id;
    }    
    
    @Transactional
    public void remove(ID id) {        
        try {
            int affected = sqlMap.delete(type.getSimpleName() + ".remove", id);
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }
  
}
