package chat.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// The same used in project Cliente
public class ChatMessage implements Serializable {
	private static final long serialVersionUID = -3029960919914733235L;

	private String name;
	private String text;
	private String nameReserved; // User name that will receive the private/group message
	private Set<String> setOnlines; // List with all users on the server
	private Action action; // Represent the action of each message, based on a Enum
	
	public ChatMessage() {
		this.setOnlines = new HashSet<String>();
	}
	
	public ChatMessage(String name, Action action){
		this();
		
		this.name = name;
		this.action = action;
	}
	
	public ChatMessage(String name, String text){
		this();
		
		this.name = name;
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getNameReserved() {
		return nameReserved;
	}

	public void setNameReserved(String nameReserved) {
		this.nameReserved = nameReserved;
	}

	public Set<String> getSetOnlines() {
		return setOnlines;
	}

	public void setSetOnlines(Set<String> setOnlines) {
		this.setOnlines = setOnlines;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public enum Action {
		CONNECT, DISCONNECT, SEND_ONE, SEND_ALL, USERS_ONLINE
	}

}
