package horbacz.maciej.wirelessconnection;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.List;

public class Server extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    FtpServer server;
    @Override
    public void onCreate() {

        Toast.makeText(getApplicationContext(), "Starting server!",
                Toast.LENGTH_LONG).show();

        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();
        // set the port of the listener
        factory.setPort(2221);
        // replace the default listener
        serverFactory.addListener("default", factory.createListener());
        // create configuring profie for server
        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
        // allow logging annoymously
        connectionConfigFactory.setAnonymousLoginEnabled(true);
        // applying configuration
        serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());
        // creatale user
        BaseUser user = new BaseUser();
        user.setName("anonymous");
        // create list of objects with specific permissions
        List<Authority> authorities = new ArrayList<>();
        // set permission to write data
        authorities.add(new WritePermission());
        // set permissions to user
        user.setAuthorities(authorities);

        // save user
        try {
            serverFactory.getUserManager().save(user);
        } catch (FtpException e) {
            e.printStackTrace();
        }


        server = serverFactory.createServer();

        try {
            server.start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy(){

        Toast.makeText(getApplicationContext(), "Stopping server!",
                Toast.LENGTH_LONG).show();

        server.stop();
    }
}