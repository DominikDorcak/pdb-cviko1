package sk.upjs.gursky.pdb;

import java.nio.ByteBuffer;

import sk.upjs.gursky.bplustree.BPObject;

public class SurnameAndOffsetEntry implements BPObject<PersonStringKey, SurnameAndOffsetEntry> {

	
	private static final long serialVersionUID = -2971543508380152066L;
	String surname; // char(10) 20B
	long offset; // 8B
	
	public SurnameAndOffsetEntry() {
		
	}
	

	public String getSurname() {
		return surname;
	}


	public long getOffset() {
		return offset;
	}


	public SurnameAndOffsetEntry(String surname, long offset) {
		super();
		this.surname = surname;
		this.offset = offset;
	}


	@Override
	public int compareTo(SurnameAndOffsetEntry arg0) {
		return this.surname.compareTo(arg0.surname);
	}

	@Override
	public void load(ByteBuffer bb) {
		char[] data = new char[10];

		for (int i = 0; i < 10; i++) {
			data[i] = bb.getChar();
		}
		surname = new String(data);
		offset = bb.getLong();
	
	}

	@Override
	public void save(ByteBuffer bb) {
		for (int k = 0; k < 10; k++) {
			bb.putChar(surname.charAt(k));
		}
		bb.putLong(offset);
	}

	@Override
	public String toString() {
		return "SurnameAndOffsetEntry [surname=" + surname + ", offset=" + offset + "]";
	}


	@Override
	public int getSize() {
		return 28;
	}

	@Override
	public PersonStringKey getKey() {
		// TODO Auto-generated method stub
		return new PersonStringKey(surname);
	}

}
