package sk.upjs.gursky.pdb;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import sk.upjs.gursky.bplustree.BPTree;

public class UnclusteredBPTree extends BPTree<PersonStringKey, SurnameAndOffsetEntry> {

	
	private static final long serialVersionUID = 3525318058009454250L;
	public static final int PAGE_SIZE = 4096;
	private File personsFile;
	
	private UnclusteredBPTree(File indexFile,File personsFile) {
		super(SurnameAndOffsetEntry.class, indexFile);
		this.personsFile = personsFile;
	}
	
	public static UnclusteredBPTree newTreeBulkLoading(File personsFile, File indexFile) throws IOException {
		UnclusteredBPTree tree = new UnclusteredBPTree(indexFile,personsFile);
		tree.setNodeSize(PAGE_SIZE);
		
		RandomAccessFile raf = new RandomAccessFile(personsFile, "r");

		FileChannel channel = raf.getChannel();
		ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
		List<SurnameAndOffsetEntry> persons = new ArrayList<>();
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
				persons.add(new SurnameAndOffsetEntry(entry.surname,offset + 4 + (i*entry.getSize())));
			}
		}
		channel.close();
		raf.close();
		Collections.sort(persons);
		
		tree.openAndBatchUpdate(persons.iterator(), persons.size());
		return tree;
	}
	
	public List<PersonEntry> intervalQueryUnclustered(PersonStringKey low,PersonStringKey high) throws IOException{
		List<SurnameAndOffsetEntry> pairs = super.intervalQuery(low, high);
		RandomAccessFile raf = new RandomAccessFile(personsFile, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
		
		List<PersonEntry> entries = new LinkedList<>();
		for (SurnameAndOffsetEntry pair : pairs) {
			buffer.clear();
			long pageOffset = pair.getOffset()/PAGE_SIZE * PAGE_SIZE;
			int bufferOffset = (int)(pair.getOffset() - pageOffset);
			channel.read(buffer, pageOffset);
			buffer.position(bufferOffset);
			PersonEntry entry = new PersonEntry();
			entry.load(buffer);
			entries.add(entry);
		}
	
		channel.close();
		raf.close();
		return entries;
	}

}
