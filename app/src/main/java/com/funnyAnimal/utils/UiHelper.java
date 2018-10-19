package com.funnyAnimal.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.funnyAnimal.R;
import com.funnyAnimal.activity.MyApplication;
import com.funnyAnimal.api.UserInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bhyan on 2017/6/16.
 */

public class UiHelper {
    /**
     * 检验是否符合基本json数据格式
     *
     * @param str
     * @return
     */
    public static boolean isJson(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        return str.matches("^\\{.*");
    }

    /**
     * 检验是否符合json数组格式
     *
     * @param str
     * @return
     */
    public static boolean isJsonArray(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        return str.matches("^\\[.*");
    }

    private static Toast toast = null;

    public static void showToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //日期转换
    public static String time2Date(String timeStamp) {
        if (TextUtils.isEmpty(timeStamp)) {
            return "";
        }
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    public static void setRefreshing(final SwipeRefreshLayout mSwipeRefreshLayout, final boolean isRefresh) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(isRefresh);
                }
            }, 50L);
        }
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {

        int w = bitmap.getWidth();

        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();

        float scaleWidth = ((float) width / w);

        float scaleHeight = ((float) height / h);

        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出

        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

        return newbmp;
    }

    public static boolean checkPhone(String mobiles) {
        Pattern p = Pattern
                .compile("^1[34578]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static byte[] bitmapTobyte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 根据byte数组，生成文件
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

//    public static void saveUserInfo(UserInfo userInfo) {
//        MyApplication.accessToken = userInfo.accessToken;
//        PreferenceUtil.setUserUuid(userInfo.accessToken);
//        PreferenceUtil.setUserName(userInfo.name);
//        PreferenceUtil.setUserGender(userInfo.gender);
//        PreferenceUtil.setUserBimapUrl(userInfo.iconurl);
//        PreferenceUtil.setUserCity(userInfo.city);
//        PreferenceUtil.setUserProvince(userInfo.province);
//        PreferenceUtil.setUserPhone(userInfo.userPhone);
//        PreferenceUtil.setUserMoto(userInfo.moto);
//        PreferenceUtil.setUserUid(userInfo.uid);
//    }

    public static void destroyUserInfo() {
        MyApplication.accessToken = "";
        PreferenceUtil.setUserUuid("");
        PreferenceUtil.setUserName("火星人");
        PreferenceUtil.setUserGender("");
        PreferenceUtil.setUserBimapUrl("");
        PreferenceUtil.setUserCity("");
        PreferenceUtil.setUserProvince("");
        PreferenceUtil.setUserPhone("");
        PreferenceUtil.setUserMoto("您还没有登录");
        PreferenceUtil.setUserUid("");
    }

    //没错 这就是MD5加盐加密
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string + "AA452103FA44E76027F36F814A6DA98E81D230B8B014").getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static DisplayImageOptions getHomeBackOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.empty_img) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.empty_img)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.empty_img) // 设置图片加载/解码过程中错误时候显示的图片
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true).cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//                .displayer(new SimpleBitmapDisplayer())// 是否图片加载好后渐入的动画时间
                .build();// 构建完成
        return options;
    }

    public static void saveUserInfo(Map<String, String> data) {
        AccountManager.setName(data.get("name"));
        AccountManager.setAccesstoken(data.get("access_token"));
        AccountManager.setGender(data.get("gender"));
        AccountManager.setIconurl(data.get("iconurl"));
        AccountManager.setCity(data.get("city"));
        AccountManager.setProvince(data.get("province"));
    }

    public static void saveUserInfo(UserInfo userInfo){
        AccountManager.setName(userInfo.name);
        AccountManager.setAccesstoken(userInfo.accessToken);
        AccountManager.setGender(userInfo.gender);
        AccountManager.setIconurl(userInfo.iconurl);
        AccountManager.setCity(userInfo.city);
        AccountManager.setProvince(userInfo.province);
    }

    public static void cleanUserInfo() {
        AccountManager.setName("");
        AccountManager.setAccesstoken("");
        AccountManager.setGender("");
        AccountManager.setIconurl("");
        AccountManager.setCity("");
        AccountManager.setProvince("");
    }
}
