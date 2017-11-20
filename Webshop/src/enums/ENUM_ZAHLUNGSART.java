package enums;


public enum ENUM_ZAHLUNGSART {

	RECHNUNG("Rechnung");
	
	private final String name;
	
	ENUM_ZAHLUNGSART(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}

}
