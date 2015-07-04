package njit.myapp.cache;

public class PostDetails {
	private String name;
	private String time;
	private String description;
	private String hugs;
	
	public PostDetails(String name, String time, String description, String hugs) {
		this.name = name;
		this.time = time;
		this.hugs = hugs;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTime() {
		return time;
	}

	public String getDescription() {
		return description;
	}
	
	public String getHugs() {
		return hugs;
	}

}