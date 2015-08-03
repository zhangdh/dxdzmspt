package com.coffice.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

// Referenced classes of package com.ashburz.base.log:
//			OpLogConfig

public class OperateLogFilter extends Filter
{

	public OperateLogFilter()
	{
	}

	public FilterReply decide(ILoggingEvent event)
	{
		StackTraceElement callData[] = event.getCallerData();
		String className = callData[0].getClassName();
		String methodName = callData[0].getMethodName();
		String key = (new StringBuilder(String.valueOf(className))).append(":").append(methodName).toString();
		//if (OpLogConfig.isIngore(key))
			return FilterReply.DENY;
		//else
		//	return FilterReply.NEUTRAL;
	}

	public FilterReply decide(Object obj)
	{
		return decide((ILoggingEvent)obj);
	}
}
