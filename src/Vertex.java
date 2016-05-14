import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Vertex implements AbstractVertex{
	private String name;
	private Type type;
	private String value;
	// coordinates:
	private float x;
	private float y;
	
	/*
	 * Constructors:
	 */
	Vertex () { //TODO is this allowed
		
	}
	
	Vertex (String name, Type type) {
		this.name = name;
		this.type = type;
		value = new String ("");
		x = 0;
		y = 0;
	}
	
	Vertex (String name, Type type, String value) {
		this.name = name;
		this.type = type;
		this.value = value;
		x = 0;
		y = 0;
	}
	
	Vertex (String name, Type type, String value, float x, float y) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.x = x;
		this.y = y;
	}
	
	/*
	 * Accessors:
	 */
	
	public String getName () {
		return name;
	}
	
	public Type getType () {
		return type;
	}
	
	public String getValue () {
		return value;
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

	public void setType (Type type) {
		this.type = type;
	}

	public void setValue (String value) {
		this.value = value;
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
		// if name = null ... do (warning; ignore) // ?!!! I no longer know why I wrote this here.
		JSONObject obj = new JSONObject ();
		
		String typeString;

		switch (this.type) {
			case GREEN:
				typeString = new String ("Z");
				break;
			case RED:
				typeString = new String ("X");
				break;
			case HADAMARD:
				typeString = new String ("hadamard");
				break;
			default:
				return null;
		}
		
		JSONObject data = new JSONObject ();
		data.put ("type", typeString);
		data.put ("value", this.value);
		
		JSONArray coord = new JSONArray ();
		coord.add (this.x);
		coord.add (this.y);
		
		JSONObject annotation = new JSONObject ();
		annotation.put("coord", coord);
		
		obj.put ("data", data);
		obj.put ("annotation", annotation);
		
		return obj;
	}
	
	public boolean check () {
		// TODO what a valid a vertex (intern infos)
		return true;
	}
}