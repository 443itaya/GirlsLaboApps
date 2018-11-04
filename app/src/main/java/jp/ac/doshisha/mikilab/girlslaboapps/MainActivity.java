package jp.ac.doshisha.mikilab.girlslaboapps;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    //中継機のアドレス&ポート番号
    private String address = "0.0.0.0";
    private int port = 55333;

    //RGBの信号値
    private int numR, numG, numB;
    private int tmpR, tmpG, tmpB;

    //グループ番号
    private int groupNumber = 5;

    private static final int REQUESTCODE_TEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");


        //シークバーの定義
        final SeekBar seekBarR = (SeekBar)findViewById(R.id.seekBarR);
        final SeekBar seekBarG = (SeekBar)findViewById(R.id.seekBarG);
        final SeekBar seekBarB = (SeekBar)findViewById(R.id.seekBarB);

        //RGBの信号値の表示
        final TextView paramR = (TextView)findViewById(R.id.paramR);
        final TextView paramG = (TextView)findViewById(R.id.paramG);
        final TextView paramB = (TextView)findViewById(R.id.paramB);

        //ボタンの定義
        Button minusR = (Button)findViewById(R.id.minusR);
        Button minusG = (Button)findViewById(R.id.minusG);
        Button minusB = (Button)findViewById(R.id.minusB);
        Button plusR = (Button)findViewById(R.id.plusR);
        Button plusG = (Button)findViewById(R.id.plusG);
        Button plusB = (Button)findViewById(R.id.plusB);
        Button sendsignal = (Button)findViewById(R.id.sendsignal);

        //RGB初期値
        numR = seekBarR.getProgress();
        numG = seekBarG.getProgress();
        numB = seekBarB.getProgress();

        //初期画面では0と表示
        paramR.setText(numR+"");
        paramG.setText(numG+"");
        paramB.setText(numB+"");

        //赤のシークバーを動かすとき
        seekBarR.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        numR = seekBarR.getProgress();
                        paramR.setText(numR+"");
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // ツマミに触れたときに呼ばれる
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // ツマミを離したときに呼ばれる
                    }
                }
        );

        //緑のシークバーを動かすとき
        seekBarG.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        numG = seekBarG.getProgress();
                        paramG.setText(numG+"");
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // ツマミに触れたときに呼ばれる
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // ツマミを離したときに呼ばれる
                    }
                }
        );

        //青のシークバーを動かすとき
        seekBarB.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        numB = seekBarB.getProgress();
                        paramB.setText(numB+"");
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // ツマミに触れたときに呼ばれる
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // ツマミを離したときに呼ばれる
                    }
                }
        );

        //+-ボタンが押されたとき
        minusR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numR > 0) seekBarR.setProgress(numR - 1);
                numR = seekBarR.getProgress();
                paramR.setText(numR+"");
            }
        });
        minusG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numG > 0) seekBarG.setProgress(numG - 1);
                numG = seekBarG.getProgress();
                paramG.setText(numG+"");
            }
        });
        minusB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numB > 0) seekBarB.setProgress(numB - 1);
                numB = seekBarB.getProgress();
                paramB.setText(numB+"");
            }
        });

        plusR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numR < 1000) seekBarR.setProgress(numR + 1);
                paramR.setText(seekBarR.getProgress()+"");
                numR = seekBarR.getProgress();
            }
        });
        plusG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numG < 1000) seekBarG.setProgress(numG + 1);
                paramG.setText(seekBarG.getProgress()+"");
                numG = seekBarG.getProgress();
            }
        });
        plusB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numB < 1000) seekBarB.setProgress(numB + 1);
                paramB.setText(seekBarB.getProgress()+"");
                numB = seekBarB.getProgress();
            }
        });

        //送信ボタンが押されたとき
        sendsignal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //信号値送信
                sendSignal();
            }
        });

    }

    public void sendSignal() {

        new AsyncTask<Void, Void, String>() {


            @Override
            protected String doInBackground(Void... voids) {
                try {
                    Socket socket = new Socket(address, port);
                    PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);

                    String sendText = (groupNumber + "," + numR + "," + numG + "," + numB);
                    Log.w("sendMessage",sendText);
                    pw.println(sendText);

                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Settingsを押したとき
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.putExtra("address", address);
            intent.putExtra("port", port);
            intent.putExtra("groupNumber", groupNumber);
            startActivityForResult(intent,REQUESTCODE_TEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //SettingActivityから値を持ってくる
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_TEST:
                if (RESULT_OK == resultCode) {
                    address = data.getStringExtra("address");
                    port = data.getIntExtra("port", 0);
                    groupNumber = data.getIntExtra("groupNumber", 0);
                }
                break;
        }
    }
}
