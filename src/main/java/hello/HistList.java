package hello;

import java.util.List;

public class HistList {
	public List<HistoryDetailItem> items;

	public HistList() {
		super();
	}

	public HistList(List<HistoryDetailItem> items) {
		this();
		this.items = items;
	}

	@Override
	public boolean equals(Object l) {
		List<HistoryDetailItem> l1;
		if (l instanceof HistList) {
			l1 = ((HistList) l).items;
			return (l1 == null && items == null) || (l1 != null && l1.equals(items));
		}
		return false;
	}
}
