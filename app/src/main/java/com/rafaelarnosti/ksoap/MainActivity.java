package com.rafaelarnosti.ksoap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    String METHOD_NAME="Soma";
    String SOAP_ACTION="";

    String NAMESPACE = "http://rafaelarnosti.com.br/";
    String SOAP_URL = "http://10.3.1.19:8080/Calculadora/Calculadora";

    SoapObject request;
    SoapPrimitive calcular;
    EditText etvalor1;
    EditText etvalor2;
    TextView tvResultado;
    String valor1;
    String valor2;
    ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etvalor1 = (EditText)findViewById(R.id.etvalor1);
        etvalor2 = (EditText)findViewById(R.id.etvalor2);
        tvResultado = (TextView) findViewById(R.id.tvResultado);
    }

    public void somar(View v){
        valor1 =   etvalor1.getText().toString();
        valor2 = etvalor2.getText().toString();
        CalcularAsync calcularAsync = new CalcularAsync();
        calcularAsync.execute(valor1,valor2);
    }

    private class CalcularAsync extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            request = new SoapObject(NAMESPACE,METHOD_NAME);
            request.addProperty("valor1",params[0]);
            request.addProperty("valor2",params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransportSE = new HttpTransportSE(SOAP_URL);
            try{
                httpTransportSE.call(SOAP_ACTION,envelope);
                calcular = (SoapPrimitive)envelope.getResponse();
            }
            catch (Exception ex){
                ex.getMessage();
            }
            return calcular.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            tvResultado.setText(resultado);
            Toast.makeText(getApplicationContext(), "Resultado: " + resultado.toString(), Toast.LENGTH_SHORT).show();
        }

    }

}
