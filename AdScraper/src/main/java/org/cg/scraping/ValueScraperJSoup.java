package org.cg.scraping;

import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;
import org.cg.base.Log;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;

public class ValueScraperJSoup implements ValueScraper {

	private ValueKind id;
	private String selectExpression;
	private Optional<String> attribute;
	private boolean multipleResultElements = false;
	
	private StringNormalizer normalizer = new StringNormalizer() {
		public String normalize(String value) {
			return value;
		}
	};

	private void initBasics(ValueKind id, String selectExpression) {
		this.id = id;
		this.selectExpression = selectExpression;
	}

	/**
	 * extracts the element text
	 */

	public ValueScraperJSoup(ValueKind id, String selectExpression) {
		Check.notNull(id);
		Check.notEmpty(selectExpression);

		initBasics(id, selectExpression);
		this.attribute = Optional.empty();
	}

	public ValueScraperJSoup(ValueKind id, String selectExpression, boolean multipleResultElements) {
		Check.notNull(id);
		Check.notEmpty(selectExpression);

		initBasics(id, selectExpression);
		this.attribute = Optional.empty();
		this.multipleResultElements = multipleResultElements;
	}

	/**
	 * extracts an attribute value
	 */

	public ValueScraperJSoup(ValueKind id, String selectExpression, String attribute) {
		initBasics(id, selectExpression);
		Check.notNull(attribute);
		
		this.attribute = Optional.of(attribute);
	}

	public final ValueKind kind() {
		return id;
	}

	private String applyTo(Element e) {
		Check.notNull(e);

		Elements selectedElements = e.select(selectExpression);
		Check.notNull(selectedElements);

		if (!multipleResultElements && selectedElements.size() > 1)
			 Log.info(ValueScraperJSoup.class.getName() + "1 element expected but returned "
					+ Integer.toString(selectedElements.size()) + " for selector " + selectExpression);

		String value = null;

		if (selectedElements.size() >= 1) {

			if (multipleResultElements)
				value = selectedElements.text();
			else
			{
				Element selectedElement = selectedElements.first();

				if (attribute.isPresent())
					value = selectedElement.attr(attribute.get());
				else
					value = selectedElement.ownText();
			}
			
		}

		return normalizer.normalize(value);
	}

	public final ScrapedValue scrape(Element e) {
		Check.notNull(e);

		return ScrapedValue.create(id, applyTo(e));
	}

	public static ValueScraper create(ValueKind id, String selectExpression, String attribute) {
		return new ValueScraperJSoup(id, selectExpression, attribute);
	}

	public static ValueScraper create(ValueKind id, String selectExpression) {
		return new ValueScraperJSoup(id, selectExpression);
	}

	public static ValueScraper create(ValueKind id, String selectExpression, boolean multipleResultElements) {
		return new ValueScraperJSoup(id, selectExpression, multipleResultElements);
	}

	public ValueScraper setNormalizer(StringNormalizer normalizer) {
		Check.notNull(normalizer);
		this.normalizer = normalizer;
		return this;
	}
}
