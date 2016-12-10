package hello;

import java.util.ArrayList;
import java.util.List;

public class UtilTest {

	public static List<HistoryDetailItem> createItems(int n) {
		List<HistoryDetailItem> items = new ArrayList<HistoryDetailItem>();
		for (int i = 0; i < n; i++) {

			HistoryDetailItem h = new HistoryDetailItem(i, "String url", "String location", "String prize",
					"String size", String.format("String %d", i), "String description");
			items.add(h);

		}
		return items;
	}

}
