
public class Boundary {
	
	private String name;
	private boolean boundary;
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
		this.x = 0;
		this.y = 0;
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

	public void setCoordinates () {
		
	}
}
