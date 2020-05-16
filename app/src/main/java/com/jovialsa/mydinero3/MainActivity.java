package com.jovialsa.mydinero3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    private EditText mInput;
    private TextView mTextEuro, mTextDolar, mTextLibra;
    private Button mBotonConvertir;
    private ProgressBar mBar;
    private ImageView mImagen;
    private Button mBotonPort;
    private MenuItem salir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBar = (ProgressBar) findViewById(R.id.barra);
        mInput = (EditText) findViewById(R.id.editDinero);
        mTextEuro = (TextView) findViewById(R.id.textEuro);
        mTextDolar = (TextView) findViewById(R.id.textDolar);
        mTextLibra = (TextView) findViewById(R.id.textLibra);
        mBotonConvertir = (Button) findViewById(R.id.buttonConvertir);
        mImagen = (ImageView) findViewById(R.id.imagen);
        mBotonPort = (Button) findViewById(R.id.btnPort);
        salir = (MenuItem) findViewById(R.id.accion_salir);

        mBotonConvertir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (mInput.getText().length() == 0) {
                    mostrarSnack(arg0, "ERROR Dolar: Introduzca pts");
                } else {
                    mTextEuro.setText(String.format("%1$,.2f", Double.parseDouble(String.valueOf(mInput.getText())) / 166.386) + "€");
                    mTextDolar.setText(String.format("%1$,.2f", Double.parseDouble(String.valueOf(mInput.getText())) / 166.386 / 0.93) + "$");
                    mTextLibra.setText(String.format("%1$,.2f", Double.parseDouble(String.valueOf(mInput.getText())) / 166.386 / 1.14) + "£");
                    mBar.setVisibility(View.VISIBLE);
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
                mBar.setVisibility(View.INVISIBLE);
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
