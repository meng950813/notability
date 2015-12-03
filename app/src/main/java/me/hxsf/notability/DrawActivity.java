package me.hxsf.notability;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.hxsf.notability.data.Note;
import me.hxsf.notability.draw.BaseLine;
import me.hxsf.notability.draw.Drawer;
import me.hxsf.notability.until.Recorder;
import me.hxsf.notability.until.SaveLoad;

public class DrawActivity extends AppCompatActivity {

    ImageView img;
    Drawer drawer;
    float lastx, lasty;
    boolean isstart;
    BaseLine line;
    long time = 0;
    private boolean isrecording = false;
    Runnable timer = new Runnable() {
        @Override
        public void run() {
            while (isrecording) {
                time += 100;
                Log.v("timer", "run " + time);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
//    LinkedBlockingQueue<BaseLine> bq = new LinkedBlockingQueue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.drawtoolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String msg = "";
                switch (menuItem.getItemId()) {
                    case R.id.nav_audio:
                        msg += "Click audio";
                        if (time == 0) {
                            isrecording = true;
                            new Thread(timer).start();
                            msg += " start";
                            Recorder.startRecording("Notability/C1/N1", "1.arm");
                            toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_menu_mic_full);
                        } else {
                            msg += " stop, total " + time + " ms";
                            time = 0;
                            isrecording = false;
                            Recorder.stopRecording();
                            toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_menu_mic);
                        }
                        break;
                    case R.id.nav_play:

                        break;
                    case R.id.nav_undo:
                        drawer.undo();
                        msg += "Click undo";
                        break;
                    case R.id.nav_redo:
                        drawer.redo();
                        msg += "Click ic_menu_redo";
                        break;
                    case R.id.nav_share:
                        msg += "Click share";
                        break;
                }

                if (msg.equals("")) {
                    msg = "none";
                }
                Toast.makeText(DrawActivity.this, msg, Toast.LENGTH_SHORT).show();
                Log.v("click", msg);
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.color_picker);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        img = (ImageView) findViewById(R.id.draw_space);
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isstart = true;
                        lastx = x;
                        lasty = y;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        line = new BaseLine(isstart, lastx, lasty, x, y);
                        Log.v("bl_Move", line.toString());
                        drawer.draw(line);
                        isstart = false;
                        lastx = x;
                        lasty = y;
                        return true;
                    case MotionEvent.ACTION_UP:
                        line = new BaseLine(isstart, lastx, lasty, x, y);
                        Log.v("bl_End ", line.toString());
                        drawer.draw(line);
                        isstart = false;
                        return true;
                    default:
                        return false;
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String a = getIntent().getStringExtra("title");
        if (a.equals("")) {
            a = "未命名 " + ((new SimpleDateFormat("yyyy-MM-dd hh:mm")).format(new Date()));
        } else {
            //TODO read file/database to get data;
        }

        getSupportActionBar().setTitle(a);
        img.post(new Runnable() {
            @Override
            public void run() {
                drawer = new Drawer(img, Color.BLACK, 1f);
                drawer.onNewNote();
            }
        });

        /*TEST*/

//        BaseLine b = new BaseLine(true, 1, 2, 3, 4);
//        SaveLoad.save("Notability/C1/N2", "1.obj", b);
//        BaseLine b = (BaseLine) SaveLoad.load("Notability/C1/N2/1.obj");
//        Log.v("sss", b.toString());
    }

    /**
     * Called when a touch screen event was not handled by any of the views
     * under it.  This is most useful to process touch events that happen
     * outside of your window bounds, where there is no view to receive it.
     *
     * @ param event The touch screen event being processed.
     * @return Return true if you have consumed the event, false if you haven't.
     * The default implementation always returns false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_draw, menu);
        return true;
    }

    //    TODO collection 对象的名称和tag 名称
    public void save() {
        SaveLoad.save("Notability/C1/" + drawer.getNote().getTitle(), drawer.getNote().getTitle() + ".obj", drawer.getNote());
    }

    public void load(String noteName) {
        drawer.onNewNote((Note) SaveLoad.load("Notability/C1/" + noteName + "/" + noteName + ".obj"));
    }

}