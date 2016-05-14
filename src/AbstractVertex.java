
public interface AbstractVertex {
	// Just because Vertex and Boundary need to have the same parent.
	public String getName ();
	public void setName (String name);
	public boolean check ();
}
