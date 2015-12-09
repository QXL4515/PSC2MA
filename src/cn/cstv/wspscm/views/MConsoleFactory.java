package cn.cstv.wspscm.views;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

public class MConsoleFactory {

	public static final MConsole CONSOLE_MESSAGE = new AbstractMConsole(
			new MessageConsole("message", null));
	public static final MConsole CONSOLE_SYSTEM = new AbstractMConsole(
			new MessageConsole("console", null));
	public static final MConsole DEBUG_SYSTEM = new AbstractMConsole(
			new MessageConsole("console", null));

	static {
		{

			IConsoleManager manager = ConsolePlugin.getDefault()
					.getConsoleManager();
			// IConsole[] existing = manager.getConsoles();
			manager.addConsoles(new IConsole[] {
					CONSOLE_SYSTEM.getMessageConsole(),
					CONSOLE_MESSAGE.getMessageConsole(),
					DEBUG_SYSTEM.getMessageConsole()});
		}
	}

	private MConsoleFactory() {
	}
}
