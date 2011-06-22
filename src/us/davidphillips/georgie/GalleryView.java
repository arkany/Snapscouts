package us.davidphillips.georgie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Timestamp;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;

public class GalleryView extends Activity implements OnClickListener, Camera.PictureCallback {

	
	CameraView cameraView;
	//ImageView imv;
	//HttpFilePoster poster;
	//Timestamp Timestamp1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
        
        setContentView(R.layout.gallery);
               
        Button pictureButton = (Button) this.findViewById(R.id.Button01);
        cameraView = (CameraView) this.findViewById(R.id.CameraView01);

        
        pictureButton.setOnClickListener(this);
        
    }
    
    // From the OnClickListener
    public void onClick(View v)
    {
    	cameraView.takePicture(null, null, this);
    }
    
    // From the Camera.PictureCallback
    public void onPictureTaken(byte[] data, Camera camera)
    {
		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		//imv.setImageBitmap(bmp);    	
		
        Calendar c = Calendar.getInstance(); // Current Date/Time
        //String imageFileName = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) and so on...
		
		String filename = "/" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.HOUR) + "suspect.jpg";
		//SHAWN'S CODE
		File pictureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename);
        try 
        {
			FileOutputStream pfos = new FileOutputStream(pictureFile);
        	bmp.compress(CompressFormat.JPEG, 75, pfos);

        	pfos.flush();
        	pfos.close();
		} 
        catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
        catch (IOException e) 
        {	
			e.printStackTrace();
		}
        
        // Create Intent Go to Next Screen
        Intent mIntent = new Intent(this,AddInfo.class); 

        // Send through image file name
        Bundle bundle = new Bundle(); 
        bundle.putString("picture_filename", filename); 
        mIntent.putExtras(bundle); 

        // Go to the next screen..
        startActivity(mIntent);
        	
       
        //END SHAWN CODE
                
  ///      poster = new HttpFilePoster("http://itp.nyu.edu/~dp1244/mobme/final/upload.php",filename,"bytes",pictureFile);
  //      poster.setObserver(this); // We implement HttpFilePosterObserver
  //      poster.startUpload();
    }

//    // HttpFilePosterObserver
//	public void statusUpdated(int newStatus) 
//	{
//		if (newStatus == HttpFilePoster.COMPLETED)
//		{
//    		Toast t = Toast.makeText(this,"Upload Complete",Toast.LENGTH_LONG);
//    		t.show();			
//		}
//		else if (newStatus == HttpFilePoster.ERROR)
//		{
//    		Toast t = Toast.makeText(this,"Upload Error " + poster.getServerResponse(),Toast.LENGTH_LONG);
//    		t.show();			
//			
//		}
//		else if (newStatus == HttpFilePoster.STARTED)
//		{
//    		Toast t = Toast.makeText(this,"Upload Started",Toast.LENGTH_LONG);
//    		t.show();			
//				
//		}
	//}
}
