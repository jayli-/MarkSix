package cn.jayli.tools.general.marksix;

public class Record {

	private String no;
	private String date;
	private String[] record = new String[7];

	public Record(String no, String date, String first, String second, String third, String forth, String fifth, String sixth, String seventh) {
		this.no = no;
		this.date = date;
		this.record[0] = first;
		this.record[1] = second;
		this.record[2] = third;
		this.record[3] = forth;
		this.record[4] = fifth;
		this.record[5] = sixth;
		this.record[6] = seventh;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFirst() {
		return this.record[0];
	}

	public void setFirst(String first) {
		this.record[0] = first;
	}

	public String getSecond() {
		return this.record[1];
	}

	public void setSecond(String second) {
		this.record[1] = second;
	}

	public String getThird() {
		return record[2];
	}

	public void setThird(String third) {
		this.record[2] = third;
	}

	public String getForth() {
		return record[3];
	}

	public void setForth(String forth) {
		this.record[3] = forth;
	}

	public String getFifth() {
		return record[4];
	}

	public void setFifth(String fifth) {
		this.record[4] = fifth;
	}

	public String getSixth() {
		return record[5];
	}

	public void setSixth(String sixth) {
		this.record[5] = sixth;
	}

	public String getSeventh() {
		return record[6];
	}

	public void setSeventh(String seventh) {
		this.record[6] = seventh;
	}
	
	@Override
	public String toString() {
		
		StringBuffer bf = new StringBuffer();
		bf.append(" ");
		bf.append(no);
		bf.append("    ");
		bf.append(date.substring(0, 6));
		bf.append("    ");
		bf.append(date.substring(6));
		bf.append("    ");
		bf.append(record[0]);
		bf.append("    ");
		bf.append(record[1]);
		bf.append("    ");
		bf.append(record[2]);
		bf.append("    ");
		bf.append(record[3]);
		bf.append("    ");
		bf.append(record[4]);
		bf.append("    ");
		bf.append(record[5]);
		bf.append("    ");
		bf.append(record[6]);
		bf.append("    ");
		return bf.toString();
	}
	
}
