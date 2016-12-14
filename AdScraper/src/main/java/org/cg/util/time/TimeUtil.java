package org.cg.util.time;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

@SuppressWarnings("boxing")
public class TimeUtil {

	private static Map<Integer, Integer> reEnum = new HashMap<Integer, Integer>();
	private static Calendar c = Calendar.getInstance();
	
	static 	{

		reEnum.put(5, 1); // Monday
		reEnum.put(6, 2);
		reEnum.put(7, 3);
		reEnum.put(1, 4);
		reEnum.put(2, 5);
		reEnum.put(3, 6);
		reEnum.put(4, 7); // Sunday
	}
	
	public static int getDayOfWeek(DateTime d)
	{
		c.set(d.getYear(), d.getMonthOfYear(), d.getDayOfMonth());		
		return reEnum.get(c.get(Calendar.DAY_OF_WEEK));
	}
	
	
}
