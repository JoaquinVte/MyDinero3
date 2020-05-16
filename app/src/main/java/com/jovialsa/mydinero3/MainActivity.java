package com.jovialsa.mydinero3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {

    private EditText mInput;
    private TextView mTextEuro, mTextDolar, mTextLibra;
    private Button mBotonConvertir;
    private ImageView mImagen;
    private Button mBotonPort;
    private MenuItem salir;
    private EditText editTextChDolar;
    private EditText editTextChLibra;
    private Button btnChDolar;
    private Button btnChLibra;
    private String[] archivos;
    private String fileDolar = "dolar.dat";
    private String fileLibra = "libra.dat";

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        archivos = fileList();

        mInput = (EditText) findViewById(R.id.editDinero);
        mTextEuro = (TextView) findViewById(R.id.textEuro);
        mTextDolar = (TextView) findViewById(R.id.textDolar);
        mTextLibra = (TextView) findViewById(R.id.textLibra);
        mBotonConvertir = (Button) findViewById(R.id.buttonConvertir);
        mImagen = (ImageView) findViewById(R.id.imagen);
        mBotonPort = (Button) findViewById(R.id.btnPort);
        salir = (MenuItem) findViewById(R.id.accion_salir);
        editTextChDolar = (EditText) findViewById(R.id.editTextChDolar);
        editTextChLibra = (EditText) findViewById(R.id.editTextChLibra);
        btnChDolar = (Button) findViewById(R.id.btnChDolar);
        btnChLibra = (Button) findViewById(R.id.btnChLibra);

        if (existe_archivo(archivos, fileDolar)) {
            editTextChDolar.setText(cargarFichero(fileDolar));
        }
        if (existe_archivo(archivos, fileLibra)) {
            editTextChLibra.setText(cargarFichero(fileLibra));
        }
        

        mBotonConvertir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (mInput.getText().length() == 0) {
                    mostrarSnack(arg0, "ERROR : Introduzca pts");
                } else if (editTextChDolar.getText().length() == 0) {
                    mostrarSnack(arg0, "ERROR Dolar: Introduzca el tipo de cambio");
                }else if (editTextChLibra.getText().length() == 0) {
                    mostrarSnack(arg0, "ERROR Libra: Introduzca el tipo de cambio");
                }else{
                    mTextEuro.setText(String.format("%1$,.2f", Double.parseDouble(String.valueOf(mInput.getText())) / 166.386) + "€");
                    mTextDolar.setText(String.format("%1$,.2f", Double.parseDouble(String.valueOf(mInput.getText())) / 166.386 / Double.parseDouble(String.valueOf(editTextChDolar.getText()))) + "$");
                    mTextLibra.setText(String.format("%1$,.2f", Double.parseDouble(String.valueOf(mInput.getText())) / 166.386 / Double.parseDouble(String.valueOf(editTextChLibra.getText()))) + "£");
                    ocultarTeclado();
                }
            }
        });

        mImagen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            public void onClick(View arg0) {
                mInput.setText("");
                mTextEuro.setText("");
                mTextDolar.setText("");
                mTextLibra.setText("");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                ocultarTeclado();
            }
        });

        if (savedInstanceState != null) {
            mTextEuro.setText(savedInstanceState.getString("euros"));
            mTextDolar.setText(savedInstanceState.getString("dolares"));
            mTextLibra.setText(savedInstanceState.getString("libras"));
        }

        mBotonPort.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        btnChDolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    guardar_dato(fileDolar,editTextChDolar.getText().toString());
                    Toast.makeText(MainActivity.this, "Dólar guardado", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Error guardando dólar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnChLibra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    guardar_dato(fileLibra,editTextChLibra.getText().toString());
                    Toast.makeText(MainActivity.this, "Libra guardado", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Error guardando libra", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    private String cargarFichero(String file){
        try {
            InputStreamReader archivo = new InputStreamReader(openFileInput(file));
            BufferedReader br = new BufferedReader(archivo);
            String line = br.readLine();
            String todas = "";
            while (line != null) {
                todas = todas + line + "\n";
                line = br.readLine();
            }
            br.close();
            archivo.close();
            return todas;
        } catch (IOException e) {
            Toast.makeText(this, "Error leyendo dólar", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    
    public void guardar_dato(String file,String data) throws IOException{
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput(file,
                    Activity.MODE_PRIVATE));
            archivo.write(data);
            archivo.flush();
            archivo.close();
    }
    private boolean existe_archivo(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++)
            if (archbusca.equals(archivos[f]))
                return true;
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mimenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finishAffinity();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("euros", mTextEuro.getText().toString());
        savedInstanceState.putString("dolares", mTextDolar.getText().toString());
        savedInstanceState.putString("libras", mTextLibra.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTextEuro.setText(savedInstanceState.getString("euros"));
        mTextDolar.setText(savedInstanceState.getString("dolares"));
        mTextLibra.setText(savedInstanceState.getString("libras"));
    }

    private void mostrarSnack(View v, String msg) {
        ocultarTeclado();
        Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void ocultarTeclado() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
    }
}
