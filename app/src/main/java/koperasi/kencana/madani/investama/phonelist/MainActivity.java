package koperasi.kencana.madani.investama.phonelist;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.xeoh.android.texthighlighter.TextHighlighter;
import java.util.ArrayList;
import java.util.List;

import koperasi.kencana.madani.investama.phonelist.adapter.ContactPersonListViewAdapter;
import koperasi.kencana.madani.investama.phonelist.entity.ContactPersonEntity;
import me.zhouzhuo.zzletterssidebar.ZzLetterSideBar;
import me.zhouzhuo.zzletterssidebar.interf.OnLetterTouchListener;

public class MainActivity extends AppCompatActivity {


    private ListView listView;
    private ZzLetterSideBar sideBar;
    private TextView dialog;
    private ContactPersonListViewAdapter adapter;
    private List<ContactPersonEntity> mDatas;
    private List<ContactPersonEntity> mDatas_cache;;
    private EditText editText;
    // Initialize TextHighlighter
    private TextHighlighter textHighlighter = new TextHighlighter()
            .setForegroundColor(Color.parseColor("#2962FF"));
    private String phone = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.READ_CONTACTS)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            initView();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    })
                    .withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {

                        }
                    })
                    .check();
        } else {
            initView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initView() {

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                initRun();
            }
        };
        handler.postDelayed(runnable, 1000);

    }

    public void initRun() {
        sideBar = (ZzLetterSideBar) findViewById(R.id.sidebar);
        dialog = (TextView) findViewById(R.id.tv_dialog);
        listView = (ListView) findViewById(R.id.list_view);
        editText = (EditText) findViewById(R.id.search);

        mDatas = new ArrayList<>();
        adapter = new ContactPersonListViewAdapter(getApplicationContext(), mDatas, textHighlighter);
        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        initData();
        initEvent();
    }

    public void initData() {
        //update data
        mDatas = getListContact();
        mDatas_cache = mDatas;
        adapter.updateListView(mDatas_cache);
    }

    public void initEvent() {

        sideBar.setLetterTouchListener(listView, adapter, dialog, new OnLetterTouchListener() {
            @Override
            public void onLetterTouch(String letter, int position) {
            }

            @Override
            public void onActionUp() {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= listView.getHeaderViewsCount()) {
                    TextView tv_phone = (TextView) view.findViewById(R.id.list_item_tv_hp);
                }
            }
        });
    }

    private List<ContactPersonEntity> getListContact(){
        List<ContactPersonEntity> list = new ArrayList<>();

        Cursor cursor = getApplicationContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY);
        String phoneNumber;
        String phoneName;

        while (cursor.moveToNext()) {
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            ContactPersonEntity entity = new ContactPersonEntity();
            entity.setPersonName(phoneName+"@HP"+phoneNumber);
            list.add(entity);

        }
        return list;
    }

    private void filter(String text) {

        textHighlighter.highlight(text, TextHighlighter.CASE_INSENSITIVE_MATCHER);

        List<ContactPersonEntity> personEntities = new ArrayList<>();
        for (ContactPersonEntity name : mDatas) {
            if(name.getPersonName().toLowerCase().contains(text.toLowerCase())) {
                ContactPersonEntity entity = new ContactPersonEntity();
                entity.setPersonName(name.getPersonName());
                personEntities.add(entity);
            }
        }

        //update data
        if(text.isEmpty()) {
            adapter.updateListView(mDatas);
        } else {
            adapter.updateListView(personEntities);
        }
    }

}