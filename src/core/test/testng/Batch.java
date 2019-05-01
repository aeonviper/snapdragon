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
import core.service.GenericService;
import core.test.TestFactory;
import core.test.model.Item;
import core.test.service.ItemService;

public class Batch {

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
    public void batchInsertNormal() {
        itemService.batchInsert(item1, item2, item3);
        
        List<Item> listInDb = new ArrayList<Item>();
        listInDb.add(item1);
        listInDb.add(item2);
        listInDb.add(item3);
        
        assertEquals(3, itemService.list().size());
        assertEquals(true, listInDb.equals(itemService.list()));
    }
    
    
    @Test
    public void batchInsertFailMidway() {
        boolean exceptionThrown = false;
        
        try {
            itemService.batchInsert(item1, item2, item3, item3, item5);
        } catch (ApplicationException e) {
            exceptionThrown = true;
        }
        
        if (!exceptionThrown) {
            throw new AssertionError();
        }
        
        assertEquals(0, itemService.list().size());        
    }
    
    
    
    @Test
    public void batchUpdateNormal() {
        boolean exceptionThrown = false;
        List<Item> listInDb = new ArrayList<Item>();
        
        itemService.batchInsert(item1, item2, item3, item4, item5);
        
        Item copy1 = new Item(item1); copy1.setName("super villain");
        Item copy2 = new Item(item2); copy2.setName("super villain");
        Item copy3 = new Item(item3); copy3.setName("super villain"); 
        Item copy4 = new Item(item4); copy4.setName("super villain"); 
        Item copy5 = new Item(item5); copy5.setName("super villain");
        
        listInDb.add(copy1);
        listInDb.add(copy2);
        listInDb.add(copy3);
        listInDb.add(copy4);
        listInDb.add(copy5);
        
        itemService.batchUpdate(copy1, copy2, copy3, copy4, copy5);
        
        assertEquals(5, itemService.list().size());
        assertEquals(true, listInDb.equals(itemService.list()));
    }
    
    
    @Test
    public void batchUpdateFailMidway() {
        boolean exceptionThrown = false;
        
        itemService.batchInsert(item1, item2, item3, item4, item5);
        
        try {
            Item copy1 = new Item(item1); copy1.setName("super villain");
            Item copy2 = new Item(item2); copy2.setName("super villain");
            Item copy3 = new Item(item3); copy3.setName("super villain"); 
            Item copy4 = new Item(item4); copy4.setName("super villain"); copy4.setValue(copy1.getValue());
            Item copy5 = new Item(item5); copy5.setName("super villain");
            
            itemService.batchUpdate(copy1, copy2, copy3, copy4, copy5);
        } catch (ApplicationException e) {
            exceptionThrown = true;
        }
        
        if (!exceptionThrown) {
            throw new AssertionError();
        }
        
        List<Item> listInDb = new ArrayList<Item>();
        listInDb.add(item1);
        listInDb.add(item2);
        listInDb.add(item3);
        listInDb.add(item4);
        listInDb.add(item5);
        
        assertEquals(5, itemService.list().size());
        assertEquals(true, listInDb.equals(itemService.list()));
    }
    

    
    @Test
    public void batchDeleteNormal() {
        
        itemService.batchInsert(item1, item2, item3, item4, item5);
        itemService.batchDelete(item1, item2, item3, item4, item5);
        
        assertEquals(0, itemService.list().size());
    }
    
    
    @Test
    public void batchDeleteFailMidway() {
        boolean exceptionThrown = false;
        
        itemService.batchInsert(item1, item2, item3, item4, item5);        
        
        try {
            itemService.batchDelete(item1, item2, item3, null, item5);
        } catch (ApplicationException e) {
            exceptionThrown = true;
        }
        
        if (!exceptionThrown) {
            throw new AssertionError();
        }
        
        List<Item> listInDb = new ArrayList<Item>();
        listInDb.add(item1);
        listInDb.add(item2);
        listInDb.add(item3);
        listInDb.add(item4);
        listInDb.add(item5);
        
        assertEquals(5, itemService.list().size());
        assertEquals(true, listInDb.equals(itemService.list()));
    }
    
    
    
    
    @Test
    public void batchOperationNormal() {
        
        // should use an array ie. copy[3] instead of copy3, oh well
        Item copy1 = (new Item(item1)).setName("super villain");
        Item copy2 = (new Item(item2)).setName("super villain");
        Item copy3 = (new Item(item3)).setName("super villain");
        Item copy4 = (new Item(item4)).setName("super villain");
        Item copy5 = (new Item(item5)).setName("super villain");
        
        itemService.batchOperation(1, 1, 1, item1, copy1, item1);
        assertEquals(0, itemService.list().size());
        
        itemService.batchOperation(2, 2, 0, item1, item2, copy1, copy2);
        assertEquals(2, itemService.list().size());
        assertEquals(true, buildList(copy1, copy2).equals(itemService.list()));
        
        itemService.batchOperation(0, 0, 2, copy1, copy2);
        assertEquals(0, itemService.list().size());
        
        itemService.batchOperation(5, 2, 2, item1, item2, item3, item4, item5, copy2, copy5, item2, item3);
        
        assertEquals(3, itemService.list().size());
        assertEquals(true, buildList(item1, item4, copy5).equals(itemService.list()));
        
        itemService.batchOperation(3, 3, 0, copy2, copy3, item7, item2, copy1, copy4);
        
        assertEquals(6, itemService.list().size());
        assertEquals(true, buildList(copy1, item2, copy3, copy4, copy5, item7).equals(itemService.list()));
    }
    
    
    
