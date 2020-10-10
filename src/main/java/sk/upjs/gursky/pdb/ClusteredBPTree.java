package sk.upjs.gursky.pdb;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.upjs.gursky.bplustree.BPTree;

public class ClusteredBPTree extends BPTree<PersonStringKey, PersonEntry> {

	public static final int PAGE_SIZE = 4096;
	
	private ClusteredBPTree(File indexFile) {
		super(PersonEntry.class, indexFile);
	}

	public ClusteredBPTree(File personsFile, File indexFile) throws IOException {
		super(PersonEntry.class, indexFile);
		setNodeSize(PAGE_SIZE);

		openNewFile();

		RandomAccessFile raf = new RandomAccessFile(personsFile, "r");

		FileChannel channel = raf.getChannel();
		ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
		
		long fileSize = personsFile.length();
		long pagesCount = fileSize / PAGE_SIZE;
		for (int offset = 0; offset < fileSize; offset+= PAGE_SIZE) {
//			System.out.println("Čítam " + offset/PAGE_SIZE + "stránku");
			buffer.clear();
			channel.read(buffer, offset);
			buffer.rewind();
			int personsCount = buffer.getInt();
			for (int i = 0; i < personsCount; i++) {
				PersonEntry entry = new PersonEntry();
				entry.load(buffer);
				add(entry);
			}
		}
		channel.close();
		raf.close();

	}
	
	public static ClusteredBPTree newTreeBulkLoading(File personsFile, File indexFile) throws Exception {
		ClusteredBPTree tree = new ClusteredBPTree(indexFile);
		tree.setNodeSize(PAGE_SIZE);
		
		RandomAccessFile raf = new RandomAccessFile(personsFile, "r");

		FileChannel channel = raf.getChannel();
		ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
		List<PersonEntry> persons = new ArrayList<>();
		long fileSize = personsFile.length();
		long pagesCount = fileSize / PAGE_SIZE;
		for (int offset = 0; offset < fileSize; offset+= PAGE_SIZE) {
//			System.out.println("Čítam " + offset/PAGE_SIZE + "stránku");
			buffer.clear();
			channel.read(buffer, offset);
			buffer.rewind();
			int personsCount = buffer.getInt();
			for (int i = 0; i < personsCount; i++) {
				PersonEntry entry = new PersonEntry();
				entry.load(buffer);
				persons.add(entry);
			}
		}
		channel.close();
		raf.close();
		Collections.sort(persons);
		
		tree.openAndBatchUpdate(persons.iterator(), persons.size());
		return tree;
	}

}
