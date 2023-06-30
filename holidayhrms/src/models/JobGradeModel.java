package models;




public class JobGradeModel {
 
    private String jbgrId;
    
    public JobGradeModel(String jbgrId, String jbgrName, String jbgrDescription) {
		this.jbgrId = jbgrId;
		this.jbgrName = jbgrName;
		this.jbgrDescription = jbgrDescription;
	}

	public JobGradeModel() {
	}

    private String jbgrName;
  
    private String jbgrDescription;

	public String getJbgrId() {
		return jbgrId;
	}

	public void setJbgrId(String jbgrId) {
		this.jbgrId = jbgrId;
	}

	public String getJbgrName() {
		return jbgrName;
	}

	public void setJbgrName(String jbgrName) {
		this.jbgrName = jbgrName;
	}

	public String getJbgrDescription() {
		return jbgrDescription;
	}

	public void setJbgrDescription(String jbgrDescription) {
		this.jbgrDescription = jbgrDescription;
	}
    
    
}

