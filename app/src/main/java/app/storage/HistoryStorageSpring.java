package app.storage;

import java.util.Date;
import java.util.List;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.common.check.Check;
import org.gwtTests.base.HistoryItem;
import org.gwtTests.base.IHistoryStorage;

public class HistoryStorageSpring implements IHistoryStorage {

	HistoryItemRepository repo;

	public HistoryStorageSpring(HistoryItemRepository repo) {
		this.repo = repo;
	}
	
	public void store(String urlId, ScrapedValues ad) {
		repo.save(new HistoryItem(ad.get(ValueKind.url).valueOrDefault(), new Date(), urlId));
	}

	public List<HistoryItem> get(int count) {
		Check.isTrue(count > 0);
		List<HistoryItem> result = repo.findAll();
		return result.subList(0, Math.min(count - 1, result.size() - 1));
	}

}
