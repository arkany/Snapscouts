package us.davidphillips.georgie;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

// Good references
// http://code.google.com/p/ytd-android/source/browse/trunk/src/com/google/ytd/YouTubeUploader.java?r=2
//http://groups.google.com/group/android-developers/browse_thread/thread/7bc840d5378823d7
//http://linklens.blogspot.com/2009/06/android-multipart-upload.html

// HttpFilePosterObserver interface

public class HttpFilePoster 
{
	String url;
	String fileName;
	File outputFile;
	String fileVar;
	String[][] vars = new String[0][0];
	HttpFilePosterObserver observer;

	int status = 0;
	String serverResponse = "";

	public final static int NOT_STARTED = 0;
	public final static int STARTED = 1;
	public final static int COMPLETED = 2;
	public final static int ERROR = 3;

	public HttpFilePoster(String _url, String _fileName,
			String _fileVar, File _outputFile) {
		url = _url;
		fileName = _fileName;
		fileVar = _fileVar;
		outputFile = _outputFile;
		//putExtra1 = name.value // for the submitted name from EditText
		//putExtra2 = spinner.value // for the spinner
		//putExtra3 = location.value // for the geotag.
		
		
		Log.v("POSTER","Constructed");
	}

	public HttpFilePoster(String _url, String _fileName,
			String _fileVar, File _outputFile, String[][] _vars) {
		this(_url,_fileName,_fileVar,_outputFile);
		this.vars = _vars;
	}
	
	public void setObserver(HttpFilePosterObserver _observer)
	{
		observer = _observer;
	}
	
	public void startUpload() {
		HTTPFilePosterThread poster = new HTTPFilePosterThread();
		Thread posterThread = new Thread(poster);
		posterThread.start();

		Log.v("POSTER","Thread Created and Started");

	}

	public String getServerResponse() {
		return serverResponse;
	}

	public int getStatus() {
		return status;
	}
	
	private void updateStatus(int newStatus)
	{
		status = newStatus;
		if (observer != null) { observer.statusUpdated(status); }
	}
	
	// We can't do updateStatus in the thread so we use a handler to do it.
    private Handler statusUpdateHandler = new Handler() {
        public void handleMessage(Message msg) {
        	updateStatus(msg.what);
        }
    };	

	class HTTPFilePosterThread implements Runnable {
		public void run() {
			
			statusUpdateHandler.sendMessage(Message.obtain(statusUpdateHandler,STARTED));
			//updateStatus(STARTED);
			// Saved the Status in the "what" field of the message

			Log.v("POSTER","Thread Running");
			
			HttpURLConnection c = null;
			InputStream is = null;
			OutputStream os = null;

			try {
				URL mURL = new URL(url); 
				Log.v("POSTER","URL Constructed");
				
				c = (HttpURLConnection) mURL.openConnection();
				Log.v("POSTER", "HTTP URL Connection");
				
				c.setDoOutput(true);
				c.setRequestMethod("POST");
				String boundary = "---------------------------7d43722015402c8";
				String boundaryMessage = "--" + boundary
						+ "\r\nContent-Disposition: form-data; " + "name=\""
						+ fileVar + "\"; filename=\"" + fileName
						+ "\"\r\nContent-Type: " + "video/3gpp\r\n\r\n";
				String endBoundary = "\r\n--" + boundary + "--\r\n";
				c.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + boundary);
				c.setRequestProperty("User-Agent", "Mobvcasting File Poster");
				c.setRequestProperty("Connection", "close");

				Log.v("POSTER","Boundary Message Created");
				
				os = c.getOutputStream();
				Log.v("POSTER","Output Stream Created");
				os.write(boundaryMessage.toString().getBytes());
				Log.v("POSTER","Boundary Message Sent");
				
				Log.v("POSTER", "Opening File");
    			Log.v("POSTER","Total File Length: " + outputFile.length());

        		FileInputStream fis = new FileInputStream(outputFile);
				
        		long totalbytes = 0;
				boolean keepgoing = true;
				int val;
				while (keepgoing)
				{
					if ((val = fis.read()) != -1)
					{
						os.write(val);
        				totalbytes++;
					}
					else
					{
						keepgoing = false;
					}
					
					if (totalbytes % 1000 == 0)
					{
						Log.v("POSTER","Sent Total: " + totalbytes);
					}
				}			
				
            	boundaryMessage = "";

            	for (int i = 0; i < vars.length; i++)
				{
					boundaryMessage += "\r\n--" + boundary + "\r\n";
					boundaryMessage += "Content-Disposition: form-data; name=\"" + vars[i][0] + "\"\r\n\r\n";
					boundaryMessage += vars[i][1];
				}

            	boundaryMessage += endBoundary;
				os.write(boundaryMessage.toString().getBytes());

            	Log.v("POSTER","Writing last boundary message");

            	// c.setRequestProperty("Content-Length",
				// Integer.toString(message.length()));
				// dos.flush();
            	
				// Read HTTP response				
				is = c.getInputStream();
				boolean cont = true;
				int ch;
				while (cont)
				{
					if ((ch = is.read()) != -1)
					{
						serverResponse = serverResponse + (char) ch;
					}
					else
					{
						cont = false;
					}
				}
				is.close();
				os.close();

				c.disconnect();

				serverResponse = serverResponse.substring(0, serverResponse.length() - 1);
				Log.v("POSTER",serverResponse);
				
				final int response = c.getResponseCode();
				if (response == 200) 
				{
					Log.v("POSTER","Response 200");
					statusUpdateHandler.sendMessage(Message.obtain(statusUpdateHandler,COMPLETED));
					//updateStatus(COMPLETED);
				} 
				else 
				{
					serverResponse += "Response Code: " + response;
					Log.v("POSTER","Response not 200:" + response);
					statusUpdateHandler.sendMessage(Message.obtain(statusUpdateHandler,ERROR));
					//updateStatus(ERROR);
				}
				
			} 
			catch (Exception e) 
			{
				Log.v("POSTER",e.toString());

				serverResponse = e.getMessage();
				statusUpdateHandler.sendMessage(Message.obtain(statusUpdateHandler,ERROR));
				//updateStatus(ERROR);
			}
		}
	}
}
