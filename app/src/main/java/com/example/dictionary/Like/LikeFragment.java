package com.example.dictionary.Like;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.Base.BaseFragment;
import com.example.dictionary.Interface.ILike;
import com.example.dictionary.R;
import com.example.dictionary.RecyclerView.LetterIndexAdapter;
import com.example.dictionary.RecyclerView.Like;
import com.example.dictionary.RecyclerView.LikeAdapter;

import java.util.ArrayList;
import java.util.List;

public class LikeFragment extends BaseFragment<LikePresenter, ILike.VP> {
    private SearchView searchView;
    private ImageButton delete;
    private List<Like> originalLikeList = new ArrayList<>();
    private List<Like> likeList = new ArrayList<>();
    private List<String> letterIndexList = new ArrayList<>();
    private List<Like> filteredLikeList = new ArrayList<>();
    private ImageButton list;
    private LikeAdapter likeAdapter;
    private RecyclerView recyclerView_letter;
    private RecyclerView recyclerView_like;
    private LetterIndexAdapter letterIndexAdapter;

    @Override
    public void onResume() {
        super.onResume();
        getContract().updateList(getActivity());
    }

    @Override
    public LikePresenter getmPresenter() {
        return new LikePresenter();
    }

    @Override
    public void initAdapter() {
        LinearLayoutManager layoutManager_like= new LinearLayoutManager(getActivity());
        recyclerView_like.setLayoutManager(layoutManager_like);
        getContract().getLikeList(getActivity());
        likeAdapter = new LikeAdapter(getActivity(),likeList);
        recyclerView_like.setAdapter(likeAdapter);
        Log.d("org", "initAdapter: org"+originalLikeList);

        LinearLayoutManager layoutManager_letter = new LinearLayoutManager(getActivity());
        recyclerView_letter.setLayoutManager(layoutManager_letter);
        getContract().getLetter(likeAdapter);
        letterIndexAdapter = new LetterIndexAdapter(letterIndexList,likeList);
        recyclerView_letter.setAdapter(letterIndexAdapter);
        letterIndexAdapter.setLikeRecyclerView(recyclerView_like);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_like;
    }

    @Override
    public void initClass() {

    }

    @Override
    public void initView(View v) {
        searchView = v.findViewById(R.id.search_dic);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredLikeList.clear();
                for (Like like: likeList){
                    if (like.getWord().contains(newText)){
                        filteredLikeList.add(like);
                    }
                }
                likeAdapter.updateData(filteredLikeList);
                return true;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                likeAdapter.updateData(likeList);
                return false;
            }
        });
        list = (ImageButton) v.findViewById(R.id.sort);
        delete = (ImageButton) v.findViewById(R.id.like_delete);
        recyclerView_like= v.findViewById(R.id.like_display);
        recyclerView_letter = v.findViewById(R.id.letter_sort);
        initAdapter();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        list.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public <ERROR> void responseError(Error error, Throwable throwable) {

    }


    private void showPopupMenu(){
        PopupMenu popupMenu = new PopupMenu(getActivity(),list, Gravity.RIGHT);
        popupMenu.getMenuInflater().inflate(R.menu.list, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_item_default_sort:
                        likeList.clear();
                        likeList.addAll(new ArrayList<>(originalLikeList));
                        Log.d("orgCheck", "onMenuItemClick: "+originalLikeList);
                        likeAdapter.notifyDataSetChanged();
                        recyclerView_letter.setVisibility(View.INVISIBLE);
                        return true;
                    case R.id.menu_item_letter_sort:
                        likeAdapter.sort();
                        recyclerView_letter.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.menu_item_delete:
                        delete.setVisibility(View.VISIBLE);
                        likeAdapter.showCheckBox();
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
            case R.id.sort:
                showPopupMenu();
                break;
            case R.id.like_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("确定删除选中的历史记录？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<Like> selectedLikes = new ArrayList<>();
                        for (int i = 0; i < likeAdapter.getItemCount(); i++) {
                            Like like = likeAdapter.getItemAtPosition(i);
                            if (like != null && like.isChecked()) {
                                selectedLikes.add(like);
                            }
                        }
                        for (Like like : selectedLikes) {
                            getContract().deleteAll(getActivity(),like);
                        }
                        likeList.removeAll(selectedLikes);
                        likeAdapter.hideCheckBox();
                        likeAdapter.notifyDataSetChanged();
                        delete.setVisibility(View.INVISIBLE);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        delete.setVisibility(View.INVISIBLE);
                        likeAdapter.hideCheckBox();
                    }
                });
                builder.show();
                break;
        }
    }
    @Override
    public ILike.VP getContract() {
        return new ILike.VP() {
            @Override
            public void getLikeList(Context context) {
                mPresenter.getContract().getLikeList(context);
            }

            @Override
            public void responseForLike(List<Like> getLikeList) {
                likeList = getLikeList;
                originalLikeList = new ArrayList<>(getLikeList);
            }

            @Override
            public void deleteAll(Context context, Like like) {
                mPresenter.getContract().deleteAll(context,like);
            }

            @Override
            public void responseForeDelete() {
            }

            @Override
            public void getLetter(LikeAdapter likeAdapter) {
                mPresenter.getContract().getLetter(likeAdapter);
            }

            @Override
            public void responseForLetter(List<String> indexList) {
                letterIndexList = indexList;
            }

            @Override
            public void updateList(Context context) {
                mPresenter.getContract().updateList(context);
            }

            @Override
            public void responseForUpdate(List<Like> updateList) {
                likeAdapter.updateData(updateList);
            }
        };
    }
}