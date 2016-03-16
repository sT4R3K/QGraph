
public class Boundary {
	
	private String name;
	private boolean boundary; // What's this for ?
	// coordinates:
	int x;
	int y;
	
	/*
	 * Constructors:
	 */
	public Boundary(String name){
		this.name = name;
		boundary = true;
		x = 0;
		y = 0;
	}
	
	
	public Boundary(String name, boolean boundary){
		this.name = name;
		this.boundary = boundary;
		x = 0;
		y = 0;
	}
	
	public Boundary(String name, boolean boundary, int x, int y){
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

	public void setBoundary (Boolean boundary) {
		this.boundary = boundary;
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
	
	/*
	 * Methods:
	 */
	
	public JSONObject makeJSONObject () {
		// to implement later.

		JSONObject obj = new JSONObject ();
		return obj;
	}
}
