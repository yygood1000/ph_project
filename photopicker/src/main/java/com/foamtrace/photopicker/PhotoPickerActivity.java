package com.foamtrace.photopicker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class PhotoPickerActivity extends AppCompatActivity {

    public static final String TAG = PhotoPickerActivity.class.getName();

    private Context mContext;
    /**
     * 图片选择模式，int类型
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;
    /**
     * 最大图片选择次数，int类型
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 默认最大照片数量
     */
    public static final int DEFAULT_MAX_TOTAL = 4;
    /**
     * 是否显示相机，boolean类型
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 默认选择的数据集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /**
     * 筛选照片配置信息
     */
    public static final String EXTRA_IMAGE_CONFIG = "image_config";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";
    /**
     * 是否是司机
     */
    public static final String EXTRA_IS_DRIVER = "is_driver";

    // 所有的照片数据集合
    private ArrayList<ImageModel> imageModels;
    // 结果数据
    private ArrayList<ImageModel> resultList = new ArrayList<>();
    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;

    //    private MenuItem menuDoneItem;
    private GridView mGridView;
    private View mPopupAnchorView;

    // 最大照片数量
    private ImageCaptureManager captureManager;
    private int mDesireImageCount;
    private ImageConfig imageConfig; // 照片配置

    private ImageGridAdapter mImageSelectAdapter;
    private FolderAdapter mFolderAdapter;
    private ListPopupWindow mFolderPopupWindow;

    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;
    private boolean isDriver = false;
    private int mode;// 图片选择模式
    private TextView tv_confirm;
    private ToasterManager toasterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopicker);
        mContext = this;
        getIntentData();
        initData();
        initViews();
        toasterManager = new ToasterManager(mContext);

//        // 打开相册列表
//        btnAlbum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mFolderPopupWindow == null) {
//                    createPopupFolderList();
//                }
//
//                if (mFolderPopupWindow.isShowing()) {
//                    mFolderPopupWindow.dismiss();
//                } else {
//                    mFolderPopupWindow.show();
//                    int index = mFolderAdapter.getSelectIndex();
//                    index = index == 0 ? index : index - 1;
//                    mFolderPopupWindow.getListView().setSelection(index);
//                }
//            }
//        });

    }

    /**
     * 获取Intent中的数据
     */
    private void getIntentData() {
        /**
         * 获取上级页面传入的照片属性
         */
        imageConfig = getIntent().getParcelableExtra(EXTRA_IMAGE_CONFIG);

        /**
         *选择图片数量
         */
        mDesireImageCount = getIntent().getIntExtra(EXTRA_SELECT_COUNT, DEFAULT_MAX_TOTAL);

        /**
         *图片选择模式
         */
        mode = getIntent().getExtras().getInt(EXTRA_SELECT_MODE, MODE_SINGLE);

        /**
         * 是否是司机
         */
        isDriver = getIntent().getExtras().getBoolean(EXTRA_IS_DRIVER, false);

        /**
         *是否显示照相机
         */
        mIsShowCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, false);

        /**
         * 添加默认选择的照片，在图片加载器加载完成时进行默认选择的设置
         */
        if (mode == MODE_MULTI) {
            ArrayList<ImageModel> tmp = (ArrayList<ImageModel>) getIntent().getSerializableExtra(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                resultList.addAll(tmp);
            }
        }

    }

    /**
     * 加载相册图片
     */
    private void initData() {
        /**
         *定义图片加载器（首次加载所有图片）
         */
        getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    private void initViews() {
        captureManager = new ImageCaptureManager(mContext);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.pickerToolbar);
        TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);

        tv_cancel.setOnClickListener(onClickListener);
        tv_confirm.setOnClickListener(onClickListener);

//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(getResources().getString(R.string.image));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setNumColumns(getNumColnums());

        mImageSelectAdapter = new ImageGridAdapter(mContext, mIsShowCamera, getItemImageWidth(), isDriver, onSelectBtnClickListener);
        // 是否显示选择指示器
        mImageSelectAdapter.showSelectIndicator(mode == MODE_MULTI);
        mGridView.setAdapter(mImageSelectAdapter);
        mGridView.setOnItemClickListener(onItemClickListener);

        if (isDriver) {
            tv_confirm.setTextColor(getResources().getColor(R.color.v3_common_blue));
        } else {
            tv_confirm.setTextColor(getResources().getColor(R.color.v3_common_green));
        }
        refreshActionStatus();

