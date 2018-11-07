package com.foamtrace.photopicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.foamtrace.photopicker.widget.ViewPagerFixed;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by foamtrace on 2015/8/25.
 */
public class PhotoPreviewActivity extends AppCompatActivity implements PhotoPagerAdapter.PhotoViewClickListener {

    public static final String EXTRA_PHOTOS = "extra_photos";
    public static final String EXTRA_CURRENT_ITEM = "extra_current_item";
    public static final String EXTRA_IS_PREVIEW_SELECTED_IAMGE = "extra_is_preview_seleted_images";

    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "preview_result";

    /**
     * 预览请求状态码
     */
    public static final int REQUEST_PREVIEW = 99;

    private ArrayList<ImageModel> paths;
    private ViewPagerFixed mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private int currentItem = 0;
    private ImageView iv_back;
//    private ImageView iv_delete;
    private Context mContext;
    private boolean isPreviewSeletedImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_image_preview);

        initData();
        initViews();


    }

    private void initData() {
        ArrayList pathArr = new ArrayList<>();
        paths = (ArrayList<ImageModel>) getIntent().getSerializableExtra(EXTRA_PHOTOS);
        Log.i("oye","pre paths == " + paths);
        isPreviewSeletedImages = getIntent().getBooleanExtra(EXTRA_IS_PREVIEW_SELECTED_IAMGE, false);
        paths.addAll(pathArr);
        currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);
        //        updateActionBarTitle();
    }

    private void initViews() {
        mViewPager = (ViewPagerFixed) findViewById(R.id.vp_photos);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(onClickListener);
//        iv_delete = (ImageView) findViewById(R.id.iv_delete);
//        iv_delete.setOnClickListener(onClickListener);

        mPagerAdapter = new PhotoPagerAdapter(this, paths);
        mPagerAdapter.setPhotoViewClickListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(5);

//        if (isPreviewSeletedImages) {
//            iv_delete.setVisibility(View.VISIBLE);
//        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                updateActionBarTitle();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void OnPhotoTapListener(View view, float v, float v1) {
        onBackPressed();
    }

    public void updateActionBarTitle() {
        getString(R.string.image_index, mViewPager.getCurrentItem() + 1, paths.size());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        // 若是预览已选择的图片，则需要将预览后的图片返回（因为可以再预览页面进行删除操作）
        if (isPreviewSeletedImages) {
            intent.putExtra(EXTRA_RESULT, (Serializable) paths);
        }
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.iv_back) {
                onBackPressed();
            }

//            if (id == R.id.iv_delete) {
//                final int index = mViewPager.getCurrentItem();
//                if (paths.size() <= 1) {
//                    // 最后一张照片弹出删除提示
//                    // show confirm dialog
//                    new AlertDialog.Builder(mContext)
//                            .setTitle(R.string.confirm_to_delete)
//                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                    paths.remove(index);
//                                    onBackPressed();
//                                }
//                            })
//                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                }
//                            })
//                            .show();
//                } else {
//                    paths.remove(index);
//                    Log.i("oye", "paths == " + paths.size());
//                    mPagerAdapter.notifyDataSetChanged();
//                }
//            }
        }
    };

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//
//        // 删除当前照片
//        if (item.getItemId() == R.id.action_discard) {
//            final int index = mViewPager.getCurrentItem();
//            final String deletedPath = paths.get(index);
//            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.deleted_a_photo,
//                    Snackbar.LENGTH_LONG);
//            if (paths.size() <= 1) {
//                // 最后一张照片弹出删除提示
//                // show confirm dialog
//                new AlertDialog.Builder(this)
//                        .setTitle(R.string.confirm_to_delete)
//                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                                paths.remove(index);
//                                onBackPressed();
//                            }
//                        })
//                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .show();
//            } else {
//                snackbar.show();
//                paths.remove(index);
//                mPagerAdapter.notifyDataSetChanged();
//            }

//            snackbar.setAction(R.string.undo, new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (paths.size() > 0) {
//                        paths.add(index, deletedPath);
//                    } else {
//                        paths.add(deletedPath);
//                    }
//                    mPagerAdapter.notifyDataSetChanged();
//                    mViewPager.setCurrentItem(index, true);
//                }
//            });
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
