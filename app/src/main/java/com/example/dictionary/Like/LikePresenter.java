package com.example.dictionary.Like;

import android.content.Context;

import com.example.dictionary.Base.BasePresenter;
import com.example.dictionary.Interface.ILike;
import com.example.dictionary.RecyclerView.Like;
import com.example.dictionary.RecyclerView.LikeAdapter;

import java.util.List;

public class LikePresenter extends BasePresenter<LikeFragment,LikeModel, ILike.VP> {
    @Override
    public LikeModel getmModelInstance() {
        return new LikeModel(this);
    }

    @Override
    public ILike.VP getContract() {
        return new ILike.VP() {
            @Override
            public void getLikeList(Context context) {
                mModel.getContract().getLikeList(context);
            }

            @Override
            public void responseForLike(List<Like> getLikeList) {
                mView.getContract().responseForLike(getLikeList);
            }

            @Override
            public void deleteAll(Context context, Like like) {
                mModel.getContract().deleteAll(context,like);
            }

            @Override
            public void responseForeDelete() {
                mView.getContract().responseForeDelete();
            }

            @Override
            public void getLetter(LikeAdapter likeAdapter) {
                mModel.getContract().getLetter(likeAdapter);
            }

            @Override
            public void responseForLetter(List<String> letterIndexList) {
                mView.getContract().responseForLetter(letterIndexList);
            }

            @Override
            public void updateList(Context context) {
                mModel.getContract().updateList(context);
            }

            @Override
            public void responseForUpdate(List<Like> updateList) {
                mView.getContract().responseForUpdate(updateList);
            }
        };
    }
}
