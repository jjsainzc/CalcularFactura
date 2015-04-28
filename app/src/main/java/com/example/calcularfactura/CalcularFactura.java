package com.example.calcularfactura;


import java.math.RoundingMode;
import java.text.DecimalFormat;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CalcularFactura extends Activity {

	
    private EditText retencionFuente;
    private EditText retencionIva;
    private EditText subTotal;
    private TextView subTotalNeto;
    private EditText totalCobrar;
    private EditText totalFacturado;
    private TextView totalRetenciones;
    private TextView valorDescuento;
    private TextView valorIva;
    private TextView valorRetenidoFuente;
    private TextView valorRetenidoIva;
    private EditText descuento;
    private EditText iva;
    
 
    private DecimalFormat df;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calcular_factura);
		
		
		
		retencionFuente = (EditText) findViewById(R.id.retencionFuente);
		retencionIva = (EditText) findViewById(R.id.retencionIva);
		
		totalCobrar = (EditText) findViewById(R.id.totalCobrar);
		totalFacturado = (EditText) findViewById(R.id.totalFacturado);
		
		subTotal = (EditText) findViewById(R.id.subTotal);
		descuento = (EditText) findViewById(R.id.descuento);
		
		iva = (EditText) findViewById(R.id.iva);
		
		subTotalNeto = (TextView) findViewById(R.id.subTotalNeto);
		totalRetenciones = (TextView) findViewById(R.id.totalRetenciones);
		valorDescuento = (TextView) findViewById(R.id.valorDescuento);
		valorIva = (TextView) findViewById(R.id.valorIva);
		valorRetenidoFuente = (TextView) findViewById(R.id.valorRetenidoFuente);
		valorRetenidoIva = (TextView) findViewById(R.id.valorRetenidoIva);
		
		iva.setGravity(Gravity.RIGHT);
		subTotal.setGravity(Gravity.RIGHT);
		descuento.setGravity(Gravity.RIGHT);
		totalCobrar.setGravity(Gravity.RIGHT);
		totalFacturado.setGravity(Gravity.RIGHT);
		retencionFuente.setGravity(Gravity.RIGHT);
		retencionIva.setGravity(Gravity.RIGHT);
		
		df = new DecimalFormat("###.##");
        df.setMinimumFractionDigits(2);
        df.setRoundingMode(RoundingMode.HALF_EVEN);
		
		limpiarCampos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calcular_factura, menu);
		return true;
	}
	
	private void limpiarCampos() {

        retencionFuente.setText("10.0");
        retencionIva.setText("100.0");
        descuento.setText("0.0");
        iva.setText("12.0");
        subTotal.setText("0.0");
        subTotalNeto.setText("$ 0.0");
        totalCobrar.setText("0.0");
        totalFacturado.setText("0.0");
        totalRetenciones.setText("$ 0.0");
        valorDescuento.setText("$ 0.0");
        valorIva.setText("$ 0.0");
        valorRetenidoFuente.setText("$ 0.0");
        valorRetenidoIva.setText("$ 0.0");
    }
	
	private void calculos(String accion) {

        Float subTotalNeto_;
        Float totalRetenciones_;
        Float valorDescuento_;
        Float valorIva_;
        Float valorRetenidoFuente_;
        Float valorRetenidoIva_;

        // Ocultar el teclado usando una referencia de un EditText
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(subTotal.getWindowToken(), 0);


        Float totalCobrar_ = Float.parseFloat(totalCobrar.getText().toString());
        Float totalFacturado_ = Float.parseFloat(totalFacturado.getText().toString());

        if (accion.equals("recalcular")) {
            if (totalFacturado_ > 0.0f) {
                this.subTotal.setText(df.format(totalFacturado_ / 1.12f));
            }
        }

        Float subTotal_ = Float.parseFloat(this.subTotal.getText().toString());

        // Porcentajes ---------------------------------------------------------
        Float descuento_ = Float.parseFloat(descuento.getText().toString());
        Float iva_ = Float.parseFloat(iva.getText().toString());
        Float retencionFuente_ = Float.parseFloat(retencionFuente.getText().toString());
        Float retencionIva_ = Float.parseFloat(retencionIva.getText().toString());
        // ---------------------------------------------------------------------

        if (accion.equals("liquido")) {
            // otra posible
            // //S= TC / 2 * VD^2 * VR
            float vd = (descuento_ / 100.0f);
            float vr = (retencionFuente_ / 100.0f);
            float tc = totalCobrar_;

            subTotal_ = tc / ((1.0f - vd) * (1.0f - vr));
            this.subTotal.setText( df.format(subTotal_) );
        }

        valorDescuento_ = subTotal_ * (descuento_ / 100.0f);
        subTotalNeto_ = subTotal_ - valorDescuento_;
        
        valorIva_ = subTotalNeto_ * (iva_ / 100.0f);
        
        valorRetenidoFuente_ = subTotalNeto_ * (retencionFuente_ / 100.0f);
        valorRetenidoIva_ = valorIva_ * (retencionIva_ / 100.0f);
        
        totalRetenciones_ = valorRetenidoFuente_ + valorRetenidoIva_;
        
        totalFacturado_ = subTotalNeto_ + valorIva_;
        totalCobrar_ = subTotalNeto_ - valorRetenidoFuente_;
        
        descuento.setText(df.format(descuento_));
        valorDescuento.setText("$ "+df.format(valorDescuento_));
        subTotalNeto.setText("$ "+df.format(subTotalNeto_));
        valorIva.setText("$ "+df.format(valorIva_));
        
        valorRetenidoFuente.setText("$ "+df.format(valorRetenidoFuente_));
        valorRetenidoIva.setText("$ "+df.format(valorRetenidoIva_));
        
        totalRetenciones.setText("$ "+df.format(totalRetenciones_));
        
        totalCobrar.setText(df.format(totalCobrar_));
        totalFacturado.setText(df.format(totalFacturado_));


    }
	
	public void limpiar(View view) {
		limpiarCampos();
	
	}
	public void liquido(View view) {
		calculos("liquido");
	
	}
	public void recalcular(View view) {
		calculos("recalcular");
	
	}

}
