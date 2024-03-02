package utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.example.proapp.R;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        if(!common.isConnectedToInternet(context)){
            AlertDialog.Builder builder =  new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.no_internet_connection,null);
            builder.setView(view);

            AppCompatButton btnRetry = view.findViewById(R.id.btn_retry);

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);

            btnRetry.setOnClickListener(view1 -> {
                dialog.dismiss();
                onReceive(context,intent);
            });
        }
    }
}
