package mobile.shinam.com.studentco;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import mobile.com.shinam.entity.Login;

public class fetchCoDisciples extends ListActivity {
    ArrayList<Login> coDisciples = new ArrayList<>();

    private static final String TAG = "fetchCoDisciples";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_co_disciples);
        Intent intent = getIntent();
        String wsUserId = intent.getStringExtra("id");
        wsFetchCoDisciples(wsUserId);

        ArrayAdapter<Login> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, coDisciples);

        this.setListAdapter(adapter);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Login login = (Login) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), login.getPrenom()+" "+login.getNom()+" : "+login.getPerson(),
                        Toast.LENGTH_SHORT).show();
        } };
//ListView a une methode pour lui ajouter un observateur :
        this.getListView().setOnItemClickListener(listener);
    }

    private boolean wsFetchCoDisciples(String uid){
        Log.w(TAG, "wsFetchCoDisciples : start-end" );
        String jsonCoDisciples = callPHPWebServiceForFetchCoDisciples(uid);
        //ArrayList<Login> coDisciples = new ArrayList<Login>();
        try {
            if(jsonCoDisciples!=null)
            {
                //Toast.makeText(fetchCoDisciples.this, jsonCoDisciples, Toast.LENGTH_SHORT).show();
                JSONArray jsonArray = new JSONArray(jsonCoDisciples);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                    Login login = new Login();
                    login.setPerson(jsonObject.getInt("Person"));
                    login.setNom(jsonObject.getString("Nom"));
                    login.setPrenom(jsonObject.getString("Prenom"));
                    coDisciples.add(login);
                }
            }
            else
            {
                Toast.makeText(fetchCoDisciples.this, "Fack", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
//  Auto-generated catch block
                e.printStackTrace();
        }
        if (jsonCoDisciples==null) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Interroge le webservice pour authentification du login
     * @param uid du login
     * @return "wsResponse" array des amis de la personne
     */
    private String callPHPWebServiceForFetchCoDisciples(String uid) {
        Log.w(TAG, "callPHPWebServiceForFetchCoDisciples : start-end");
//Settings
        String soap_url = "http://10.0.2.2/studentCo/bl/ws.php";//url du serveur SOAP
        String soap_action = "http://10.0.2.2/studentCo/bl/ws.php/wsFetchCoDisciples";//le service interrogé
        String method_name = "wsFetchCoDisciples";//La méthode
        String target_namespace = "studentco.org";//voir wsdl : targetNameSpace ou tns

        String wsResponse;
        SoapObject request = new SoapObject(target_namespace, method_name);
//Params :
        PropertyInfo propInfo1 = new PropertyInfo();
        propInfo1.setName("uid");
        propInfo1.setType(PropertyInfo.STRING_CLASS);
        propInfo1.setValue(uid);
        request.addProperty(propInfo1);
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
