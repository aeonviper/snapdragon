package core.type;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import org.joda.time.*;

public class DateTimeTypeHandlerCallback implements TypeHandlerCallback {

	private long k = 1000;
	
	@Override
	public Object getResult(ResultGetter getter) throws SQLException {
		Long l = getter.getLong();
		if (l != null) {
			return new DateTime(l*k);
		}
		return null;
	}

	@Override
	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		DateTime dateTime = (DateTime) parameter;
		if (dateTime != null) {
			setter.setLong(dateTime.getMillis()/k);
		} else {
			setter.setNull(java.sql.Types.BIGINT);
		}
	}

	@Override
	public Object valueOf(String s) {		
		DateTime dateTime = null;
		try {			
			dateTime = new DateTime(Long.parseLong(s));
		} catch (NumberFormatException nfe) {}
		return dateTime;
	}

}
