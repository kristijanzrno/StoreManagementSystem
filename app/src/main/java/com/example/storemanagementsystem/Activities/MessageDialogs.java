package com.example.storemanagementsystem.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MessageDialogs {

    public interface DialogResponses{
        void finishTransaction(boolean answer);
        void removeItem(boolean answer, int position);
        void addManually(String itemID);
        void confirmTransaction(boolean answer);
    }

    public static void removeItemDialog(Context context, DialogResponses responses, int position){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Remove")
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

    public static void confirmPurchase(Context context, DialogResponses responses){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Confirm")
                .setMessage("Are you sure you want to confirm the transaction?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responses.confirmTransaction(true);

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responses.confirmTransaction(false);
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

    public static void addManually(Context context, DialogResponses responses){
        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Enter item ID:")
                .setView(input)
                .setPositiveButton("ADD ITEM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responses.addManually(input.getText().toString());
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.show();
    }
}
