package core.test.model;

import org.apache.commons.lang.builder.*;
import org.joda.time.DateTime;

import core.model.GenericEntity;

public class Item implements GenericEntity<Long>{

	private Long id;
	private String name;
	private String value;
	private Integer version;
	private DateTime created;

	public Item() {}

	public Item(Long id, String name, String value) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.version = 0;
	}
	
	public Item(Long id, String name, String value, Integer version) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.version = version;
    }
	
	public Item(Item item) {
		if (item != null) {
			if (item.getId() != null)
				this.id = new Long(item.getId().longValue());
			this.name = item.getName();
			this.value = item.getValue();
			this.version = item.getVersion();
		}
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public Item setName(String name) {
		this.name = name;
		return this;
	}
	public String getValue() {
		return value;
	}
	public Item setValue(String value) {
		this.value = value;
		return this;
	}
	public Integer getVersion() {
        return version;
    }
    public Item setVersion(Integer version) {
        this.version = version;
        return this;
    }

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public String toString() {
		return "[" + id + "," + name + "," + value + "," + version + "]";
	}

	public boolean equals(Object o) {
		
		if (o == null) { return false; }
		if (!(o instanceof Item)) { return false; }
		
		Item i = (Item)o;
		
		if (id != null && i.getId() != null && id.longValue() == i.getId().longValue()) {} 
		else if (id == null && i.getId() == null) {}
		else { return false; }
		
		if (name != null && name.equals(i.getName())) {} 
		else if (name == null && i.getName() == null) {}
		else { return false; }
		
		if (value != null && value.equals(i.getValue())) {}
		else if (value == null && i.getValue() == null) {}
		else { return false; }
		
		if (version != null && version.equals(i.getVersion())) {}
        else if (version == null && i.getVersion() == null) {}
        else { return false; }
		
		return true;
	}
	
	public int hashCode() {
		return new HashCodeBuilder(77,19)
				.append(id)
				.append(name)
				.append(value)
				.append(version)
				.toHashCode();
			
	}

}
