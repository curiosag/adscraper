package org.cg.analytics;

public abstract class TimeInterval<V, I>{

	public abstract TimeIntervalType getIntervalType();
	
	public abstract  I toInterval(V value);
	
	public abstract I nextInterval(I value);

	public abstract I previousInterval(I value);
	
	public abstract String render(I value);
	
	public abstract boolean isBefore(I a, I b);
	
	public abstract boolean isAfter(I a, I b);
	
}
