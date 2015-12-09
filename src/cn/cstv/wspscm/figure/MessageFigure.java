package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.Label;

/**
 * @author hp
 *
 */
public class MessageFigure extends Label {

	public MessageFigure(){
		this.setText("");
	}
	
	public MessageFigure(String s){
		this.setText(s);
	}
}
