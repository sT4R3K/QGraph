
public class Edge {
	private String name;
	private String target;
	private String source;
	
	/*
	 * Constructors:
	 */
	
	public Edge (String name, String target, String source) {
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
	
	public String getTarget () {
		return target;
	}
	
	public String getSource () {
		return source;
	}
	
	/*
	 * Mutators:
	 */
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void setTarget (String target) {
		this.target = target;
	}
	
	public void setSource (String source) {
		this.source = source;
	}
	
}
