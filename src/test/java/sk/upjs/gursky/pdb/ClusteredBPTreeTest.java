package sk.upjs.gursky.pdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sk.upjs.gursky.bplustree.entries.BPObjectIntDouble;

public class ClusteredBPTreeTest {

	private static final File INDEX_FILE = new File("person.kl");
	private ClusteredBPTree bptree;
	
	@Before
	public void setUp() throws Exception {
		long time = System.nanoTime();
//		bptree = new ClusteredBPTree(Generator.GENERATED_FILE, INDEX_FILE);
//		time = System.nanoTime() - time;
//		System.out.println("one by one time: " + time/1000000.0 + "ms");
//		bptree.close();
//		INDEX_FILE.delete();
//		
//		time = System.nanoTime();
		bptree = ClusteredBPTree.newTreeBulkLoading(Generator.GENERATED_FILE, INDEX_FILE);
		time = System.nanoTime() - time;
		System.out.println("bulk load time: " + time/1000000.0 + "ms");
	}

	@After
	public void tearDown() throws Exception {
		bptree.close();
		INDEX_FILE.delete();
	}

	@Test
	public void test() throws Exception {	
		long time = System.nanoTime();
		List<PersonEntry> result = bptree.intervalQuery(new PersonStringKey("a"), new PersonStringKey("b999999999"));
		time = System.nanoTime() - time;
		
		System.out.println("Result size: " + result.size() + ", Time: " + time/1000000.0 + "ms");

		assertTrue(result.size() > 0);
	}
	
//	@Test
	public void testAdd() throws Exception {	
		PersonEntry prev = null;
		int count = 100;
        for (PersonEntry entry : bptree) {
            System.out.println(entry);
            if (prev != null) {
            	assertTrue(0 > prev.getKey().compareTo(entry.getKey()));
            }
            prev = entry;
            count--;
            if (count == 0) {
            	break;
            }
        }
		
	}
}