//        mFolderAdapter = new FolderAdapter(mContext);
//        mPopupAnchorView = findViewById(R.id.photo_picker_footer);
//        btnAlbum = (Button) findViewById(R.id.btnAlbum);
//        btnPreview = (Button) findViewById(R.id.btnPreview);
    }

    /**
     * 获取GridView Item宽度
     */
    private int getItemImageWidth() {
        int cols = getNumColnums();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }

    /**
     * 根据屏幕宽度与密度计算GridView显示的列数， 最少为4列
     */
    private int getNumColnums() {
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        return cols < 4 ? 4 : cols;
    }

    /**
     * 勾选按钮点击事件
     */
    ImageGridAdapter.onItemBtnClickListener onSelectBtnClickListener = new ImageGridAdapter.onItemBtnClickListener() {
        @Override
        public void onSelectClick(int position, ImageModel imageModel) {
            // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
            if (position == 0) {
                if (mode == MODE_MULTI) {
                    // 判断选择数量问题
                    if (mDesireImageCount == resultList.size()) {
                        showToast();
                        return;
                    }
                }
                showCameraAction();
            } else {
                // 选中操作
                selectImageFromGrid(imageModel, mode);
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_cancel) {
                finish();
                return;
            }

            if (id == R.id.tv_confirm) {
                onFinish();
            }
        }
    };

    /**
     * 选择列表点击事件
     */
    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 如果显示照相机，则第一个Grid显示为照相机，点击第一项跳转到照相机进行拍照。
            if (mImageSelectAdapter.isShowCamera()) {
                if (position == 0) {
                    if (mode == MODE_MULTI) {
                        // 判断选择数量问题
                        if (mDesireImageCount == resultList.size()) {
                            showToast();
                            return;
                        }
                    }
                    showCameraAction();
                } else {
                    // TODO 进入所有图片预览界面
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(mContext);
                    intent.setCurrentItem(position - 1);// 设置显示第几张
                    intent.setIsPreviewSelectedImages(false);
                    ArrayList<ImageModel> previewAllImagePaths = new ArrayList<>();// 创建所有图片路径集合，用于预览。
                    for (int i = 0; i < imageModels.size(); i++) {
                        previewAllImagePaths.add(imageModels.get(i));
                    }
                    intent.setPhotoPaths(previewAllImagePaths);
                    startActivityForResult(intent, PhotoPreviewActivity.REQUEST_PREVIEW);
                }
            } else {
                // 选中操作
                ImageModel imageModel = (ImageModel) parent.getAdapter().getItem(position);
                selectImageFromGrid(imageModel, mode);
            }
        }
    };

    /**
     * 打开相机拍照
     */
    private void showCameraAction() {
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(mContext, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 选择图片操作
     */
    private void selectImageFromGrid(ImageModel imageModel, int mode) {
        if (imageModel != null) {
            // 多选模式
            if (mode == MODE_MULTI) {
                if (resultList.contains(imageModel)) {
                    resultList.remove(imageModel);// 删除操作
                } else {
                    // 判断选择数量问题
                    if (mDesireImageCount == resultList.size()) {
                        showToast();
                        return;
                    }
                    resultList.add(imageModel);
//                    onImageSelected(imageModel);// 新增操作
                }
                // 刷新图片选择指示器
                refreshActionStatus();
                // 刷新列表选中的标记
                mImageSelectAdapter.select(imageModel.getPath());
            } else if (mode == MODE_SINGLE) {
                // 单选模式
                onSingleImageSelected(imageModel.path);
            }
        }
    }

    /**
     * 单选操作
     */
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(new ImageModel(path));
        data.putExtra(EXTRA_RESULT, (Serializable) resultList);
        setResult(RESULT_OK, data);
        finish();
    }

//    /**
//     * 多选操作
//     */
//    public void onImageSelected(ImageModel path) {
//        for (int i = 0; i < resultList.size(); i++) {
//            if (resultList.get(i).getPath().equals(path.getPath())) {// 新增的图片路径在集合中已经存在。
//                return;
//            }
//
//            if (i == resultList.size() - 1) {
//                resultList.add(path);
//            }
//        }
//
//    }

    public void showToast() {
        toasterManager.showToast("您已经选择了4张照片");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相机拍照完成后，返回图片路径
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    // 将照片添加进已选集合
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        resultList.add(new ImageModel(captureManager.getCurrentPhotoPath()));
                    }
                    initData();// 从新加载相册
                    refreshActionStatus();
                    break;
                // 预览照片
                case PhotoPreviewActivity.REQUEST_PREVIEW:
                    ArrayList<String> pathArr = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    // 刷新页面
                    if (pathArr != null && pathArr.size() != resultList.size()) {
                        resultList = (ArrayList<ImageModel>) data.getSerializableExtra(PhotoPreviewActivity.EXTRA_RESULT);
                        refreshActionStatus();
                        mImageSelectAdapter.setDefaultSelected(resultList);
                    }
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "on change");

        // 重置列数
        mGridView.setNumColumns(getNumColnums());
        // 重置Item宽度
        mImageSelectAdapter.setItemSize(getItemImageWidth());

