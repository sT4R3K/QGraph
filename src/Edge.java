import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Edge {
	private String name;
	private AbstractVertex target;
	private AbstractVertex source;
	
	/*
	 * Constructors:
	 */
	Edge () {
		
	}
	
	Edge (String name, AbstractVertex target, AbstractVertex source) {
		this.name = name;
		this.target = target;
		this.source = source;
	}
	
	/*
	 * Accessors:
	 */
	
	public String getName () {
		return name;
	}
	
	public AbstractVertex getTarget () {
		return target;
	}
	
	public AbstractVertex getSource () {
		return source;
	}
	
	/*
	 * Mutators:
	 */
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void setTarget (AbstractVertex target) {
		this.target = target;
	}
	
	public void setSource (AbstractVertex source) {
		this.source = source;
	}
	
	/*
	 * Methods:
	 */
	
	@SuppressWarnings("unchecked")
	public JSONObject makeJSONObject () {
		JSONObject obj = new JSONObject ();
		
		obj.put("src", source.getName());
		obj.put("tgt", target.getName());
		
		return obj;
	}
	
}