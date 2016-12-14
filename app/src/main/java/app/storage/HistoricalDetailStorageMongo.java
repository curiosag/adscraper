package app.storage;

import org.cg.ads.advalues.ScrapedValues;
import org.gwtTests.base.IHistoricalDetailStorage;

public class HistoricalDetailStorageMongo implements IHistoricalDetailStorage {

	private int maxId = -1;

	HistoryDetailItemRepository repo;

	public HistoricalDetailStorageMongo(HistoryDetailItemRepository repo) {
		this.repo = repo;
	}
	
	public void store(ScrapedValues ad) {
		
		repo.save(new HistoryDetailItem(getId(), ad));
	}

	private int getId() {
		if (maxId < 0)
			maxId = getMaxId();
		else
			maxId = maxId + 1;
		return maxId;
	}

	public int getMaxId() {
		return 0;
	}

	public String setMaxId(int i) {
		this.maxId = i;
		return null;
	}

	public void flush() {
	}

}
