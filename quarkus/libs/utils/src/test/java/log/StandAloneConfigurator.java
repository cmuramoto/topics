package log;

import java.util.logging.Handler;
import java.util.logging.Level;

import org.jboss.logmanager.formatters.PatternFormatter;
import org.jboss.logmanager.handlers.ConsoleHandler;

import lombok.Getter;

public class StandAloneConfigurator implements org.jboss.logmanager.EmbeddedConfigurator {

	public static ConsoleHandler createDefaultHandler() {
		var handler = new ConsoleHandler(new PatternFormatter("%d{yyyy-MM-dd'T'HH:mm:ss.SSS} %-5p [%c{3.}] %s%e%n"));
		handler.setLevel(Level.ALL);
		return handler;
	}

	@Getter(lazy = true)
	private final Handler[] handlers = new Handler[]{ createDefaultHandler() };

	@Override
	public Handler[] getHandlersOf(String loggerName) {
		return getHandlers();
	}

}
