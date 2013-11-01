package de.illonis.edulog;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Provides a formatter for logging output.
 * 
 * @author illonis
 * 
 */
public class LogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();

		long diff = System.currentTimeMillis() - EduLog.getStartTime();
		sb.append("[");
		sb.append(diff);
		sb.append("ms] ");
		sb.append(record.getLevel());
		sb.append(" (");
		Date date = new Date(record.getMillis());
		sb.append(date.toString());
		sb.append(")\n ");
		sb.append(formatMessage(record));
		sb.append("\n  in ");
		sb.append(record.getSourceClassName());
		sb.append(".");

		sb.append(record.getSourceMethodName());
		sb.append("\n");
		if (record.getThrown() != null) {
			Throwable t = record.getThrown();
			sb.append("  ");
			StackTraceElement[] elems = t.getStackTrace();
			sb.append(t.getClass().getName());
			sb.append(": " + t.getLocalizedMessage());
			sb.append("\n");
			int i = 3;
			for (StackTraceElement e : elems) {
				for (int j = 0; j < i; j++)
					sb.append(" ");
				sb.append("in ");
				sb.append(e.getClassName());
				sb.append(".");
				sb.append(e.getMethodName());
				sb.append("(");
				sb.append(e.getFileName());
				sb.append(":");
				sb.append(e.getLineNumber());
				sb.append(")\n");
				i++;
			}
		}

		return sb.toString();
	}
}
