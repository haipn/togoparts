package sg.togoparts.json;

import java.util.ArrayList;

public class SectionResult {
	public Sections Result;

	public class Sections {
		public String Return;
		public ArrayList<Section> Sections;
	}

	public class Section {
		public int id;
		public Data data;

	}

	public class Data {
		public String title;
		public int newitem_cost;
		public int priority_cost;
	}
}
