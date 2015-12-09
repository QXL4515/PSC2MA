package cn.cstv.wspscm.monitor;

public class TimedAutomata {
	
	private String timedCondition; // 消息时间条件，比如“x<1,y:=0”
	
	private String startStatus; //开始状态

	private String endStatus;   //结束状态
	
	private boolean yesORno;  //消息是，或消息非
	
	private String automataMessage; //消息部分文本，有empty的情况存在
	
	private int x = -1;            //消息条件中的x值得范围
	
	private int xSymbol = -1;      //0表示<, 1表示=, 2表示>, 3表示:=
	
	private int y = -1;            //消息条件中的y值得范围
	
	private int ySymbol = -1;       //0表示<, 1表示=, 2表示>, 3表示:=
	
	private int messageType=0;		//0表示内部消息，1表示发送（！），2表示接收（？）				tjf 20110915


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getXSymbol() {
		return xSymbol;
	}

	public void setXSymbol(int symbol) {
		xSymbol = symbol;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getYSymbol() {
		return ySymbol;
	}

	public void setYSymbol(int symbol) {
		ySymbol = symbol;
	}

	public TimedAutomata(){}
	
	public TimedAutomata(String startStatus,boolean yesORno,String automataMessage,String timedCondition,String endStatus){
		this.startStatus = startStatus;
		this.yesORno = yesORno;
		//System.out.println("TimedAutomata YESORNOLIST"+ isYesORno()+yesORno);
		this.automataMessage = automataMessage;
		this.timedCondition = timedCondition;
		this.endStatus = endStatus;
	}
	
	public TimedAutomata(String startStatus,boolean yesORno,String automataMessage,String timedCondition,String endStatus,int messageType){//tjf 20110921
		this(startStatus,yesORno,automataMessage,timedCondition,endStatus);
		this.messageType=messageType;
	}
	
	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getStartStatus() {
		return startStatus;
	}

	public void setStartStatus(String startStatus) {
		this.startStatus = startStatus;
	}

	public String getEndStatus() {
		return endStatus;
	}

	public void setEndStatus(String endStatus) {
		this.endStatus = endStatus;
	}

	public boolean isYesORno() {
		return yesORno;
	}

	public void setYesORno(boolean yesORno) {
		this.yesORno = yesORno;
	}

	public String getAutomataMessage() {
		return automataMessage;
	}

	public void setAutomataMessage(String automataMessage) {
		this.automataMessage = automataMessage;
	}

	public String getTimedCondition() {
		return timedCondition;
	}

	public void setTimedCondition(String timedCondition) {
		this.timedCondition = timedCondition;
	}

	
}
