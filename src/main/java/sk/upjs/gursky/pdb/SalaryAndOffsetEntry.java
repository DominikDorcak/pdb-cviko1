package sk.upjs.gursky.pdb;

import java.nio.ByteBuffer;

import sk.upjs.gursky.bplustree.BPObject;

public class SalaryAndOffsetEntry implements BPObject<PersonIntKey, SalaryAndOffsetEntry> {

	
	private static final long serialVersionUID = -2971543508380152066L;
	int salary; // 4B
	long offset; // 8B
	
	public SalaryAndOffsetEntry() {
		
	}
	

	public int getSurname() {
		return salary;
	}


	public long getOffset() {
		return offset;
	}


	public SalaryAndOffsetEntry(int salary, long offset) {
		super();
		this.salary = salary;
		this.offset = offset;
	}


	@Override
	public int compareTo(SalaryAndOffsetEntry arg0) {
		return this.salary - arg0.salary;
	}

	@Override
	public void load(ByteBuffer bb) {
		salary = bb.getInt();
		offset = bb.getLong();
	
	}

	@Override
	public void save(ByteBuffer bb) {
		bb.putInt(salary);
		bb.putLong(offset);
	}



	@Override
	public int getSize() {
		return 12;
	}

	@Override
	public PersonIntKey getKey() {
		return new PersonIntKey(salary);
	}

}
