package database;
import java.io.Serializable;
import java.util.ArrayList;
import entity.Table;

public class TableList implements Serializable{

	private ArrayList<Table> tableList = new ArrayList<Table>();

	public TableList() {

		Table t1 = new Table(1,2);
		Table t2 = new Table(2,2);
		Table t3 = new Table(3,4);
		Table t4 = new Table(4,4);
		Table t5 = new Table(5,6);
		Table t6 = new Table(6,6);
		Table t7 = new Table(7,8);
		Table t8 = new Table(8,8);
		Table t9 = new Table(9,10);
		Table t10 = new Table(10,10);
		tableList.add(t1);
		tableList.add(t2);
		tableList.add(t3);
		tableList.add(t4);
		tableList.add(t5);
		tableList.add(t6);
		tableList.add(t7);
		tableList.add(t8);
		tableList.add(t9);
		tableList.add(t10);

	}

	public ArrayList<Table> getTableList() {
		return this.tableList;
	}
	
}