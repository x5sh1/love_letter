package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class card{
	// model.card code
	private String cardCode;
	// model.card name
	private String name;
	//model.card strength
	private int strength;
	//model.card description
	private String description;
	// model.card status, if the model.card can be drawed or not.
	private boolean status;
}