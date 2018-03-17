package Helper;
import java.util.ArrayList;

public class Result {
	private String text;
	private ArrayList<Event> events = new ArrayList<Event>();
	
	public void addEvent(Event event){
		events.add(event);
	}
	public ArrayList<Event> getEvents() {
		return events;
	}
	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String eventsToString(){
		String eventString="";
		for (Event event : events) {
			eventString+=event.getEvent()+"\r\n";
		}
		return eventString;
	}
	
	public String toString(){
		String eventString="";
		for (Event event : events) {
			eventString+=" , "+event.getEvent();
		}
		return text+"\r\n"+eventString;
	}
}
