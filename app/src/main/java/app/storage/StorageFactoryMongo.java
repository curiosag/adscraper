package app.storage;

import org.cg.adscraper.factory.IStorageFactory;
import org.gwtTests.base.IKeyTypeValueStorage;
import org.gwtTests.base.IHistoricalDetailStorage;
import org.gwtTests.base.IHistoryStorage;
import org.gwtTests.base.ISettingsStorage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageFactoryMongo implements IStorageFactory, ApplicationContextAware {

	private ApplicationContext context;

	@Override
	public IHistoricalDetailStorage getHistoricalDetailStorage() {
		return new HistoricalDetailStorageMongo(context.getBean(HistoryDetailItemRepository.class));
	}

	@Override
	public IHistoryStorage getHistoryStorage() {
		return new HistoryStorageSpring(context.getBean(HistoryItemRepository.class));
	}

	@Override
	public IKeyTypeValueStorage createKeyTypeValueStorage() {
		return new KeyTypeValueStorageSpring(createKTVRepo());
	}

	@Override
	public ISettingsStorage getSettingsStorage() {
		return new SettingsStorageSpring(createKTVRepo());
	}

	private KeyTypeValueItemRepository createKTVRepo() {
		return context.getBean(KeyTypeValueItemRepository.class);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;

	}

}
