package cn.cstv.wspscm.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class AbstractMConsole implements MConsole {
	 private MessageConsole console;
	    
	    private MessageConsoleStream stream= null;
	    
	    public AbstractMConsole(MessageConsole console){
	        this.console = console;
	        this.stream = console.newMessageStream();
	    }
	    
	    /**
	     * 这个MessageConsole应该避免暴露
	     */
	    public MessageConsole getMessageConsole(){
	        return this.console;
	    }
	    /**
	     * 这里的println有很大的发挥空间
	     */
	    public void println(String msg){
	        StringBuffer sb = new StringBuffer();
	        sb.append(new SimpleDateFormat("[HH:mm:ss]").format(new Date()));
	        sb.append(msg);
	        this.stream.println(sb.toString());
	    }
}