    @Test
    public void batchOperationFailMidway() {
        
        // should use an array ie. copy[3] instead of copy3, oh well
        Item copy1 = (new Item(item1)).setName("super villain");
        Item copy2 = (new Item(item2)).setName("super villain");
        Item copy3 = (new Item(item3)).setName("super villain");
        Item copy4 = (new Item(item4)).setName("super villain");
        Item copy5 = (new Item(item5)).setName("super villain");
        
        boolean exceptionThrown; 
        
        
        exceptionThrown = false;
        try { 
            itemService.batchOperation(1, 1, 1, item1, copy1, null);
        } catch (ApplicationException e) {
            exceptionThrown = true;
            e.printStackTrace();
        }
        if (!exceptionThrown) { throw new AssertionError(); }
        assertEquals(0, itemService.list().size());
                
        
        itemService.batchOperation(2, 2, 0, item1, item2, copy1, copy2);
        assertEquals(2, itemService.list().size());
        assertEquals(true, buildList(copy1, copy2).equals(itemService.list()));
        
        
        try {
            itemService.batchOperation(1, 0, 2, item1, copy1, copy2);
        } catch (ApplicationException e) {
            exceptionThrown = true;
            e.printStackTrace();
        }
        if (!exceptionThrown) { throw new AssertionError(); }
        assertEquals(2, itemService.list().size());
        assertEquals(true, buildList(copy1, copy2).equals(itemService.list()));
        
        
        
        try {
            itemService.batchOperation(5, 2, 2, item6, item7, item3, item4, item5, copy2, (new Item(2L, copy1.getName(), copy1.getValue())), item2, item3);
        } catch (ApplicationException e) {
            exceptionThrown = true;
            e.printStackTrace();
        }
        if (!exceptionThrown) { throw new AssertionError(); }
        assertEquals(2, itemService.list().size());
        assertEquals(true, buildList(copy1, copy2).equals(itemService.list()));
        
                       
    }
    
    
    @Test
    public void isolation() {
        
        try {
            itemService.insertIsolationNone(item1);
        } catch (ApplicationException e) { e.printStackTrace(); }
        
        try {
            itemService.insertIsolationReadCommit(item2);
        } catch (ApplicationException e) { e.printStackTrace(); }
        
        try {
            itemService.insertIsolationReadUncommit(item3);
        } catch (ApplicationException e) { e.printStackTrace(); }
        
        try {
            itemService.insertIsolationRepeat(item4);
        } catch (ApplicationException e) { e.printStackTrace(); }
        
        try {
            itemService.insertIsolationSerial(item5);
        } catch (ApplicationException e) { e.printStackTrace(); }
        
    }
    
    
    
}
