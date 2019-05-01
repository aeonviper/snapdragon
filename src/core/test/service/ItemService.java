package core.test.service;

import core.aspect.Transactional;
import core.service.GenericService;
import core.test.model.Item;

public class ItemService extends GenericService<Item, Long> {
	
	public ItemService() {
		setType(Item.class);
	}

    @Transactional
    public Item get(Long id) {
        return (Item) queryForObject("Item.get", id);
    }
    
    @Transactional
    public java.util.List  list() {        
        return queryForList("Item.list", null);
    }
    
    @Transactional
    public Object insert(Item item) {       
        return insert("Item.insert", item);
    }   
    
    @Transactional
    public int update(Item item) {        
        return update("Item.update", item);        
    }   
    
    @Transactional
    public int delete(Item item) {
        return delete("Item.delete", item);
    }
    
    @Transactional
    public int deleteAll() {
        return delete("Item.deleteAll", null);
    }
    
    
    
    
    
    @Transactional
    public Item getByName(String name) {
        return (Item) queryForObject("Item.getByName", name);
    }
    
    @Transactional
    public Item getByValue(String value) {
        return (Item) queryForObject("Item.getByValue", value);
    }
    
    
    
    
    
    
    
    
    @Transactional
    public void batchInsert(Item... items) {
        for (Item i : items) {
            insert(i);
        }
    }
    
    @Transactional
    public void batchUpdate(Item... items) {
        for (Item i : items) {
            update(i);
        }
    }
    
    @Transactional
    public void batchDelete(Item... items) {
        for (Item i : items) {
            delete(i);
        }
    }
    
    
    @Transactional
    public void batchOperation(int numInserts, int numUpdates, int numDeletes, Item... items) {
        int i;
        int j = 0;
        
        for (i=0;i<numInserts;i++) {
            if (j<items.length) {
                insert(items[j++]);
            }
        }
        
        for (i=0;i<numUpdates;i++) {
            if (j<items.length) {
                update(items[j++]);
            }
        }
        
        for (i=0;i<numDeletes;i++) {
            if (j<items.length) {
                delete(items[j++]);
            }
        }
        
    }
    
    
    
    
    
    @Transactional
    public void noOp(int level, int executeLevel, String operation, Item... items) {
        if (level == executeLevel) {
            if ("insert".equals(operation)) {
                batchInsert(items);
            } else if ("update".equals(operation)) {
                batchUpdate(items);
            } else if ("delete".equals(operation)) {
                batchDelete(items);
            }
        } else {
            noOp(level+1, executeLevel, operation, items);
        }
    }
    
    @Transactional(isolation="none")
    public void insertIsolationNone(Item item) {
        insert(item);
    }
    
    @Transactional(isolation="read_commit")
    public void insertIsolationReadCommit(Item item) {
        insert(item);
    }
    
    @Transactional(isolation="read_uncommit")
    public void insertIsolationReadUncommit(Item item) {
        insert(item);
    }
    
    @Transactional(isolation="repeat")
    public void insertIsolationRepeat(Item item) {
        insert(item);
    }
    
    @Transactional(isolation="serial")
    public void insertIsolationSerial(Item item) {
        insert(item);
    }
    
    
}
