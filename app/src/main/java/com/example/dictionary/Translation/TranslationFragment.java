package com.example.dictionary.Translation;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.dictionary.Base.BaseFragment;
import com.example.dictionary.Interface.ITranslation;
import com.example.dictionary.R;

import okhttp3.OkHttpClient;

public class TranslationFragment extends BaseFragment<TranslationPresenter, ITranslation.VP> {
    private EditText inputEditText;
    private ImageButton from;
    private ImageButton to;
    private ImageButton exchange;
    private ImageButton translate;
    private ImageButton play;
    private TextView fromText;

    private TextView toText;
    private TextView resultText;
    private String fromLan = "zh-CHS";
    private String toLan = "en";
    private OkHttpClient client = new OkHttpClient();
    private String appKey = "284f6f8423c0820f";
    private String appSecret = "kfTTlvnxbtR1w5a18sL9I9snykuYdNPO";
    private String translateUrl = "https://openapi.youdao.com/api";
    private String inputText;
    private String resultTranslation;
    private String apiUrl;

//    protected void onResume() {
//        super.onResume();
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
//        BottomLayout bottomLayout = findViewById(R.id.bottom);
//        bottomLayout.updateButtonStates("translation");
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        setContentView(R.layout.activity_translation);
//        initView();
//        initData();
//        initListener();
//        initClass();
//    }

    @Override
    public ITranslation.VP getContract() {
        return new ITranslation.VP() {
            @Override
            public void requestNetwork(String url, String inputText) {
                mPresenter.getContract().requestNetwork(url,inputText);
            }

            @Override
            public void responseForNetwork(String translation) {
                Log.d("tranDeBug", "responseForNetwork: "+translation);
                getActivity().runOnUiThread(()->resultText.setText(translation));
            }
        };
    }

    @Override
    public TranslationPresenter getmPresenter() {
        return new TranslationPresenter();
    }

    @Override
    public void initAdapter() {
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_translation;
    }

    @Override
    public void initClass() {
    }

    @Override
    public void initView(View v) {
        resultText = v.findViewById(R.id.result_text);
        exchange = (ImageButton) v.findViewById(R.id.ex);
        fromText = v.findViewById(R.id.from_text);
        toText = v.findViewById(R.id.to_text);
        inputEditText = v.findViewById(R.id.edit_text);
        translate = (ImageButton) v.findViewById(R.id.translate);
        from = (ImageButton) v.findViewById(R.id.from);
        to = (ImageButton) v.findViewById(R.id.to);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        exchange.setOnClickListener(this);
        from.setOnClickListener(this);
        to.setOnClickListener(this);
        translate.setOnClickListener(this);
    }

    @Override
    public <ERROR> void responseError(Error error, Throwable throwable) {

    }

    private void showFromPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(),from);
        popupMenu.getMenuInflater().inflate(R.menu.choose, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.english:
                        fromText.setText("英语");
                        fromLan = "en";
                        return true;
                    case R.id.chinese:
                        fromText.setText("中文");
                        fromLan = "zh-CHS";
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void showToPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(),to);
        popupMenu.getMenuInflater().inflate(R.menu.choose, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.english:
                        toText.setText("英语");
                        toLan = "en";
                        return true;
                    case R.id.chinese:
                        toText.setText("中文");
                        toLan = "zh-CHS";
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ex:
                String fromLanguage = fromText.getText().toString();
                String toLanguage = toText.getText().toString();

                fromText.setText(toLanguage);
                toText.setText(fromLanguage);

                String tempLanguage = fromLan;
                fromLan = toLan;
                toLan = tempLanguage;
                break;
            case R.id.to:
                showToPopupMenu();
                break;
            case R.id.from:
                showFromPopupMenu();
                break;
            case R.id.translate:
                inputText = inputEditText.getText().toString();
                Log.d("inputText", "initData: "+inputText);
                apiUrl = translateUrl + "?q=" + inputText
                        + "&from=" + fromLan + "&to=" + toLan;
                getContract().requestNetwork(apiUrl,inputText);
                break;
        }
    }
}