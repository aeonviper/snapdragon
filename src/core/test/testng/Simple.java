package core.test.testng;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
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

public class Simple {

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
      
    
    @Test
    public void testSingletonSqlMapClient() {        
        for (int i=0;i<5;i++)
            assertEquals(true, TestFactory.getInstance(SqlMapClient.class) == TestFactory.getInstance(SqlMapClient.class));
    }
    
    
    
    
    
    @Test
    public void getNormal() {
        itemService.insert(item1);
        assertEquals(true, item1.equals(itemService.get(item1.getId())));    
    }
    
    @Test
    public void getNonExistant() {             
        assertNull(itemService.get(1000L));    
    }
    
    @Test(expectedExceptions = ApplicationException.class)
    public void getDuplicates() {
        assertEquals(item1.getName(), item2.getName());
        itemService.insert(item1);
        itemService.insert(item2);
        itemService.getByName(item1.getName());
    }
    
    
    
    
    
    
    
    
    @Test
    public void insertNormal() {
        itemService.insert(item1);
        assertEquals(true, item1.equals(itemService.get(item1.getId())));     
    }
    
    @Test
    public void insertExisting() {        
        itemService.insert(item1);
        boolean exceptionThrown = false;
        
        try {           
           itemService.insert(new Item(item1.getId(), "super villain", "mr. freeze"));
        } catch (ApplicationException e) {
           exceptionThrown = true;
        }
        
        if (!exceptionThrown) {
            throw new AssertionError();
        }
        
        assertEquals(true, item1.equals(itemService.get(item1.getId())));     
    }
    
    @Test
    public void insertInvalid() {       
        boolean exceptionThrown = false;
                
        try {           
           itemService.insert(new Item());
        } catch (ApplicationException e) {
           exceptionThrown = true;
        }
        
        if (!exceptionThrown) {
            throw new AssertionError();
        }
       
        assertEquals(0, itemService.list().size());     
    }
    
    @Test
    public void insertNull() {       
        boolean exceptionThrown = false;
                
        try {           
           itemService.insert(null);
        } catch (ApplicationException e) {
           exceptionThrown = true;
        }
        
        if (!exceptionThrown) {
            throw new AssertionError();
        }
       
        assertEquals(0, itemService.list().size());     
    }
    
    @Test
    public void insertViolatesConstraint() {        
        itemService.insert(item1);
        boolean exceptionThrown = false;
        
        try {           
           itemService.insert(new Item(99L, item1.getName(), item1.getValue()));
        } catch (ApplicationException e) {
           exceptionThrown = true;
        }
        
        if (!exceptionThrown) {
            throw new AssertionError();
        }
        assertEquals(1, itemService.list().size());
        assertEquals(true, item1.equals(itemService.get(item1.getId())));     
    }
    
    
    
    
    
    
    
    
    
    
    
    @Test
    public void updateNormal() {        
        int updated = 0;
        Item item = new Item(item1);
        itemService.insert(item);
        item.setName("super villain");
        item.setValue("lex luthor");
        updated = itemService.update(item);
        assertEquals(1, updated);
        assertEquals(true, item.equals(itemService.get(item.getId())));
    }
    
    @Test
    public void updateNonExistant() {        
        int updated = itemService.update(item1);
        assertEquals(0, updated);
        assertEquals(0, itemService.list().size());        
    }    
    
    @Test
    public void updateInvalid() {        
        int updated = itemService.update(new Item());
        assertEquals(0, updated);
        assertEquals(0, itemService.list().size());        
    }    
    
    @Test
    public void updateNull() {        
        int updated = 0;
        boolean exceptionThrown = false;
        try {
            updated = itemService.update(null);
        } catch (ApplicationException e) {
            exceptionThrown = true;
        }
        
        if (!exceptionThrown) {
            throw new AssertionError();
        }
        
        assertEquals(0, updated);
        assertEquals(0, itemService.list().size());        
    }    
    
    @Test
    public void updateViolatesConstraint() {      
        itemService.insert(item1);        
        Item copy1 = new Item(99L, "super villain", "lex luthor", 99);      
        itemService.insert(copy1);
        int updated = 0;
        
        boolean exceptionThrown = false;
        try {             
            
            Item copy2= new Item(copy1);            
            copy2.setName(item1.getName());
            copy2.setValue(item1.getValue());
            copy2.setVersion(77);
            updated = itemService.update(copy2);
        } catch (ApplicationException e) {
            exceptionThrown = true;
        }
        
        if (!exceptionThrown) {
            throw new AssertionError();
        }
        assertEquals(0, updated);
        assertEquals(2, (itemService.list().size()));
        assertEquals(false, copy1.getId() == item1.getId());
        assertEquals(true, copy1.equals(itemService.get(copy1.getId())));   
        assertEquals(true, item1.equals(itemService.get(item1.getId())));   
        
        List<Item> list = new ArrayList<Item>();
        list.add(item1);
        list.add(copy1);
        assertEquals(true, list.equals(itemService.list()));
    }
    
    
    
    
    
    
    
    
    
    
    
    @Test
    public void deleteNormal() {
        itemService.insert(item1);        
        assertEquals(true, item1.equals(itemService.get(item1.getId())));
        itemService.delete(item1); 
        assertNull(itemService.get(item1.getId()));      
        assertEquals(0, itemService.list().size());
    }
    
    @Test
    public void deleteNonExistant() {        
        int deleted = itemService.delete(item1);
        assertEquals(0, deleted);
        assertEquals(0, itemService.list().size());        
    }
    
    @Test
    public void deleteNull() {
        int deleted = 0;
        boolean exceptionThrown = false;
        try {
            deleted = itemService.delete(null);
        } catch (ApplicationException e) {
           exceptionThrown = true;
        }
        
        if (!exceptionThrown) {
            throw new AssertionError();
        }
        assertEquals(0, deleted);
        assertEquals(0, itemService.list().size());       
    }
    
    @Test
    public void deleteViolatesConstraint() {
        //todo
    }
    
    
    
    
    
    
    
    
    @Test
    public void listNormal() {
        for (Item i : items) {
            itemService.insert(i);
        }
        List<Item> listInDb = itemService.list();
        assertEquals(items.length, listInDb.size());
        assertEquals(true, listItems.equals(listInDb));
    }
    
    @Test
    public void listNothing() {        
        assertEquals(0, itemService.list().size());
    }
    
    @Test
    public void listOperations() {
        List<Item> listHere = new ArrayList<Item>();
        int n = 0;
        
        for (Item i : items) {            
            assertEquals(n, itemService.list().size());
            assertEquals(true, listHere.equals(itemService.list()));
            
            itemService.insert(i);
            n++;
            listHere.add(i);
            
            assertEquals(n, itemService.list().size());
            assertEquals(true, listHere.equals(itemService.list()));
        }
        
        int j = 0;
        for (Item i : items) {            
            assertEquals(n, itemService.list().size());
            assertEquals(true, listHere.equals(itemService.list()));
            
            listHere.remove(i);
            i.setName("super villain");
            itemService.update(i);            
            listHere.add(j, i);
            j++;
            
            assertEquals(n, itemService.list().size());
            assertEquals(true, listHere.equals(itemService.list()));            
        }
        
        for (Item i : items) {            
            assertEquals(n, itemService.list().size());
            assertEquals(true, listHere.equals(itemService.list()));
            
            itemService.delete(i); 
            listHere.remove(i);            
            n--;
            
            assertEquals(n, itemService.list().size());
            assertEquals(true, listHere.equals(itemService.list()));            
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
