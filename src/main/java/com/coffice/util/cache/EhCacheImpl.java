
package com.coffice.util.cache;

import java.net.URL;
import net.sf.ehcache.*;
import net.sf.ehcache.Cache;

// Referenced classes of package com.ashburz.util.cache:
//			ICache

public class EhCacheImpl
	implements ICache
{

	private Cache cache;
	private CacheManager manager;
	private URL url;
	private Element element;

	public EhCacheImpl()
	{
		cache = null;
		manager = null;
		url = null;
		element = null;
	}

	public void init()
	{
		if (manager == null)
		{
			url = getClass().getResource("/ehcache.xml");
			manager = new CacheManager(url);
		}
		cache = manager.getCache("syscache");
	}

	public Object getGlobalInfo(String module, String key)
	{
		if (cache == null)
			return null;
		element = cache.get((new StringBuilder(String.valueOf(module))).append(key).toString());
		if (element == null)
			return null;
		else
			return element.getValue();
	}

	public Object getUserInfo(String yhid, String key)
	{
		if (cache == null)
			return null;
		element = cache.get((new StringBuilder(String.valueOf(yhid))).append(key).toString());
		if (element == null)
			return null;
		else
			return element.getValue();
	}

	public void setGlobalInfo(String module, String key, Object value)
	{
		if (cache == null)
			init();
		cache.put(new Element((new StringBuilder(String.valueOf(module))).append(key).toString(), value));
	}

	public void setUserInfo(String yhid, String key, Object value)
	{
		if (cache == null)
			init();
		cache.put(new Element((new StringBuilder(String.valueOf(yhid))).append(key).toString(), value));
	}

	public void removeInfo(String prefix, String key)
	{
		if (cache == null)
			init();
		cache.remove((new StringBuilder(String.valueOf(prefix))).append(key).toString());
	}
}
