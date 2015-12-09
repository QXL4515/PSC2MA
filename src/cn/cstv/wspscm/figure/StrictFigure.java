package cn.cstv.wspscm.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class StrictFigure extends Figure{
	private String s = new String();
	public String getS() {
		return s;
	}


	public StrictFigure(String s){
		this.setBounds(new Rectangle(0,0,68,28));
		this.s=s;
	}
	
	
	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		Rectangle r = this.getBounds();
		g.setBackgroundColor(ColorConstants.black);
		//g.drawLine(r.x,r.y ,r.x,r.y + 200);
		//g.fillOval(new Rectangle(r.x,r.y,16,16));
		g.drawText(getS(), new Point(r.x,r.y+16));
	}
}
