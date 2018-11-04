package jp.ac.doshisha.mikilab.girlslaboapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingActivity extends AppCompatActivity {

    //中継機のアドレス&ポート番号
    private String address;
    private int port;

    //グループ番号
    private int groupNumber;

    private EditText editAddress;
    private EditText editPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        port = intent.getIntExtra("port", 0);
        groupNumber = intent.getIntExtra("groupNumber", 0);

        editAddress = findViewById(R.id.editAddress);
        editAddress.setText(address);
        editPort = findViewById(R.id.editPort);
        editPort.setText(port+"");

        RadioGroup group = (RadioGroup)findViewById(R.id.radioGroup);
        group.check(R.id.radioButton + groupNumber);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                groupNumber = checkedId - R.id.radioButton;
                RadioButton radio = (RadioButton)findViewById(checkedId);
            }
        });

    }

    //MainActivityへ値を返す
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            address = editAddress.getText().toString();
            port = Integer.parseInt(editPort.getText().toString());

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("address", address);
            intent.putExtra("port", port);
            intent.putExtra("groupNumber", groupNumber);
            setResult(RESULT_OK, intent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
