package enums;


public enum ENUM_BESTELLSTATUS {

	NEU("Neu"), IN_BEARBEITUNG("In Bearbeitung"), VERSANDT("Versandt"), ABGESCHLOSSEN("Abgeschlossen");
	
	private final String name;
	
	ENUM_BESTELLSTATUS(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}

}
