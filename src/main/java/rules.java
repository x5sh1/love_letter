// interface which used for handling commands

import model.player;

public interface rules{
	public void commandHandler(player sender, String message);
}