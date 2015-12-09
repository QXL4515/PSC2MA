package cn.cstv.wspscm.monitor;
import java.util.ArrayList;
import java.util.List;


public class MessageSet {

	private int id;
	
	private List<MessageWithXY> message = new ArrayList<MessageWithXY>();

	public MessageSet(){
		
	}
	
	public void SetMessageSet(MessageSet ms){
		this.message = ms.message;
	}
	
	public MessageSet(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<MessageWithXY> getMessage() {
		return message;
	}

	public void setMessage(List<MessageWithXY> message) {
		this.message = message;
	}
	
	public void addMessage(MessageWithXY me) {
		if(this.message!=null)this.message.add(me);
		else message.add(me);
	}
}
