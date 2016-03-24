import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Boundary implements AbstractVertex {
	
	private String name;
	private boolean boundary; // What's this for ?
	// coordinates:
	private float x;
	private float y;
	
	/*
	 * Constructors:
	 */
	Boundary(String name){
		this.name = name;
		boundary = true;
		x = 0;
		y = 0;
	}
	
	
	Boundary(String name, boolean boundary){
		this.name = name;
		this.boundary = boundary;
		x = 0;
		y = 0;
	}
	
	Boundary(String name, boolean boundary, float x, float y){
		this.name = name;
		this.boundary = boundary;
		this.x = x;
		this.y = y;
	}
	
	
	/*
	 * Accessors:
	 */
	
	public String getName () {
		return name;
	}
	
	public boolean getBoundary () {
		return boundary;
	}
	
	public float getX () {
		return x;
	}

	public float getY () {
		return y;
	}

	public float [] getCoordinates () {
		return new float [] {x,y};
	}
	
	/*
	 * Mutators:
	 */
	
	public void setName (String name) {
		this.name = name;
	}

	public void setBoundary (Boolean boundary) {
		this.boundary = boundary;
	}

	public void setX (float x) {
		this.x = x;
	}

	public void setY (float y) {
		this.y = y;
	}

	public void setCoordinates (float [] coordinates) {
		x = coordinates [0];
		y = coordinates [1];
	}
	
	/*
	 * Methods:
	 */
	
	@SuppressWarnings("unchecked")
	public JSONObject makeJSONObject () {
		JSONObject obj = new JSONObject ();
		
		JSONArray coord = new JSONArray ();
		coord.add (this.x);
		coord.add (this.y);
		
		JSONObject annotation = new JSONObject ();
		annotation.put("coord", coord);
		
		obj.put("boundary", true);
		obj.put ("annotation", annotation);
		
		return obj;
	}
}