//        if (mFolderPopupWindow != null) {
//            if (mFolderPopupWindow.isShowing()) {
//                mFolderPopupWindow.dismiss();
//            }
//
//            // 重置PopupWindow高度
//            int screenHeigh = getResources().getDisplayMetrics().heightPixels;
//            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
//        }

        super.onConfigurationChanged(newConfig);
    }

    /**
     * 刷新操作按钮状态
     */
    private void refreshActionStatus() {
        String text = getString(R.string.done_with_count, resultList.size(), mDesireImageCount);
        tv_confirm.setText(text);
    }

    /**
     * 读取手机照片回调
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            // 根据图片设置参数新增验证条件
            StringBuilder selectionArgs = new StringBuilder();

            if (imageConfig != null) {
                if (imageConfig.minWidth != 0) {
                    selectionArgs.append(MediaStore.Images.Media.WIDTH + " >= " + imageConfig.minWidth);
                }

                if (imageConfig.minHeight != 0) {
                    selectionArgs.append("".equals(selectionArgs.toString()) ? "" : " and ");
                    selectionArgs.append(MediaStore.Images.Media.HEIGHT + " >= " + imageConfig.minHeight);
                }

                if (imageConfig.minSize != 0f) {
                    selectionArgs.append("".equals(selectionArgs.toString()) ? "" : " and ");
                    selectionArgs.append(MediaStore.Images.Media.SIZE + " >= " + imageConfig.minSize);
                }

                if (imageConfig.mimeType != null) {
                    selectionArgs.append(" and (");
                    for (int i = 0, len = imageConfig.mimeType.length; i < len; i++) {
                        if (i != 0) {
                            selectionArgs.append(" or ");
                        }
                        selectionArgs.append(MediaStore.Images.Media.MIME_TYPE + " = '" + imageConfig.mimeType[i] + "'");
                    }
                    selectionArgs.append(")");
                }
            }

            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        selectionArgs.toString(), null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                String selectionStr = selectionArgs.toString();
                if (!"".equals(selectionStr)) {
                    selectionStr += " and" + selectionStr;
                }
                CursorLoader cursorLoader = new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'" + selectionStr, null,
                        IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                imageModels = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));

                        ImageModel imageModel = new ImageModel(path, name, dateTime);
                        imageModels.add(imageModel);
//                        if (!hasFolderGened) {
//                            // 获取文件夹名称
//                            File imageFile = new File(path);
//                            File folderFile = imageFile.getParentFile();
//                            Folder folder = new Folder();
//                            folder.name = folderFile.getName();
//                            folder.path = folderFile.getAbsolutePath();
//                            folder.cover = imageModel;
//                            if (!mResultFolder.contains(folder)) {
//                                List<ImageModel> imageModelList = new ArrayList<>();
//                                imageModelList.add(imageModel);
//                                folder.imageModels = imageModelList;
//                                mResultFolder.add(folder);
//                            } else {
//                                // 更新
//                                Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
//                                f.imageModels.add(imageModel);
//                            }
//                        }

                    } while (data.moveToNext());

                    // 给适配器添加数据
                    mImageSelectAdapter.setData(imageModels);

                    // 设定默认选择
                    if (resultList != null && resultList.size() > 0) {
                        mImageSelectAdapter.setDefaultSelected(resultList);
                    }
//                    mFolderAdapter.setData(mResultFolder);
//                    hasFolderGened = true;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };

    /**
     * 返回已选择的图片数据
     */
    private void onFinish() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, (Serializable) resultList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }


    /**
     * 创建文件夹选择列表
     */
    //    private void createPopupFolderList() {
//
//        mFolderPopupWindow = new ListPopupWindow(mContext);
//        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        mFolderPopupWindow.setAdapter(mFolderAdapter);
//        mFolderPopupWindow.setContentWidth(ListPopupWindow.MATCH_PARENT);
//        mFolderPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
//
//        // 计算ListPopupWindow内容的高度(忽略mPopupAnchorView.height)，R.layout.item_foloer
//        int folderItemViewHeight =
//                // 图片高度
//                getResources().getDimensionPixelOffset(R.dimen.folder_cover_size) +
//                        // Padding Top
//                        getResources().getDimensionPixelOffset(R.dimen.folder_padding) +
//                        // Padding Bottom
//                        getResources().getDimensionPixelOffset(R.dimen.folder_padding);
//        int folderViewHeight = mFolderAdapter.getCount() * folderItemViewHeight;
//
//        int screenHeigh = getResources().getDisplayMetrics().heightPixels;
//        if (folderViewHeight >= screenHeigh) {
//            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
//        } else {
//            mFolderPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
//        }
//
//        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
//        mFolderPopupWindow.setModal(true);
//        mFolderPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
//        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                mFolderAdapter.setSelectIndex(position);
//
//                final int index = position;
//                final AdapterView v = parent;
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mFolderPopupWindow.dismiss();
//
//                        if (index == 0) {
//                            getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
//                            btnAlbum.setText(R.string.all_image);
//                            mImageSelectAdapter.setShowCamera(mIsShowCamera);
//                        } else {
//                            Folder folder = (Folder) v.getAdapter().getItem(index);
//                            if (null != folder) {
//                                mImageSelectAdapter.setData(folder.imageModels);
//                                btnAlbum.setText(folder.name);
//                                // 设定默认选择
//                                if (resultList != null && resultList.size() > 0) {
//                                    mImageSelectAdapter.setDefaultSelected(resultList);
//                                }
//                            }
//                            mImageSelectAdapter.setShowCamera(false);
//                        }
//
//                        // 滑动到最初始位置
//                        mGridView.smoothScrollToPosition(0);
//                    }
//                }, 100);
//            }
//        });
//    }

}
