package guy4444.clickaccuracychecker;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_Result extends AppCompatActivity {

    TextView text;
    ImageView image;
    FrameLayout view;
    View root;

    ArrayList<int[]> vals = new ArrayList<>();
    int viewHeight = 0;
    int viewWidth = 0;

    float centerX = 300.0f;
    float centerY = 300.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        root = findViewById(android.R.id.content);

        text = (TextView) findViewById(R.id.text);
        view = (FrameLayout) findViewById(R.id.view);
        image = (ImageView) findViewById(R.id.image);

        root.setOnTouchListener(touchListener);
        root.setOnClickListener(clickListener);

        initViewHeightAndWeight();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show2();
            }
        });
    }

    private void show1() {
        int[][] valsArr = new int[2][vals.size()];
        for (int i = 0; i < vals.size(); i++) {
            valsArr[0][i] = vals.get(i)[0];
            valsArr[1][i] = vals.get(i)[1];
        }
        TugSway tugSway = new TugSway();
        tugSway.setRange(0, viewWidth, 0, viewHeight);
        tugSway.setIsConstantScale(true);
        //Bitmap swayBitmap = tugSway.getSwayBitmap(60, DemoData.getData()[0], DemoData.getData()[1], false);

        Bitmap swayBitmap = tugSway.getSwayBitmap(80, valsArr[0], valsArr[1]);
        image.setImageBitmap(swayBitmap);
    }

    private void show2() {
        int[][] valsArr = new int[2][vals.size()];
        for (int i = 0; i < vals.size(); i++) {
            valsArr[0][i] = vals.get(i)[0];
            valsArr[1][i] = vals.get(i)[1];
        }
        TugSway tugSway = new TugSway();
        tugSway.setRange(-viewWidth/2, viewWidth/2, -viewHeight/2, viewHeight/2);
        tugSway.setIsConstantScale(true);
        //Bitmap swayBitmap = tugSway.getSwayBitmap(60, DemoData.getData()[0], DemoData.getData()[1], false);

        Bitmap swayBitmap = tugSway.getSwayBitmap(80, valsArr[0], valsArr[1]);
        image.setImageBitmap(swayBitmap);
    }

    private void initViewHeightAndWeight() {
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                viewHeight = view.getMeasuredHeight();
                viewWidth  = view.getMeasuredWidth();

                Log.d("pttt", "View height=" + view.getHeight());
                Log.d("pttt", "View Width =" + view.getWidth() );

                Log.d("pttt", "View height=" + viewHeight );
                Log.d("pttt", "View Width =" + viewWidth);
            }
        });
    }

    private float[] lastTouchDownXY = new float[2];

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // save the X,Y coordinates
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                lastTouchDownXY[0] = event.getX();
                lastTouchDownXY[1] = event.getY();
            }
            else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                float x = event.getX();
                float y = viewHeight - event.getY();

                vals.add(new int[]{(int) (x-centerX), (int) (y-centerY)});
                Log.i("ptttM", "onTouch: " + event.getActionMasked() + "  -  " + event.getX() + "," + event.getY());
            }

            Log.i("ptttT", "onTouch: " + event.getActionMasked() + "  -  " + event.getX() + "," + event.getY());


            // let the touch event pass on to whoever needs it
            return false;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // retrieve the stored coordinates
            float x = lastTouchDownXY[0];
            float y = viewHeight - lastTouchDownXY[1];

            vals.add(new int[]{(int) (x-centerX), (int) (y-centerY)});
            //vals.add(new int[]{(int) Math.abs((viewWidth/2)-x), (int) Math.abs((viewHeight/2)-y)});
            //vals.add(new int[]{(int) x, (int) y});
            // use the coordinates for whatever
            text.setText((vals.get(vals.size()-1))[0] + ", " + (vals.get(vals.size()-1))[1]);
            //text.setText(x + ", " + y);
            Log.i("pttt", "onClick: x = " + x + ", y = " + y);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
//
//
//-100, 300
//
//
//20, 290
//120, -10
//
//-130, 320
//-30, 20