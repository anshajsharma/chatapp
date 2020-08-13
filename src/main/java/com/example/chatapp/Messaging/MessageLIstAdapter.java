package com.example.chatapp.Messaging;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.RegisterAndLogin.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

public class MessageLIstAdapter extends RecyclerView.Adapter<MessageLIstAdapter.ViewHolder>  {
    private List<SingleMessage> chats;
    private Context ctx;
    private DatabaseReference mMessageRef, mChatRooomRef;
    private FirebaseUser currUser;
    private int MESSAGE_RECEIVED=0,MESSAGE_SENT=1;
    private String user2,type;
    private static final String TAG = "MessageLIstAdapter";

    public MessageLIstAdapter(List<SingleMessage> chats, Context ctx ,String User2 ,String Type) {
        this.chats = chats;
        this.ctx = ctx;
        this.user2 = User2;
        this.type = Type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.received_messages_layout, parent, false);
        }
        else{
             view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sent_messages_layout, parent, false);
        }

        return new MessageLIstAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //Every position describes a different message
        final SingleMessage message = chats.get(position);

        final String res = path(user2,FirebaseAuth.getInstance().getUid());
        //What to set in single holder
        holder.setDetailAndOnclickFns(message,res);
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


        LinearLayout date = holder.mView.findViewById(R.id.date);
        TextView dateValue = holder.mView.findViewById(R.id.dateValue);
        TextView messageTime = holder.mView.findViewById(R.id.messageTime);
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM YYYY", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        final TextView messageBody = holder.mView.findViewById(R.id.message_body);

        CardView messageCardView = holder.mView.findViewById(R.id.cardView);
        ImageView messageCorner = holder.mView.findViewById(R.id.message_corner);

//        if()




        if(message.getType().equals("Location") && !message.getAvailability().contains("none")){
            messageBody.setTextColor(Color.parseColor("#190DAB"));
        }else if(message.getAvailability().contains("none")) {
            messageBody.setTextColor(Color.parseColor("#B5B3B3"));
        }
        else{
            messageBody.setTextColor(Color.parseColor("#363636"));
        }
