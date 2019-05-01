package core.test.testng;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibatis.sqlmap.client.SqlMapClient;

import core.exception.ApplicationException;
import core.test.TestFactory;
import core.test.model.Item;
import core.test.service.ItemService;

public class CustomType {

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
    
    private DateTime normalize(DateTime dateTime) {
    	return new DateTime((dateTime.getMillis()/1000)*1000);
    }
    
    @Test
    public void saveAndGet() { 
    	DateTime now = normalize(new DateTime());
    	DateTime retrieved;
    	Item item = new Item(item1);
    	Item itemRetrieved;
    	item.setCreated(now);
        itemService.insert("Item.insertWithCreated", item);
        
        itemRetrieved = itemService.find(item.getId());
        retrieved = itemRetrieved.getCreated();
        
        assertTrue(now.equals(retrieved));        
    }
    
    @Test
    public void saveAndUpdate() {
    	DateTime lastWeek = new DateTime();
    	lastWeek = normalize(lastWeek.minusDays(7));
    	DateTime retrieved;
    	Item item = new Item(item2);
    	Item itemRetrieved;
    	item.setCreated(lastWeek);
        itemService.insert("Item.insertWithCreated", item);
        
        itemRetrieved = itemService.find(item.getId());
        retrieved = itemRetrieved.getCreated();
        
        assertTrue(lastWeek.equals(retrieved));
        
        lastWeek = normalize(lastWeek.minusDays(3));
        item.setCreated(lastWeek);
        itemService.update("Item.updateWithCreated", item);
        
        itemRetrieved = itemService.find(item.getId());
        retrieved = itemRetrieved.getCreated();
        
        assertTrue(lastWeek.equals(retrieved));
    }
    
    
    
    
    
}
