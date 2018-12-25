//card object
//store card information

public class card{
	// card code
	private String card_code;
	// card name
	private String name;
	//card strength
	private int strength;
	//card description
	private String desc;
	// card status, if the card can be drawed or not.
	private boolean status;

	// constructor
	public card(String card_code, String name, int strength, String desc){
		this.card_code = card_code;
		this.name = name;
		this.strength = strength;
		this.desc = desc;
		this.status = true;
	}

	// set card status
	public void setStatus(){
		this.status = false;
	}

	// get card status
	public boolean getStatus(){
		return status;
	}

	// set card code
	public void setCode(String card_code){
		this.card_code = card_code;
	}

	// set card name
	public void setName(String name){
		this.name = name;
	}

	// set card strength
	public void setStrength(int strength){
		this.strength = strength;
	}

	// set description
	public void setDesc(String desc){
		this.desc = desc;
	}

	// get card code
	public String getCode(){
		return card_code;
	}

	//get card name
	public String getName(){
		return name;
	}

	// get card strength
	public int getStrength(){
		return strength;
	}

	// get card description
	public String getDesc(){
		return desc;
	}

	// reset card status
	public void reset(){
		status = true;
	}
}