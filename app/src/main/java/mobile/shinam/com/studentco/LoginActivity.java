package mobile.shinam.com.studentco;


import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {


    EditText user;
    EditText pass;
    int cpt = 3;
    public String wsUserId;

    private static final int REINIT_ID = Menu.FIRST;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);

        Button button = (Button) findViewById(R.id.Valider);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = user.getText().toString();
                String pss = pass.getText().toString();
                if(cpt < 1) {
                    Toast.makeText(LoginActivity.this, "Gloup", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(wsAuthenticate(usr, pss)){
                        Intent intent = new Intent(LoginActivity.this, fetchCoDisciples.class).putExtra("id", wsUserId);
                        startActivity(intent);
                        //Toast.makeText(LoginActivity.this, "Id : "+wsUserId, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "User : "+usr+" size : "+usr.length()+" pass = "+pss+" size : "+pss.length(), Toast.LENGTH_SHORT).show();
                        cpt--;
                    }
                }
            }
        });
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().build());
    }

    private boolean wsAuthenticate(String username, String password){
        Log.w(TAG, "wsAuthenticate : start-end" );
        String id = callPHPWebServiceForAuthenticate(username, password);
        if (id==null) {
            this.wsUserId=null;
            return false;
        }
        else {
            this.wsUserId=id;
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, REINIT_ID, 0, "Reset");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case REINIT_ID:
                Reinit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Reinit(){
        this.cpt = 3;
    }

    /**
     * Interroge le webservice pour authentification du login
     * @param username du login
     * @param password du login
     * @return "id" du login si identifié, null sinon
     */
    private String callPHPWebServiceForAuthenticate(String username, String password) {
        Log.w(TAG, "callPHPWebServiceForAuthenticate : start-end");
//Settings
        String soap_url = "http://10.0.2.2/studentCo/bl/ws.php";//url du serveur SOAP
        String soap_action = "http://10.0.2.2/studentCo/bl/ws.php/wsAuthenticate";//le service interrogé
        String method_name = "wsAuthenticate";//La méthode
        String target_namespace = "studentco.org";//voir wsdl : targetNameSpace ou tns

        String wsResponse=null;
        SoapObject request = new SoapObject(target_namespace, method_name);
//Params :
        PropertyInfo propInfo1 = new PropertyInfo();
        propInfo1.setName("username");
        propInfo1.setType(PropertyInfo.STRING_CLASS);
        propInfo1.setValue(username);
        request.addProperty(propInfo1);
//
        PropertyInfo propInfo2 = new PropertyInfo();
        propInfo2.setName("password");
        propInfo2.setType(PropertyInfo.STRING_CLASS);
        propInfo2.setValue(password);
        request.addProperty(propInfo2);
        Log.w(TAG, request.toString());
//Request-call
        SoapSerializationEnvelope envelope = new
                SoapSerializationEnvelope(SoapEnvelope.VER11);
//envelope.dotNet = true;//si le service est .NET based
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransportSE = new HttpTransportSE(soap_url);
        try {
            httpTransportSE.call(soap_action, envelope);
            Object response = envelope.getResponse();
            wsResponse = response.toString();
                return wsResponse;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}



