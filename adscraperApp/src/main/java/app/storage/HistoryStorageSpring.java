package app.storage;

import java.util.Date;
import java.util.List;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.base.HistoryItem;
import org.cg.base.IHistoryStorage;
import org.cg.common.check.Check;

public class HistoryStorageSpring implements IHistoryStorage {

	HistoryItemRepository repo;
	
	public HistoryStorageSpring(HistoryItemRepository repo){
		this.repo = repo;
	}
	
	public void store(String urlId, ScrapedValues ad) {
		repo.save(new HistoryItem(ad.get(ValueKind.url).valueOrDefault(), new Date(), urlId));
	}

	public List<HistoryItem> get(int count) {
		Check.isTrue(count > 0);
		List<HistoryItem> result = repo.findAll();
		int to = count == 0 || result.size() == 0 ? 0 : Math.min(count - 1, result.size() - 1);
		return result.subList(0, to);
	}

}
