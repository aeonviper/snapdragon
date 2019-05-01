package core.type;

import java.sql.SQLException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class AlternativeDateTimeHandlerCallback implements TypeHandlerCallback {

	public Object getResult(ResultGetter getter) throws SQLException {
		DateTime date = new DateTime(getter.getDate());
		return date;
	}

	public void setParameter(ParameterSetter setter, Object obj)
			throws SQLException {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime date;
		if(obj instanceof DateTime){
			date = (DateTime) obj;
			setter.setString(date.toString(format));
		}		
		else{
			throw new IllegalArgumentException("Illegal Date object");
		}
	}

	public Object valueOf(String string) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-mm-dd");
		DateTime date = format.parseDateTime(string);		
		return date;
	}

}
