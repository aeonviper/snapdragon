package core.test.testng;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibatis.sqlmap.client.SqlMapClient;

import core.exception.ApplicationException;
import core.test.TestFactory;
import core.test.model.Item;
import core.test.service.ItemService;

public class Parallel {
      
	private ItemService itemService = TestFactory.getInstance(ItemService.class);    
    private final Item item1 = new Item(1L, "superhero", "superman"); 
    private final Item item2 = new Item(2L, "superhero", "batman"); 
    private final Item item3 = new Item(3L, "superhero", "spiderman"); 
    private final Item item4 = new Item(4L, "superhero", "human torch"); 
    private final Item item5 = new Item(5L, "superhero", "mr. fantastic"); 
    private Item[] items = {item1, item2, item3, item4, item5};
    
    @BeforeClass
    public void init() {
        itemService.deleteAll();     
        itemService.setSwallowException(false);
        System.out.println(ItemService.class + " is now " + itemService.isSwallowException());        
    }
        
    public void insert() {
        String name = Thread.currentThread().getName();    
        Item copy;
        Item retrieved;
        double r;
        
        for (Item i : items) {            
            copy = new Item(i);
            copy.setName(name + " " + i.getId());
            copy.setValue(name + " " + i.getId());
            
            try {
                itemService.insert(copy);               
            } catch (ApplicationException e) {
            	System.out.println("insert " + e);
                retrieved = itemService.get(copy.getId());
                assertEquals(false, copy.equals(retrieved), "insert() " + name + " insert failed, but " + copy + " == " + retrieved);
                continue;
            }            
            retrieved = itemService.get(copy.getId());
            assertEquals(true, copy.equals(retrieved), "insert() " + name + " insert ok, but " + copy + " != " + retrieved);
        }        
    }
    
    public void update() {
        String name = Thread.currentThread().getName();    
        Item copy;
        Item retrieved;
        double r;
        
        for (Item i : items) {            
            copy = itemService.get(i.getId());
            
            if (copy == null) {
                continue;
            }
            copy.setValue(name + " " + i.getId() + " updated");
            
            try {
                if (0 == itemService.update(copy)) {
                    throw new ApplicationException("no update");
                }
            } catch (ApplicationException e) {
            	System.out.println("update " + e);
                retrieved = itemService.get(copy.getId());
                assertEquals(false, copy.equals(retrieved),"update() " + name + " update failed, but " + copy + " == " + retrieved);
                continue;
            }            
            retrieved = itemService.get(copy.getId());
            assertEquals(true, copy.equals(retrieved), "update() " + name + " update ok, but " + copy + " != " + retrieved );
        }        
    }
    
    @Test
    public void insert1() { insert(); }  
    @Test
    public void insert2() { insert(); }  
    @Test
    public void insert3() { insert(); }  
    @Test
    public void insert4() { insert(); }  
    @Test
    public void insert5() { insert(); }  
    
    @Test
    public void update1() { update(); }  
    @Test
    public void update2() { update(); }  
    @Test
    public void update3() { update(); }  
    @Test
    public void update4() { update(); }  
    @Test
    public void update5() { update(); }  
    
    
    
    
}
