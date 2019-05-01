package core.test.testng;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibatis.sqlmap.client.SqlMapClient;

import core.test.TestFactory;
import core.test.model.Item;
import core.test.service.ItemService;

public class Single {

	private ItemService itemService = TestFactory.getInstance(ItemService.class);    
    private final Item item1 = new Item(1L, "superhero", "superman"); 
    private final Item item2 = new Item(2L, "superhero", "batman"); 
    private final Item item3 = new Item(3L, "superhero", "spiderman"); 
    private final Item item4 = new Item(4L, "superhero", "human torch"); 
    private final Item item5 = new Item(5L, "superhero", "mr. fantastic"); 
    private final Item item6 = new Item(6L, "superhero", "invisible girl"); 
    private final Item item7 = new Item(7L, "superhero", "the thing"); 
    private Item[] items = {item1, item2, item3, item4, item5, item6, item7};
    private List<Item> listItems = Arrays.asList(items);
    
    
    @BeforeClass
    public void init() {
    	itemService.setSwallowException(false);
    }
    
    @BeforeMethod
    public void cleanTable() {        
        itemService.deleteAll();
    }
    
    public <T> List<T> buildList(T... items) {
        List<T> list = new ArrayList<T>();
        for (T i : items) {
            list.add(i);
        }
        return list;
    }
    
    @Test
    public void find() {
    	Item item;
    	itemService.insert("Item.insert", item1);
    	item = itemService.find(item1.getId());
    	assertTrue(item1.equals(item));
    }
    
    @Test
    public void update() {
    	Item item;
    	Item updatedItem;
    	
    	itemService.insert("Item.insert", item1);
    	item = itemService.find(item1.getId());
    	assertTrue(item1.equals(item));
    	
    	item.setName("Elephant");
    	item.setValue("Safari");
    	item.setVersion(item.getVersion()+1);
    	itemService.update(item);
    	
    	updatedItem = itemService.find(item1.getId());
    	assertTrue(item.equals(updatedItem));
    }
    
    @Test
    public void list() {
    	for (Item item : items) {
    		itemService.insert("Item.insert", item);
    	}
    	List<Item> list = itemService.list();
    	assertEquals(items.length, list.size());
    	for (Item item : items) {
    		assertTrue(list.contains(item));
    	}
    }
    
    @Test
    public void remove() {
    	int length;
    	for (Item item : items) {
    		itemService.insert("Item.insert", item);
    	}
    	List<Item> list = itemService.list();
    	assertEquals(items.length, list.size());
    	length = items.length;
    	for (Item item : items) {
    		itemService.remove(item.getId());
    		length--;
    		assertEquals(length, itemService.list().size());
    		assertNull(itemService.find(item.getId()));
    	}
    }
    
    
}
