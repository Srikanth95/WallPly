package com.wallply.wallply.quickblox;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.wallply.wallply.quickblox.DataHolder;

import java.util.ArrayList;



public class ServerSetup extends IntentService {

    int count;


    int per_page;
    int current;


    private static long IMAGES_PER_PAGE;
    private long current_page;
    DatabaseReference databaseReference;
    QBPagedRequestBuilder builder;

    QBUser qbUser;
    private SharedPreferences permissionStatus;

    static final String APP_ID = "53884";
    static final String AUTH_KEY = "CG5Dkj8ZgK3udPO";
    static final String AUTH_SECRET = "M9eQVMjh3TbDChm";
    static final String ACCOUNT_KEY = "1RTkWe2UjjtM3BUzuymg";


    public ServerSetup() {
        super("ServerSetup");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        qbUser = new QBUser("srikanth95", "password.1");

        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle args) {
                // success
            }

            @Override
            public void onError(QBResponseException error) {
                // error
            }
        });


        count = 1;


        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (count == 1) {

                    current_page = (long) dataSnapshot.child("current_page").getValue();
                    IMAGES_PER_PAGE = (long) dataSnapshot.child("per_page").getValue();
                    DataHolder.getInstance().clear();
                    getFileList();

                } else {

                    Intent intent = new Intent();
                    intent.setAction("com.wallply.wallply.REFRESH");
                    sendBroadcast(intent);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        builder = new QBPagedRequestBuilder();


    }

    public void getFileList() {


        Intent intent = new Intent();
        intent.setAction("com.wallply.wallply.LOADING");
        sendBroadcast(intent);

        per_page = (int) IMAGES_PER_PAGE;
        current = (int) current_page;


        builder.setPerPage(per_page);

        builder.setPage(current);


        QBContent.getFiles(builder).performAsync(new QBEntityCallback<ArrayList<QBFile>>() {
            @Override
            public void onSuccess(ArrayList<QBFile> qbFiles, Bundle bundle) {

                if (qbFiles.isEmpty()) {
                    current_page++;

                } else {

                    DataHolder.getInstance().addQbFiles(qbFiles);
                    for (QBFile qbFile : qbFiles) {
                        //
                        if (qbFile.getName().toString().contains("nature")) {
                            //Toast.makeText(ViewActivity.this, ""+qbFile.getName().toString(), Toast.LENGTH_SHORT).show();
                            DataHolder.getInstance().addQbFile(qbFile, "nature");

                        }
                        if (qbFile.getName().toString().contains("city")) {
                            //Toast.makeText(ViewActivity.this, ""+qbFile.getName().toString(), Toast.LENGTH_SHORT).show();
                            DataHolder.getInstance().addQbFile(qbFile, "city");

                        }
                        if (qbFile.getName().toString().contains("pattern")) {
                            //Toast.makeText(ViewActivity.this, ""+qbFile.getName().toString(), Toast.LENGTH_SHORT).show();
                            DataHolder.getInstance().addQbFile(qbFile, "pattern");

                        }
                        if (qbFile.getName().toString().contains("color")) {
                            //Toast.makeText(ViewActivity.this, ""+qbFile.getName().toString(), Toast.LENGTH_SHORT).show();
                            DataHolder.getInstance().addQbFile(qbFile, "color");
                        }

                        if (qbFile.getName().toString().contains("material")) {
                            //Toast.makeText(ViewActivity.this, ""+qbFile.getName().toString(), Toast.LENGTH_SHORT).show();
                            DataHolder.getInstance().addQbFile(qbFile, "material");

                        }

                        if (qbFile.getName().toString().contains("amoled")) {
                            //Toast.makeText(ViewActivity.this, ""+qbFile.getName().toString(), Toast.LENGTH_SHORT).show();
                            DataHolder.getInstance().addQbFile(qbFile, "amoled");

                        }
                        if (qbFile.getName().toString().contains("gradient")) {
                            //Toast.makeText(ViewActivity.this, ""+qbFile.getName().toString(), Toast.LENGTH_SHORT).show();
                            DataHolder.getInstance().addQbFile(qbFile, "gradient");

                        }
                        if (qbFile.getName().toString().contains("car")) {
                            //Toast.makeText(ViewActivity.this, ""+qbFile.getName().toString(), Toast.LENGTH_SHORT).show();
                            DataHolder.getInstance().addQbFile(qbFile, "car");

                        }
                    }


                }

                Intent intent = new Intent();
                intent.setAction("com.wallply.wallply.PROGRESS");
                sendBroadcast(intent);

                loadNextPage();
            }

            @Override
            public void onError(QBResponseException e) {

                current_page++;

                Intent intent = new Intent();
                intent.setAction("com.wallply.wallply.ERROR");
                sendBroadcast(intent);


            }
        });


    }


    public void refresh() {


        DataHolder.getInstance().clear();

        getFileList();


    }


    private void loadNextPage() {
        if (DataHolder.getInstance().isEmpty()) {

            Toast.makeText(getApplicationContext(), "No Photos", Toast.LENGTH_SHORT).show();
        } else {
            current_page--;

            getFileList();


        }
        Intent intent = new Intent();
        intent.setAction("com.wallply.wallply.CHANGE");
        sendBroadcast(intent);


    }


}
