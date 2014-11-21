package com.fahad.ornob.sust.hitthedeal.contants;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import com.fahad.ornob.sust.hitthedeal.SignUpCreatorPage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileUpload {

	private static int serverResponseCode = 0;

	private static String upLoadServerUri = Constants.urlFileUpload;
	private static String imagepath = null;
	static ProgressDialog dialog = null;
		
	public static void uploadFileGetInstance(final Context context,
			final String oldNAme, final String newName) {

		dialog = ProgressDialog.show(context, "", "Uploading file...", true);
		imagepath = fileRename(oldNAme, newName);
		// messageText.setText("uploading started.....");
		new Thread(new Runnable() {
			public void run() {

				uploadFile(context, imagepath);

			}
		}).start();
	}

//	public static void uploadFileGetInstance2(final Context context,
//			final String sourceFileUri) {
//
//		dialog = ProgressDialog.show(context, "", "Uploading file...", true);
//
//		// messageText.setText("uploading started.....");
//		new Thread(new Runnable() {
//			public void run() {
//
//				uploadFile(context, sourceFileUri);
//
//			}
//		}).start();
//	}

	public static int uploadFile(final Context context, String sourceFileUri) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			return 0;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();
				ex.printStackTrace();

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				dialog.dismiss();
				e.printStackTrace();

				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();
			return serverResponseCode;

		} // End else block
	}

	public static String fileRename(String oldName, String newName) {
		File sdcard = Constants.mainFolderpath;
		File from = new File(sdcard, oldName);
		File to = new File(sdcard, newName);
		from.renameTo(to);

		return to.getPath().toString();
	}

	public static File getFileForCaptureCreatorSignUp() {
		// TODO Auto-generated method stub
		final File path = Constants.mainFolderpath;
		if (!path.exists()) {
			path.mkdir();
		}
		String name;

		int n = 100000;
		int rand;
		rand = new Random().nextInt(n);
		name = "Image-" + rand + ".jpg";
		SignUpCreatorPage.imageFileName = name;
		File fileimage = new File(path, name);

		return fileimage;
	}
	
	public static  File copyFile(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	    
	    return dst;
	}
}
