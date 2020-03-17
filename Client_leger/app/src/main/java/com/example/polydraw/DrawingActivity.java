package com.example.polydraw;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import yuku.ambilwarna.AmbilWarnaDialog; //https://codinginflow.com/tutorials/android/ambilwarna-color-picker-dialog
//seekbar https://www.tutlane.com/tutorial/android/android-seekbar-with-examples

public class DrawingActivity extends AppCompatActivity {
    DrawingCanvas drawingCanvas;
    Button eraseButton;
    Button drawButton;
    Button color;
    Button capStyle;
    SeekBar seekBar;
    TextView seekBarText;
    Chat chat;

    ConstraintLayout mLayout;
    int mDefaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        drawingCanvas = new DrawingCanvas(this,null);
        chat = new Chat();
        setContentView(R.layout.activity_canvas);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        initializeObject();
        eventListeners();

    }

    private void initializeObject(){
        drawingCanvas = (DrawingCanvas) findViewById(R.id.drawing);
        eraseButton = (Button) findViewById(R.id.eraser);
        drawButton = (Button) findViewById(R.id.paint);
        color = (Button) findViewById(R.id.colorButton);
        capStyle = (Button) findViewById(R.id.capButton);
        mLayout = (ConstraintLayout) findViewById(R.id.layout);
        mDefaultColor = ContextCompat.getColor(DrawingActivity.this, R.color.colorPrimary);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBarText = (TextView) findViewById(R.id.sbTextView);

    }

    private void eventListeners() {
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(true);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.selected_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));
                capStyle.setBackground(getDrawable(R.drawable.round_button));
                openEraserOptions();

            }
        });
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(false);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.selected_button));
                capStyle.setBackground(getDrawable(R.drawable.round_button));
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(false);
                color.setBackground(getDrawable(R.drawable.selected_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));
                capStyle.setBackground(getDrawable(R.drawable.round_button));
                openColorPicker();

            }
        });

        capStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingCanvas.setErase(false);
                color.setBackground(getDrawable(R.drawable.round_button));
                eraseButton.setBackground(getDrawable(R.drawable.round_button));
                drawButton.setBackground(getDrawable(R.drawable.round_button));
                capStyle.setBackground(getDrawable(R.drawable.selected_button));
                openCapOptions();

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                drawingCanvas.setWidth(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarText.setText(pval + "/" + seekBar.getMax());
            }
        });
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                drawingCanvas.setDrawingColor(mDefaultColor);

            }
        });
        colorPicker.show();
    }

    //https://www.javatpoint.com/android-popup-menu-example
    public void openCapOptions(){
        PopupMenu popup = new PopupMenu(DrawingActivity.this, capStyle);
        popup.getMenuInflater().inflate(R.menu.cap_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(DrawingActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                drawingCanvas.setPencilTip(item.getTitle().toString());
                return true;
            }
        });
        popup.show();

    }

    public void openEraserOptions(){
        PopupMenu popup = new PopupMenu(DrawingActivity.this, eraseButton);
        popup.getMenuInflater().inflate(R.menu.eraser_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(DrawingActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();
    }
}

