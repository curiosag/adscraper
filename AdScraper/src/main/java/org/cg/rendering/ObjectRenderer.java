package org.cg.rendering;

import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;


public class ObjectRenderer {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
	private static final String trOpen = "<tr>";
	private static final String trClose = "</tr>\n";
	private static final String tableOpen = "<table>\n";
	private static final String tableClose = "</table>\n";
	private static final String td = "<td>%s</td>";
	private static final String th = "<th>%s</th>";
	
	private void renderTableRow(StringBuilder sb, Object o) throws IllegalArgumentException, IllegalAccessException {
		sb.append(trOpen);
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			String value = f.getType().getSimpleName();
			if (f.getType().equals(String.class))
				value = (String) f.get(o);
			else if (f.getType().equals(Integer.TYPE))
				value = String.valueOf(f.getInt(o));
			else if (f.getType().equals(Date.class))
				value = dateFormat.format((Date) f.get(o));
			sb.append(String.format(td, value));

		}
		sb.append(trClose);
	}

	private void renderTableHeader(StringBuilder sb, Object o) throws IllegalArgumentException, IllegalAccessException {
		sb.append(trOpen);
		for (Field f : o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			sb.append(String.format(th, f.getName()));
		}
		sb.append(trClose);
	}
	
	public String renderTable(@SuppressWarnings("rawtypes") List l) {
		StringBuilder sb = new StringBuilder();
		boolean headerAdded = false;
		sb.append(tableOpen);
		try {
			for (Object o : l){
				if (! headerAdded){
					renderTableHeader(sb, o);
					headerAdded = true;
				}
				renderTableRow(sb, o);
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		sb.append(tableClose);
		return sb.toString();
	}

}
