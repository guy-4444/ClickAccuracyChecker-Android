package guy4444.clickaccuracychecker;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import java.util.List;

public class Activity_Result extends AppCompatActivity {

    TextView text;
    ImageView imageClick;
    ImageView imageTouch;
    FrameLayout view;
    View root;

    ArrayList<int[]> click_vals = new ArrayList<>();
    ArrayList<int[]> touch_vals = new ArrayList<>();
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
        imageClick = (ImageView) findViewById(R.id.imageClick);
        imageTouch = (ImageView) findViewById(R.id.imageTouch);

        view.setOnTouchListener(touchListener);
        view.setOnClickListener(clickListener);

        initViewHeightAndWeight();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show3();
            }
        });

    }



    private void show3() {
        int[][] click_valsArr = new int[2][click_vals.size()];
        for (int i = 0; i < click_vals.size(); i++) {
            click_valsArr[0][i] = click_vals.get(i)[0];
            click_valsArr[1][i] = click_vals.get(i)[1];
        }

        int[][] touch_valsArr = new int[2][touch_vals.size()];
        for (int i = 0; i < touch_vals.size(); i++) {
            touch_valsArr[0][i] = touch_vals.get(i)[0];
            touch_valsArr[1][i] = touch_vals.get(i)[1];
        }


        TugSway click_tugSway = new TugSway();
        TugSway touch_tugSway = new TugSway();

        click_tugSway.setRange(0, viewWidth, 0, viewHeight);
        touch_tugSway.setRange(0, viewWidth, 0, viewHeight);

        click_tugSway.setIsConstantScale(true);
        touch_tugSway.setIsConstantScale(true);

        Bitmap click_swayBitmap = click_tugSway.getSwayBitmap(100, 150, click_valsArr[0], click_valsArr[1]);
        Bitmap touch_swayBitmap = touch_tugSway.getSwayBitmap(100, 150, touch_valsArr[0], touch_valsArr[1]);

        imageClick.setImageBitmap(click_swayBitmap);
        imageTouch.setImageBitmap(touch_swayBitmap);
    }

    private void show1() {
        int[][] valsArr = new int[2][click_vals.size()];
        for (int i = 0; i < click_vals.size(); i++) {
            valsArr[0][i] = click_vals.get(i)[0];
            valsArr[1][i] = click_vals.get(i)[1];
        }
        TugSway tugSway = new TugSway();
        tugSway.setRange(0, viewWidth, 0, viewHeight);
        tugSway.setIsConstantScale(true);
        //Bitmap swayBitmap = tugSway.getSwayBitmap(60, DemoData.getData()[0], DemoData.getData()[1], false);

        Bitmap swayBitmap = tugSway.getSwayBitmap(80, 80, valsArr[0], valsArr[1]);
        imageClick.setImageBitmap(swayBitmap);
    }

    private void show2() {
        int[][] valsArr = new int[2][click_vals.size()];
        for (int i = 0; i < click_vals.size(); i++) {
            valsArr[0][i] = click_vals.get(i)[0];
            valsArr[1][i] = click_vals.get(i)[1];
        }
        TugSway tugSway = new TugSway();
//        tugSway.setRange(-viewWidth/2, viewWidth/2, -viewHeight/2, viewHeight/2); // middle accuracy
        tugSway.setRange(0, viewWidth, 0, viewHeight);
        tugSway.setIsConstantScale(true);
        //Bitmap swayBitmap = tugSway.getSwayBitmap(60, DemoData.getData()[0], DemoData.getData()[1], false);

        Bitmap swayBitmap = tugSway.getSwayBitmap(40, 80, valsArr[0], valsArr[1]);
        imageClick.setImageBitmap(swayBitmap);
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

                //touch_vals.add(new int[]{(int) (x-centerX), (int) (y-centerY)});
                touch_vals.add(new int[]{(int) x, (int) y});
                Log.i("ptttM", "onTouch: " + event.getActionMasked() + "  -  " + event.getX() + "," + event.getY());


            }

            //Log.i("ptttT", "onTouch: " + event.getActionMasked() + "  -  " + event.getX() + "," + event.getY());


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

//            vals.add(new int[]{(int) (x-centerX), (int) (y-centerY)});// middle accuracy
            //vals.add(new int[]{(int) Math.abs((viewWidth/2)-x), (int) Math.abs((viewHeight/2)-y)});
            click_vals.add(new int[]{(int) x, (int) y});
            // use the coordinates for whatever
            text.setText((click_vals.get(click_vals.size()-1))[0] + ", " + (click_vals.get(click_vals.size()-1))[1]);
//            text.setText(x + ", " + y);
//            Log.i("pttt", "onClick: x = " + x + ", y = " + y);
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
            click_vals = new ArrayList<>();
            touch_vals = new ArrayList<>();
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