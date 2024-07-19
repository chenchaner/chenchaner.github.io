package com.example.dictionary.DailySaying;

import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dictionary.Base.BaseFragment;
import com.example.dictionary.Interface.IDailySaying;
import com.example.dictionary.AuxiliaryClass.ImageLoader;
import com.example.dictionary.AuxiliaryClass.PlayMedia;
import com.example.dictionary.R;
import com.example.dictionary.Search.SearchActivity;

public class DailySayingFragment extends BaseFragment<DailySayingPresenter, IDailySaying.VP> {
    private MediaPlayer mediaPlayer;
    private PlayMedia playMedia;
    private ImageView daily;
    private TextView sentence;
    private TextView date;
    private TextView translation;
    private String tts;
    private ImageButton search;
    private ImageButton play;
    private String apiUrl;

    @Override
    public IDailySaying.VP getContract() {
        return new IDailySaying.VP() {

            @Override
            public void requestNetwork(String apiUrl) {
                mPresenter.getContract().requestNetwork(apiUrl);
            }

            @Override
            public void setImageUrl(String imageUrl) {
                getActivity().runOnUiThread(()->{
                ImageLoader imageLoader = new ImageLoader(daily);
                imageLoader.execute(imageUrl);
            });
            }

            @Override
            public void setTextSentence(String TextSentence) {
               getActivity().runOnUiThread(()->sentence.setText(TextSentence));
            }

            @Override
            public void setTextTranslation(String TextTranslation) {
                getActivity().runOnUiThread(()->translation.setText(TextTranslation));
            }

            @Override
            public void setTextDate(String TextDate) {
                String TAG = "TextDate";
                Log.d(TAG, "setTextDate: "+TextDate);
                getActivity().runOnUiThread(()->date.setText(TextDate));
            }

            @Override
            public void setTtsUrl(String ttsUrl) {
                tts = ttsUrl;
                Log.d("ttsUrl", "setTtsUrl: "+ttsUrl);
            }
        };
    }

    @Override
    public DailySayingPresenter getmPresenter() {
        return new DailySayingPresenter();
    }

    @Override
    public void initAdapter() {

    }

    public void sendRequest() {
        getContract().requestNetwork("http://open.iciba.com/dsapi");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_dailysaying;
    }

    @Override
    public void initClass() {
        playMedia = new PlayMedia();
        sendRequest();
    }

    @Override
    public void initView(View v) {
        date = v.findViewById(R.id.date);
        daily = v.findViewById(R.id.daily_pic);
        sentence = v.findViewById(R.id.sentence);
        translation = v.findViewById(R.id.translation);
        search = v.findViewById(R.id.search_button);
        play = v.findViewById(R.id.play);

    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        play.setOnClickListener(this);
        search.setOnClickListener(this);
    }

    @Override
    public void responseError(Error error, Throwable throwable) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_button:
                Log.d("search", "onClick: success");
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.play:
                Log.d("tts", "onClick:tts "+tts);
                playMedia.play(tts);
        }
    }

}
