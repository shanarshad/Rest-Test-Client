/**
 * 
 */

/**
 * @author Ch.Shan
 *
 */
public class RequestBean {

	private String contentType;
	private String requestMethod;
	private String url;
	private String successCode;
	private String optionalStringToSearch;
	private String jsonPayload;
	private Integer responseCode;
	private String httpResponse;
	private Integer printResponse;
	public RequestBean(String contentType, String requestMethod, String url,
			String successCode, Integer printResponse, String optionalStringToSearch,
			String jsonPayload) {
		super();
		this.contentType = contentType;
		this.requestMethod = requestMethod;
		this.url = url;
		this.successCode = successCode;
		this.optionalStringToSearch = optionalStringToSearch;
		this.jsonPayload = jsonPayload;
		this.printResponse = printResponse;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSuccessCode() {
		return successCode;
	}
	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}
	public String getOptionalStringToSearch() {
		return optionalStringToSearch;
	}
	public void setOptionalStringToSearch(String optionalStringToSearch) {
		this.optionalStringToSearch = optionalStringToSearch;
	}
	public String getJsonPayload() {
		return jsonPayload;
	}
	public void setJsonPayload(String jsonPayload) {
		this.jsonPayload = jsonPayload;
	}
	public Integer getResponseCode() {
		return responseCode;
	}
	public String getHttpResponse() {
		return httpResponse;
	}
	public void setHttpResponse(String httpResponse) {
		this.httpResponse = httpResponse;
	}
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}
	public Integer getPrintResponse() {
		return printResponse;
	}
	public void setPrintResponse(Integer printResponse) {
		this.printResponse = printResponse;
	}
	
	
}