//        Log.i(TAG, "onBindViewHolder: " + messageBody.getText());
        holder.stripUnderlines(messageBody);
        messageBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(message.getType().equals("Location")){
                    String geoUri = message.getLocation_URL();

                    if(geoUri != null && !geoUri.isEmpty()){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                        ctx.startActivity(intent);
                    }
                }
            }
        });
        final TextView senderName = holder.mView.findViewById(R.id.senderName);
        if(!type.equals("Group")){
            if(!message.getSender().equals(FirebaseAuth.getInstance().getUid()))
            senderName.setVisibility(GONE);
        }else{
            if(position>0 && !message.getSender().equals(FirebaseAuth.getInstance().getUid())){
                SingleMessage prevMessage = chats.get(position-1);
                if(message.getSender().equals(prevMessage.getSender())){
                    senderName.setVisibility(GONE);
                }else{
                    senderName.setVisibility(View.VISIBLE);
                    mRootRef.child("Users").child(message.getSender()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Users users=dataSnapshot.getValue(Users.class);
                                senderName.setText(users.getName());
                            }else{
                                senderName.setText("Deleted user..");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        }

        if(position>0){
            SingleMessage prevMessage = chats.get(position-1);
            if(sdf3.format(prevMessage.getTimestamp()).equals(sdf3.format(message.getTimestamp()))){
                date.setVisibility(GONE);
            }else{
                date.setVisibility(View.VISIBLE);
            }
            if(message.getSender().equals(prevMessage.getSender())){
                messageCorner.setVisibility(View.INVISIBLE);
                holder.setMargins(messageCardView, 0, 1, 0, 0);

            }else{
                messageCorner.setVisibility(View.VISIBLE);
                holder.setMargins(messageCardView, 0, 3, 0, 0);
            }
        }
        if(position==chats.size()-1){
            holder.setMargins(messageCardView, 0, 3, 0, 5);
        }

        dateValue.setText(sdf3.format(message.getTimestamp()));
        messageTime.setText(sdf2.format(message.getTimestamp()));

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        final String user1 = currUser.getUid();
//        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                long now = System.currentTimeMillis();
                final TextView tv = holder.mView.findViewById(R.id.message_body);

                long temp  = now - message.getTimestamp();
                 final int SECOND_MILLIS = 1000;
                 final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
                 final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
                 boolean asdf = temp<HOUR_MILLIS ;




                 if(temp<HOUR_MILLIS && message.getSender().equals(currUser.getUid()) && !message.getAvailability().contains("none"))
                 {
                     AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
                     builder1.setMessage("Delete message??");
                     builder1.setCancelable(true);
                     builder1.setPositiveButton(
                             "DELETE FOR ME",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {

//                                     Toast.makeText(ctx, "DELETE FOR ME", Toast.LENGTH_SHORT).show();

                                 mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                     .addListenerForSingleValueEvent(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                             if(dataSnapshot.hasChild("availability") && !type.equals("Group"))
                                             {

                                                 if(dataSnapshot.child("availability").getValue(String.class).contains(user2))
                                                 {
                                                     mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                                             .removeValue();
                                                 }
                                                 else{
                                                     String availability = dataSnapshot.child("availability").getValue(String.class);
                                                     availability = availability+ user1;
                                                     mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                                             .child("availability").setValue(availability);
                                                 }

                                             }else if(dataSnapshot.hasChild("availability")){
                                                 String availability = dataSnapshot.child("availability").getValue(String.class);
                                                 availability = availability+ user1;
                                                 mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                                         .child("availability").setValue(availability );
                                             }


                                         }

                                         @Override
                                         public void onCancelled(@NonNull DatabaseError databaseError) {

                                         }
                                     });

                                 }
                             });

                     builder1.setNegativeButton(
                             "DELETE FOR EVERYONE",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
//                                     Toast.makeText(ctx, "DELETE FOR EVERYONE", Toast.LENGTH_SHORT).show();

                                     mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                             .addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                     if(dataSnapshot.exists() && !type.equals("Group"))
                                                     {
                                                         mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                                                 .child("message_body").setValue("@-> This message was deleted");
                                                         String availability = dataSnapshot.child("availability").getValue(String.class);
                                                         if(availability!=null) availability+="none";
                                                         else availability="none";
                                                             mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                                                     .child("availability").setValue(availability);

                                                     }else if(dataSnapshot.hasChild("availability")){
                                                         String availability = dataSnapshot.child("availability").getValue(String.class);
                                                         availability = "none" + availability;
                                                         mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                                                 .child("message_body").setValue("@-> This message was deleted");
                                                         mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                                                 .child("availability").setValue(availability);
                                                     }


                                                 }

                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                                 }
                                             });



                                 }
                             });

                     AlertDialog alert11 = builder1.create();
                     alert11.show();



                 }
                 else
                 {

                     AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
                     builder1.setMessage("Delete message??");
                     builder1.setCancelable(true);

                     builder1.setPositiveButton(
                             "DELETE FOR ME",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {
//                                     Toast.makeText(ctx, "DELETE FOR ME", Toast.LENGTH_SHORT).show();

                                     mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                             .addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                     if(dataSnapshot.hasChild("availability") && !type.equals("Group"))
                                                     {

                                                         if(dataSnapshot.child("availability").getValue(String.class).contains(user2))
                                                         {
                                                             mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                                                     .removeValue();
                                                         }
                                                         else{
                                                             String availability = dataSnapshot.child("availability").getValue(String.class);
                                                             availability = availability+ user1;
                                                             mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                                                     .child("availability").setValue(availability);
                                                         }

                                                     }else if(dataSnapshot.hasChild("availability")){
                                                         String availability = dataSnapshot.child("availability").getValue(String.class);
                                                         availability = availability+ user1;
                                                         mRootRef.child(res).child(String.valueOf(message.getMessageID()))
                                                                 .child("availability").setValue(availability );
                                                     }


                                                 }

                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                                 }
                                             });


                                 }
                             });


                     builder1.setNegativeButton(
                             "Cancel",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int id) {

//                                     Toast.makeText(ctx, "Cancel", Toast.LENGTH_SHORT).show();

                                     dialog.dismiss();


                                 }
                             });

                     AlertDialog alert11 = builder1.create();
                     alert11.show();



                 }



                return false;
            }
        });



    }


    @Override
    public int getItemCount() {
        return chats.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setMargins (View view, int left, int top, int right, int bottom) {
            if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                p.setMargins(left, top, right, bottom);
                view.requestLayout();
            }
        }
        private void stripUnderlines(TextView textView) {
            Spannable s = new SpannableString(textView.getText());
            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            for (URLSpan span: spans) {
                int start = s.getSpanStart(span);
                int end = s.getSpanEnd(span);
                s.removeSpan(span);
                span = new URLSpanNoUnderline(span.getURL());
                s.setSpan(span, start, end, 0);
            }
            textView.setText(s);
        }

        @SuppressLint("ResourceAsColor")
        public void setDetailAndOnclickFns(final SingleMessage message, final String res) {

            currUser = FirebaseAuth.getInstance().getCurrentUser();
            final String user1 = currUser.getUid();
            final TextView tv = mView.findViewById(R.id.message_body);
            final ImageView imageView = mView.findViewById(R.id.image);
            final ImageView downloadImage = mView.findViewById(R.id.download_image);
            downloadImage.setVisibility(GONE);
            final DatabaseReference mRootRef=FirebaseDatabase.getInstance().getReference();
            final DatabaseReference mMessageRef =  mRootRef.child("Messages").child(path(message.getSender(),message.getReceiver())).child(String.valueOf(message.getMessageID()));
            tv.setText(message.getMessage_body());









            String sentOrReceived="Received";
            mMessageRef.keepSynced(true);

            if(message.getSender().equals(user1)) sentOrReceived="Sent";

            final String finalSentOrReceived = sentOrReceived;

            if(message.getMessage_body().equals("")){
                tv.setVisibility(GONE);
            }else{
                tv.setVisibility(View.VISIBLE);
            }


//            mMessageRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.hasChild("downloaded"))
//                    {
//
//                        if(message.getDownloaded().equals("NO"))
//                        {
//                            Log.i("sdsd", "onDataChange: ");
//                           downloadImage.setVisibility(View.VISIBLE);
//                        }else{
//                            Log.i("sdsd", "onDataChange2: ");
//                            downloadImage.setVisibility(View.GONE);
//                        }
//
//                    }else if(!message.getType().equals("text")){
//
//                        downloadImage.setVisibility(View.VISIBLE);
//                    }else{
//
//                        imageView.setVisibility(View.GONE);
//                        downloadImage.setVisibility(View.GONE);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

            if(message.getType().equals("image"))
            {
               // Log.i("fghbjn", "imageCompressorAndUploader12: " + "gvhidfhujmdfhg");
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(ctx)
                       .load(message.getImage_url())
                        .placeholder(R.drawable.image_loading)
                       .centerCrop()
                       .fit()
                       .into(imageView);
            }
            if(message.getType().equals("pdf"))
            {
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(ctx)
                        .load(message.getFile_url())
                        .placeholder(R.drawable.document_icon)
                        .centerCrop()
                        .fit()
                        .into(imageView);
            }
            if(!(message.getType().equals("image")||message.getType().equals("pdf"))){
                imageView.setVisibility(GONE);
            }


            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(message.getType().equals("imageff") && message.getDownloaded().equals("NO"))
                    {
                        String url = "url you want to download";
                        if(ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                            // this will request for permission when user has not granted permission for the app
                            ActivityCompat.requestPermissions((Activity)ctx, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }

                        else{
                            //Download Script

                            DownloadManager downloadManager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(message.getImage_url());
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setVisibleInDownloadsUi(true);        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            final File path = Environment.getExternalStorageDirectory();
                            File path2 = Environment.getRootDirectory();

                            Log.i("asdf12", "onClick: " + path + "--1--" + path2 + "--1--" + Environment.getExternalStorageState());

                            String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(message.getTimestamp()) + ".jpg";
                            filename = "ChatApp-" + filename ;

                            File dir = new File(path + "/ChatApp/" + finalSentOrReceived);
                            dir.mkdirs();



                            final File file = new File(dir,filename);

                            request.setDestinationUri(Uri.fromFile(file));

                            Log.i("iukhi", "onClick: " + Uri.fromFile(file));

//                            Picasso.with(ctx)
//                                    .load( Uri.fromFile(file))
//                                    .placeholder(R.drawable.avtar)
//                                    .into(imageView);

                       //     request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());
                            downloadManager.enqueue(request);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                final Uri contentUri = Uri.fromFile(dir);

                                scanIntent.setData(contentUri);
                                ctx.sendBroadcast(scanIntent);
                            } else {
                                final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(dir));
                                ctx.sendBroadcast(intent);
                            }
                            final String finalFilename = filename;
                            BroadcastReceiver onComplete=new BroadcastReceiver() {
                                public void onReceive(Context ctxt, Intent intent) {
                                    mMessageRef.child("downloaded").setValue("YES");
                                    mMessageRef.child("image_url").setValue(String.valueOf(Uri.fromFile(file)));
                                }
                            };

                            ctx.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));





                        }
                    }
                    else if(!message.getType().equals("image"))
                    {

                    }
                }
            });


        }
    }

    @Override
    public int getItemViewType(int position) {
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currUser.getUid().equals(chats.get(position).getSender())){
            return MESSAGE_SENT;
        }
        else{
            return MESSAGE_RECEIVED;
        }

    }
    public String path(String s1,String s2)
    {
        if(!type.equals("Group")){
            String res;
            if(s1.compareTo(s2)>0) res= s1+"/"+s2;
            else res= s2+"/"+s1;
            res = "Messages/"+res;
            return res;
        }else{
            return "GroupMessages/"+s1;
        }

    }
    private void openFile(File url) {

        try {

            Uri uri = Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip");
            } else if (url.toString().contains(".rar")){
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(ctx, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param context used to check the device version and DownloadManager information
     * @return true if the download manager is available
     */
    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }
    public boolean isSDCardPresent()
    {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();

        if(isSDSupportedDevice && isSDPresent)
        {
            // yes SD-card is present

         return true;

        }
        else
        {
            // Sorry
            return false;
        }
    }
}
