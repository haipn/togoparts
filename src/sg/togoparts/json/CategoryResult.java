package sg.togoparts.json;

import java.util.ArrayList;

public class CategoryResult {
	public Categories Result;

	public class Categories {
		public ArrayList<Category> Category;
	}

	public class Category {
		public int id;
		public String title;

	}

}
