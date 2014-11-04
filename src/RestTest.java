import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.ui.console.IOConsoleOutputStream;
/**
 * 
 */

/**
 * @author Ch.Shan
 *
 */
public class RestTest {

	private static final String URLS_FILE = "urlsFile";
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	private int fail = 0;
	private int success = 0;
	private static String  SPACE = " ";
	
	public static void main(String[] args) {

		String urlsFile = args[0];
		String[] urlTokens = urlsFile.split("=");
		urlsFile = urlTokens[1];
		if (args[0].equalsIgnoreCase(URLS_FILE)) {
			urlsFile = args[0];
		}

		if ( !isNotNullAndEmpty(urlsFile)) {
			System.out.println("\033[31mInvalid input file");
			return;
		}
		
		RestTest restTest = new RestTest();
		try {
			restTest.readAndProcessTextFile(urlsFile);
		} catch (IOException e) {
			System.out.println("\033[31mSomething went wrong.");
			return;
		}
	}

	public void readAndProcessTextFile(String aFileName)
			throws IOException {
		Path path = Paths.get(aFileName);
		
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			
			String line = null;
			List<RequestBean> urlAttributesList = new ArrayList<RequestBean>();
			
			while ((line = reader.readLine()) != null) {

				if (line.startsWith("#")) {
					continue;
				}
				
				urlAttributesList.add(tokenizeString(line));
			}
			
			testRestUrls(urlAttributesList);
			
		} catch (IOException e) {
			System.out.println("\033[31mCannot Read File");
			return;
		}
	}

	private RequestBean tokenizeString(final String input) {

		String[] stringTokens = input.split("\\|");
		
		if (null != stringTokens && stringTokens.length < 6) {
			System.out.println("\033[31mInvalid parameteres defined in file.");
			return null;
		}
		
		if (null != stringTokens && stringTokens.length == 6) {
			String contentType = stringTokens[0];
			String requestMethod = stringTokens[1];
			String url = stringTokens[2];
			String successCode = stringTokens[3];
			Integer printResponseToggle = Integer.parseInt(stringTokens[4]);
			return new RequestBean(contentType, requestMethod, url, successCode,
					printResponseToggle, null, null);
			
		}else if (null != stringTokens && stringTokens.length > 4 && stringTokens.length == 7) {
			String contentType = stringTokens[0];
			String requestMethod = stringTokens[1];
			String url = stringTokens[2];
			String successCode = stringTokens[3];
			Integer printResponseToggle = Integer.parseInt(stringTokens[4]);
			String optionalStringToSearch = stringTokens[5];
			String jsonPayload = stringTokens[6];
			return new RequestBean(contentType, requestMethod, url, successCode,
					printResponseToggle, optionalStringToSearch, jsonPayload);
		}
		
		return null;
	}

	private void testRestUrls(final List<RequestBean> urls){
		
		if (null != urls && !urls.isEmpty()) {
			
			for (RequestBean requestBean : urls) {
				
				sendRestRequest(requestBean);		
			}
			System.out.println("\033[0m");
			System.out.println("Failed: " + fail + "    " + "Success: " + success);
			System.out.println("Total: " + (fail+success));
		}
	}

	private void sendRestRequest(RequestBean requestBean) {
		
		StringBuilder response = new StringBuilder();
		StringBuilder jsonString = new StringBuilder();
		int httpResponseCode = 0;
		try {

			
			URL url = new URL(requestBean.getUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestBean.getRequestMethod().toUpperCase());
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", requestBean.getContentType());
			conn.setRequestProperty("Content-Type",
					requestBean.getContentType());
			int expectedCode = Integer.parseInt(requestBean
					.getSuccessCode());
			
			if (isNotNullAndEmpty(requestBean.getJsonPayload())) {
				OutputStreamWriter writer = new OutputStreamWriter(
						conn.getOutputStream(), "UTF-8");
				writer.write(requestBean.getJsonPayload());
				writer.close();
				
			}
			
			response.append( requestBean.getRequestMethod() ).append( SPACE ).append( requestBean.getUrl() ).append( SPACE ).append( requestBean.getContentType() ).append( SPACE ).append( requestBean.getSuccessCode() ).append( SPACE ).append( requestBean.getOptionalStringToSearch() ).append( SPACE ).append( requestBean.getJsonPayload() );
			
			httpResponseCode = conn.getResponseCode();
			
			if ( httpResponseCode != expectedCode) {

				fail +=1;
				System.out.print("\033[31m");
				return;
			}
			else{
				success +=1;
				System.out.print("\033[32m");
			}
			requestBean.setResponseCode(httpResponseCode);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			
			String line = null;

			if (isNotNullAndEmpty(requestBean.getOptionalStringToSearch()) || requestBean.getPrintResponse() == 1) {
				while ((line = br.readLine()) != null) {
					jsonString.append(line);
				}
				requestBean.setHttpResponse(jsonString.toString());
			}
			
			br.close();
			conn.disconnect();

		} catch (Exception e) {
			System.out.print("\033[31m");
		
		}finally {
			System.out.println(response.toString());
			if(requestBean.getPrintResponse() == 1){
				System.out.println("Response code: " + httpResponseCode);
				System.out.println("Actual Response: " + jsonString.toString());
				
			}
			
			if(isNotNullAndEmpty( requestBean.getOptionalStringToSearch() )){
				boolean isAvailable = jsonString.toString().contains( requestBean.getOptionalStringToSearch() );
				if(isAvailable){
					System.out.print("\033[32m");
					System.out.println( "Response contains the string : " + requestBean.getOptionalStringToSearch() );
				}else{
					System.out.print("\033[31m");
					System.out.println( "Response doesn't contains the string : " + requestBean.getOptionalStringToSearch() );
				}
			}
			System.out.print("\033[0m");
			System.out.println("##############################################################################################");
			
		}
		
	}
	
	private static boolean isNotNullAndEmpty(final String strToCheck) {

		if (null == strToCheck || "" == strToCheck) {
			return false;
		}

		return true;
	}

}
