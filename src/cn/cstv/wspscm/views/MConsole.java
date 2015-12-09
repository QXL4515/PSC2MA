package cn.cstv.wspscm.views;

import org.eclipse.ui.console.MessageConsole;

public interface MConsole {
	public MessageConsole getMessageConsole();

	public void println(String msg);
}
