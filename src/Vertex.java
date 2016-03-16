
public class Vertex {
	private String name;
	private Type type;
	private String value;
	// coordinates:
	int x;
	int y;
	
	/*
	 * Constructors:
	 */
	
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
	
	Vertex (String name, Type type, String value, int x, int y) {
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
	
	public int getX () {
		return x;
	}

	public int getY () {
		return y;
	}

	public int [] getCoordinates () {
		return new int [] {x,y};
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

	public void setX (int x) {
		this.x = x;
	}

	public void setY (int y) {
		this.y = y;
	}

	public void setCoordinates (int [] coordinates) {
		x = coordinates [0];
		y = coordinates [1];
	}
}
