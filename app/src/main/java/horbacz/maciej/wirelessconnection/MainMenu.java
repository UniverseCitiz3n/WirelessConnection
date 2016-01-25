package horbacz.maciej.wirelessconnection;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;
import android.text.format.Formatter;

public class MainMenu extends AppCompatActivity {

    Button explorer_btn;
    Button server_btn;
    TextView ip_addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        explorer_btn= (Button) findViewById(R.id.button_explorer);
        server_btn= (Button) findViewById(R.id.button_server);
        ip_addr= (TextView) findViewById(R.id.textIP);
        ip_addr.setText(getLANIP());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    Random r= new Random();
    int red=r.nextInt(255);


    public void explorerClick(View view){
        int red=r.nextInt(255);
        int green=r.nextInt(255);
        int blue=r.nextInt(255);
        explorer_btn.setTextColor(Color.rgb(red, green, blue));
        Intent intent = new Intent(MainMenu.this, Explorer.class);
        startActivity(intent);
    }

    public void serverClick(View view){

        if (server_btn.getText()=="Stop server") {
            Intent intent = new Intent(MainMenu.this, Server.class);
            stopService(intent);
            server_btn.setTextColor(Color.parseColor("black"));
            server_btn.setText("Start server");
        }
        else{
            Intent intent = new Intent(MainMenu.this, Server.class);
            startService(intent);
            server_btn.setTextColor(Color.parseColor("red"));
            server_btn.setText("Stop server");
        }
    }

    public String getLANIP() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo=  wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddr= Formatter.formatIpAddress(ip);

        return ipAddr;
    }
}