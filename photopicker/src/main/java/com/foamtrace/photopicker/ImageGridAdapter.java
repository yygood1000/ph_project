package com.foamtrace.photopicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ͼƬAdapter
 * Created by Nereo on 2015/4/7.
 */
public class ImageGridAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;
    private boolean isDriver;

    private LayoutInflater mInflater;
    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private List<ImageModel> mImageModels = new ArrayList<>();
    private List<String> mSelectedImageModels = new ArrayList<>();

    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;
    private onItemBtnClickListener mListener;

    public ImageGridAdapter(Context context, boolean showCamera, int itemSize, boolean isDriver, onItemBtnClickListener listener) {
        this.isDriver = isDriver;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
        this.mItemSize = itemSize;
        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);
        this.mListener = listener;
    }

    public interface onItemBtnClickListener {
        void onSelectClick(int position, ImageModel imageModel); // 选中
    }

    /**
     * 显示选择指示器
     *
     * @param b
     */
    public void showSelectIndicator(boolean b) {
        showSelectIndicator = b;
    }

    public void setShowCamera(boolean b) {
        if (showCamera == b) return;

        showCamera = b;
        notifyDataSetChanged();
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    /**
     * 选择某个图片，改变选择状态
     */
    public void select(String path) {
        if (mSelectedImageModels.contains(path)) {
            mSelectedImageModels.remove(path);
        } else {
            mSelectedImageModels.add(path);
        }
        notifyDataSetChanged();
    }

    /**
     * 通过图片路径设置默认选择
     */
    public void setDefaultSelected(ArrayList<ImageModel> resultList) {
        mSelectedImageModels.clear();
        for (ImageModel im : resultList) {
            mSelectedImageModels.add(im.getPath());
        }
        notifyDataSetChanged();
    }

    private ImageModel getImageByPath(String path) {
        if (mImageModels != null && mImageModels.size() > 0) {
            for (ImageModel imageModel : mImageModels) {
                if (imageModel.path.equalsIgnoreCase(path)) {
                    return imageModel;
                }
            }
        }
        return null;
    }

    /**
     * 设置数据集
     *
     * @param imageModels
     */
    public void setData(List<ImageModel> imageModels) {
        mSelectedImageModels.clear();

        if (imageModels != null && imageModels.size() > 0) {
            mImageModels = imageModels;
        } else {
            mImageModels.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 重置每个Column的Size
     *
     * @param columnWidth
     */
    public void setItemSize(int columnWidth) {

        if (mItemSize == columnWidth) {
            return;
        }

        mItemSize = columnWidth;

        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);

        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera) {
            return position == 0 ? TYPE_CAMERA : TYPE_NORMAL;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return showCamera ? mImageModels.size() + 1 : mImageModels.size();
    }

    @Override
    public ImageModel getItem(int i) {
        if (showCamera) {
            if (i == 0) {
                return null;
            }
            return mImageModels.get(i - 1);
        } else {
            return mImageModels.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        int type = getItemViewType(position);
        if (type == TYPE_CAMERA) {
            view = mInflater.inflate(R.layout.item_camera, viewGroup, false);
            view.setTag(null);
        } else if (type == TYPE_NORMAL) {
            ViewHolde holde;
            if (view == null) {
                view = mInflater.inflate(R.layout.item_select_image, viewGroup, false);
                holde = new ViewHolde(view);
            } else {
                holde = (ViewHolde) view.getTag();
                if (holde == null) {
                    view = mInflater.inflate(R.layout.item_select_image, viewGroup, false);
                    holde = new ViewHolde(view);
                }
            }

            holde.bindData(getItem(position));
            holde.setOnIndecatorClickListener(getItem(position), position);
        }

        /** Fixed View Size */
        GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
        if (lp.height != mItemSize) {
            view.setLayoutParams(mItemLayoutParams);
        }

        return view;
    }

    class ViewHolde {
        ImageView image;
        ImageView indicator;
        LinearLayout ll_checkmark;

        ViewHolde(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            indicator = (ImageView) view.findViewById(R.id.checkmark);
            ll_checkmark = (LinearLayout) view.findViewById(R.id.ll_checkmark);
            view.setTag(this);
        }


        void setOnIndecatorClickListener(final ImageModel imageModel, final int position) {
            ll_checkmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSelectClick(position, imageModel);
                }
            });
        }

        void bindData(final ImageModel data) {
            if (data == null) return;
            // 处理单选和多选状态
            if (showSelectIndicator) {
                indicator.setVisibility(View.VISIBLE);
                if (mSelectedImageModels.contains(data.getPath())) {
                    // 设置选中状态
                    if (isDriver) {
                        indicator.setImageResource(R.mipmap.v3_checked_order_list);
                    } else {
                        indicator.setImageResource(R.mipmap.v4_is_anonymity_true);
                    }
                } else {// 未选择
                    indicator.setImageResource(R.mipmap.v4_is_anonymity_false);
                }

            } else {
                indicator.setVisibility(View.GONE);
            }
            File imageFile = new File(data.path);

            if (mItemSize > 0) {
                // 显示图片
                Glide.with(mContext)
                        .load(imageFile)
                        .placeholder(R.mipmap.default_error)
                        .error(R.mipmap.default_error)
                        .override(mItemSize, mItemSize)
                        .centerCrop()
                        .into(image);
            }
        }
    }

}
