package us.davidphillips.georgie;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddInfo extends Activity implements OnClickListener, HttpFilePosterObserver {
	
	Button SubmitButton;
	ImageView imv;
	HttpFilePoster poster;
	Spinner spinner;
	EditText nameText;
	//String spinerVal;
	String imageFileName;
	File pictureFile;
	private Intent returnIntent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // Get the data from the other activity 
        // We Put in like this
        	//bundle.putString("picture_filename", filename);
        
        // Get out like this
        Bundle extras = getIntent().getExtras();
        imageFileName = extras.getString("picture_filename"); 
    
        // Turn it into a File
        pictureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + imageFileName);

        setContentView(R.layout.addinfo);
  
        //SHAWNS CODE
        Button submitButton = (Button) this.findViewById(R.id.Button01);
        imv = (ImageView) this.findViewById(R.id.ImageView01);
        
        // Set image
        // We have the file so let's set it from that..
        imv.setImageURI(Uri.fromFile(pictureFile));
        
        nameText = (EditText) this.findViewById(R.id.EditText01);
        nameText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                  // Perform action on key press
                  Toast.makeText(AddInfo.this, nameText.getText(), Toast.LENGTH_SHORT).show();
                  nameText.setInputType(0);
                  return true;
                  
                }
                return false;
            }
        });
        
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.crimes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        submitButton.setOnClickListener(this);
        
    }
 
    // From the OnClickListener
    public void onClick(View v)
    {

    	// Get value from text input and Do the upload
    	//spinerVal = (String) spinner.getSelectedItem();
    	String[][] httpvars = {{"suspect",nameText.getText().toString()},{"spinner",spinner.getSelectedItem().toString()}};
        Toast t = Toast.makeText(this,nameText.getText().toString(),Toast.LENGTH_LONG);
		t.show();			
		
        poster = new HttpFilePoster("http://itp.nyu.edu/~dp1244/mobme/final/uploads/",imageFileName,"bytes",pictureFile,httpvars);
        poster.setObserver(this); // We implement HttpFilePosterObserver
        poster.startUpload();
    
    }
    
    // HttpFilePosterObserver
	public void statusUpdated(int newStatus) 
	{
		if (newStatus == HttpFilePoster.COMPLETED)
		{
    		Toast t = Toast.makeText(this,"Upload Complete",Toast.LENGTH_LONG);
    		t.show();		
    		
    		returnIntent = new Intent(AddInfo.this, GeorgieOwell.class);
			startActivity(returnIntent);
    		
		}
		else if (newStatus == HttpFilePoster.ERROR)
		{
    		Toast t = Toast.makeText(this,"Upload Error " + poster.getServerResponse(),Toast.LENGTH_LONG);
    		t.show();			
			
		}
		else if (newStatus == HttpFilePoster.STARTED)
		{
    		Toast t = Toast.makeText(this,"Upload Started",Toast.LENGTH_LONG);
    		t.show();			
				
		}
	}
}

//@Override
//public void onClick(View v) {
//	// TODO Auto-generated method stub
//	String spinerVal = (String) spinner.getSelectedItem();
//	String[][] httpvars = {{"spinner",spinerVal},{"title", EditText01.getEditableText().toString()}};
////here it needs to add all of the information along with the image.
//	poster = new HttpFilePoster("http://itp.nyu.edu/mobme/final/upload.php",filename,"bytes",pictureFile, httpvars);
//	poster.setObserver(this); // We implement HttpFilePosterObserver
//	poster.startUpload();
//}}



	
