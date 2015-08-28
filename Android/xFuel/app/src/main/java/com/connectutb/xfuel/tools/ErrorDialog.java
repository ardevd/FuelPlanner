package com.connectutb.xfuel.tools;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ErrorDialog {

   public void showErrorDialog(String title, String message, Context context){
       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
       alertDialogBuilder.setMessage(message);
       alertDialogBuilder.setTitle(title);
       alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {

               dialog.cancel();
           }});
       alertDialogBuilder.show();
   }
}
