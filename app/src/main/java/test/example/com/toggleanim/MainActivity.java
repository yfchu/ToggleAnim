package test.example.com.toggleanim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

	private ImageView img_click;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		img_click = (ImageView) findViewById(R.id.img_click);
		img_click.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
				int[] location=new int[2];
				img_click.getLocationOnScreen(location);
				intent.putExtra("x",location[0]);
				intent.putExtra("y",location[1]);
				startActivity(intent);
				overridePendingTransition(0,0);
			}
		});
	}
}
