package sg.togoparts.json;

import java.util.ArrayList;

import sg.togoparts.login.Profile.Value;

public class PostAdOnLoadResult {
	public ResultValue Result;
	public class ResultValue {
		public String Return;
		public int TCreds;
		public ArrayList<Value> merchant;
		public ArrayList<Value> postingpack;
		public ArrayList<Value> quota;
		public int min_newitem_cost;
		public int min_priority_cost;
		public String Message;
	}
}
