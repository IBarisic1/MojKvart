package beans;

public class CouncilStatement {
	private int id;
	private String councillor;
	private String statement;

	public CouncilStatement(int id, String councillor, String statement) {
		this.id = id;
		this.councillor = councillor;
		this.statement = statement;
	}

	public CouncilStatement() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCouncillor() {
		return councillor;
	}

	public void setCouncillor(String councillor) {
		this.councillor = councillor;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
}
