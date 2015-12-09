package cn.cstv.wspscm.monitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
	
	private String messageOrigin;

	/**
	 * @return the messageOrigin
	 */
	public String getMessageOrigin() {
		return messageOrigin;
	}

	/**
	 * @param messageOrigin the messageOrigin to set
	 */
	public void setMessageOrigin(String messageOrigin) {
		this.messageOrigin = messageOrigin;
	}
	
	//////////////////////////////////////////////////////////////////
	//新的消息格式：
	//2010-10-07 14:09:21.687 [Gps Call]org.equinoxosgi.toast.internal.dev.gps.sim.GpsCompassSensor.getHeading(255):90
	//
	//
	/////////////////////////////////////////////////////////////////

	private String messageType; // 消息类型，比如invoke, on message, receive etc.

	private String messageFunction; // 消息函数名称

	private String parameter; // 信息函数参数
	
	private String returnValue;//返回值参数

	private String messageFullText; // 消息全格式，比如“[on
									// Message]detectVitalParameters(vitalParameters)”

	private boolean messageStatus; // 消息状态，true表示“Entering”, false表示“Exiting”

	private String timedCondition; // 消息时间条件，比如“2008-04-17 13:00 ”
	
//	private int x = -1;            //消息条件中的x值得范围
//	
//	private int xSymbol = -1;      //0表示<, 1表示=, 2表示>, 3表示:=
//	
//	private int y = -1;            //消息条件中的y值得范围
//	
//	private int ySymbol = -1;       //0表示<, 1表示=, 2表示>, 3表示:=
	
	private double time = -1.0;            //消息发生的时刻


	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}
	
	public void setTime(String firstTime) {
		//2008-04-17 13:00 
		//2008-04-19 07:00
		if(firstTime.contains("-")){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				Date firstdate = formatter.parse(firstTime);
			   Date mydate = formatter.parse(timedCondition); 
			   setTime((mydate.getTime() - firstdate.getTime()) / (1000));	//以秒为单位 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else{
			Double first = Double.parseDouble(firstTime);
			setTime(Double.parseDouble(timedCondition)-first);
		}

	}

	public String getMessageFullText() {
		return messageFullText;
	}

	public void setMessageFullText(String messageFullText) {
		this.messageFullText = messageFullText;
	}

	public void setMessageFullText() {
		messageFullText = messageFunction+ "(" + parameter + ")";
//		if( returnValue.length() == 0)
//		{
//			this.messageFullText = "[" + messageType + "]" + messageFunction + "(" + parameter + ")" ;			
//		}
//		else
//		{
//			this.messageFullText = "[" + messageType + "]" + messageFunction+ "(" + parameter + "):"+returnValue;		
//		}
	}

	public Message() {

	}

	public Message(String timedCondition,String type, String messageText, String parameter, String returnValue) {
		this.messageStatus = true;
		this.timedCondition = timedCondition;
		this.messageType = type;
		this.messageFunction = messageText;
		this.parameter = parameter;
		this.returnValue = returnValue;
		this.messageFullText = messageText+ "(" + parameter + ")";
//		if( returnValue.length() == 0)
//		{
//			this.messageFullText = "[" + type + "]" + messageText+ "(" + parameter + ")";			
//		}
//		else
//		{
//			this.messageFullText = "[" + type + "]" + messageText+ "(" + parameter + "):"+returnValue;		
//		}
//		setTime(timedCondition);
//		System.out.println(getTime());
	}

	public String getType() {
		return messageType;
	}

	public void setType(String type) {
		this.messageType = type;
	}

	public String getMessageText() {
		return messageFunction;
	}

	public void setMessageText(String messageText) {
		this.messageFunction = messageText;
	}

	public String getMessageFunction() {
		return messageFunction;
	}

	public void setMessageFunction(String messageFunction) {
		this.messageFunction = messageFunction;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public boolean isMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(boolean messageStatus) {
		this.messageStatus = messageStatus;
	}
	
	//根据判定条件的String类型来初始化消息状态
	public void setMessageStatus(String messageStatus) {
		if(messageStatus.equals("Entering")){
			this.messageStatus = true;
		}
		else if(messageStatus.equals("Exiting")){
			this.messageStatus = false;
		}
		else{
			System.out.println("Wrong messae status");
		}
		
	}

	public String getTimedCondition() {
		return timedCondition;
	}

	public void setTimedCondition(String timedCondition) {
		this.timedCondition = timedCondition;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getReturnValue() {
		return returnValue;
	}

//	public int getX() {
//		return x;
//	}
//
//	public void setX(int x) {
//		this.x = x;
//	}
//
//	public int getXSymbol() {
//		return xSymbol;
//	}
//
//	public void setXSymbol(int symbol) {
//		xSymbol = symbol;
//	}
//
//	public int getY() {
//		return y;
//	}
//
//	public void setY(int y) {
//		this.y = y;
//	}
//
//	public int getYSymbol() {
//		return ySymbol;
//	}
//
//	public void setYSymbol(int symbol) {
//		ySymbol = symbol;
//	}

}
