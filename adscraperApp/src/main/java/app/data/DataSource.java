package app.data;

import java.util.List;

public class DataSource {

	private String url;
	private List<String> headerParams;
	private DataSource next;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<String> getHeaderParams() {
		return headerParams;
	}
	public void setHeaderParams(List<String> headerParams) {
		this.headerParams = headerParams;
	}
	public DataSource getNext() {
		return next;
	}
	public void setNext(DataSource next) {
		this.next = next;
	}
	
}
