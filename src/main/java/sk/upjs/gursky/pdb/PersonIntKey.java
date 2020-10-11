package sk.upjs.gursky.pdb;

import java.nio.ByteBuffer;

import sk.upjs.gursky.bplustree.BPKey;

public class PersonIntKey implements BPKey<PersonIntKey> {

	private static final long serialVersionUID = 7815952263804389119L;
	private int key;
	
	public PersonIntKey() {}
	
	public PersonIntKey(int key) {
		
		this.key = key;
	}
	
	public int getSize() {
		
		return 4;
	}
	
	public void load(ByteBuffer bb) {
		
		key = bb.getInt();
	}
	
	public void save(ByteBuffer bb) {
		
		bb.putInt(key);
	}
	
	public int compareTo(PersonIntKey personIntKey) {
		
		return key - personIntKey.key;
	}
}
