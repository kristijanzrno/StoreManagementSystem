package com.example.storemanagementsystem.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MessageDialogs {

    public interface DialogResponses{
        void finishTransaction(boolean answer);
        void removeItem(boolean answer, int position);
    }

    public static void removeItemDialog(Context context, DialogResponses responses, int position){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Are you sure?")
                .setMessage("Are you sure you want to remove the item from basket?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responses.removeItem(true, position);

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responses.removeItem(false, position);
                    }
                }).create();
        dialog.show();
    }

    public static void transactionFinishedDialog(Context context, DialogResponses responses){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Success")
                .setMessage("Your purchase has successfully been processed!")
                .setPositiveButton("LOG OUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responses.finishTransaction(true);

                    }
                }).setNegativeButton("NEW TRANSACTION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responses.finishTransaction(false);
                    }
                }).create();
        dialog.show();
    }
}
