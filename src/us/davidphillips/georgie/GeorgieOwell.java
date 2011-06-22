package us.davidphillips.georgie;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class GeorgieOwell extends Activity implements OnClickListener{
	
	Button AboutButton;
	Button TakePicButton;
	//Button GalleryButton;
	Button AboutGameButton;
	ImageView bg;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    bg = (ImageView) this.findViewById(R.id.ImageView01);
        
    AboutButton = (Button) this.findViewById(R.id.Button01);
    AboutButton.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//AboutButton.setText("Let's go!");
			Intent i = new Intent(GeorgieOwell.this, About.class);
			startActivity(i);
		}
    	
    	
    });
    
    TakePicButton = (Button) this.findViewById(R.id.Button02);
    TakePicButton.setOnClickListener(new OnClickListener(){
        public void onClick(View v)
        {
        	Intent i = new Intent(GeorgieOwell.this, GalleryView.class);
			startActivity(i);
		}
    });
    
    AboutGameButton = (Button) this.findViewById(R.id.Button03);
    AboutGameButton.setOnClickListener(new OnClickListener(){
    	public void onClick(View v){
    		//AboutGameButton.setText("About the game");
    		Intent i = new Intent(GeorgieOwell.this, AboutGame.class);
    		startActivity(i);
    	}
    });
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
