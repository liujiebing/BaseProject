package com.droideek.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.xg.platform.R;

/**
 * Created by Droideek on 2015/12/28.
 */
public class ImageUtil {

    private static final String TAG = "ImageUtil";

    public static void init(Context context) {
        ProgressiveJpegConfig config1 = new SimpleProgressiveJpegConfig();
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                .setProgressiveJpegConfig(config1)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setMemoryTrimmableRegistry(new MemoryTrimmableRegistry() {

                    @Override
                    public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
                        trimmable.trim(MemoryTrimType.OnSystemLowMemoryWhileAppInBackground);
                    }

                    @Override
                    public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {
                    }
                })
                .build();
        Fresco.initialize(context, imagePipelineConfig);
    }


    public static void displayImage(String url, SimpleDraweeView imageView) {

        displayImage(url, imageView, 0);
    }


    public static void displayImage(int resId, ImageView imageView, int defRes) {
        displayImage("res:///" + resId, imageView, defRes);
    }

    public static void displayImage(int resId, ImageView imageView) {
        displayImage(resId, imageView, 0);
    }

    public static void displayImage(final String url, ImageView imageView, int def) {
        displayImage(url, imageView, def, null);
    }


    public static void displayImage(final String url, ImageView imageView, int def, final ImgControllerListener listener) {
        if (null == imageView) {
            return;
        }

        if (imageView instanceof SimpleDraweeView) {

            if (!TextUtils.isEmpty(url) && (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file://") || url.startsWith("res://"))) {
                Uri uri = Uri.parse(url);
                final SimpleDraweeView draweeView = (SimpleDraweeView) imageView;
                setDefault(draweeView, def);

                if (null != uri) {
                    try {
                        //draweeView.setImageURI(uri); //todo delete 2016/4/11 基本用法

                        if (isNotEqualsUriPath(draweeView, uri.toString())) {

                            ImageRequest request = ImageRequestBuilder
                                    .newBuilderWithSource(uri)
                                    .setProgressiveRenderingEnabled(true)
                                    .build();

                            DraweeController controller = Fresco.newDraweeControllerBuilder()
                                    .setUri(uri)
                                    .setImageRequest(request)
                                    .setOldController(draweeView.getController())
                                    .setAutoPlayAnimations(true)
                                    .setControllerListener(new BaseControllerListener() {
                                        @Override
                                        public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                                            super.onFinalImageSet(id, imageInfo, animatable);
                                            draweeView.setTag(R.id.action_bar, url);
                                            if (null != listener) {
                                                listener.onSuccessImageSet(url, imageInfo, animatable);
                                            }
                                        }
                                    })
                                    .build();

                            draweeView.setController(controller);
                        }

                    } catch (Exception e) {

                    }
                }
            }else{
                setDefault((SimpleDraweeView)imageView, def);
            }
        }


//        if (!isUrlNormal) {
//            if (0 != dafault) {
//                imageView.setImageResource(dafault);
//            }
//        }


    }

    public interface ImgControllerListener {
        void onSuccessImageSet(String url, Object imageInfo, Animatable animatable);
    }


    public static boolean isNotEqualsUriPath(SimpleDraweeView draweeView, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            return false;
        }
        return !(draweeView.getTag(R.id.action_bar) + "").equals(imgUrl);
    }

    public static void setDefault(SimpleDraweeView draweeView, int dafault) {
        if (0 != dafault) {
            try {
                draweeView.getHierarchy().setPlaceholderImage(dafault);
            } catch (Exception e) {

            }
        }
    }

    public static void setDefault(SimpleDraweeView draweeView, Drawable defaultDrawable, ScalingUtils.ScaleType scaleType) {
        if (null !=defaultDrawable) {
            try {
                draweeView.getHierarchy().setPlaceholderImage(defaultDrawable, scaleType);
            } catch (Exception e) {

            }
        }
    }

    public static void clearMemoryCaches() {
        ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
    }

    public static void clearCaches() {
        ImagePipelineFactory.getInstance().getImagePipeline().clearDiskCaches();
    }

    public static void getBitmapByUriInMain(String uriStr, BaseBitmapDataSubscriber bitmapDataSubscriber) {

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(uriStr))
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, new Object());

        dataSource.subscribe(bitmapDataSubscriber,
                UiThreadImmediateExecutorService.getInstance());

        //dataSource.close();
    }

    public static void getBitmapByUri(String uriStr, BaseBitmapDataSubscriber bitmapDataSubscriber) {

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(uriStr))
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, new Object());

        dataSource.subscribe(bitmapDataSubscriber,
                CallerThreadExecutor.getInstance());

        //dataSource.close();
    }

    public static void getBitmapCache(String uriStr, BaseBitmapDataSubscriber bitmapDataSubscriber) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(uriStr))
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        DataSource<CloseableReference<CloseableImage>> dataSource =
                imagePipeline.fetchImageFromBitmapCache(imageRequest, new Object());
        dataSource.subscribe(bitmapDataSubscriber, CallerThreadExecutor.getInstance());

        try {
            dataSource.close();
        } catch (Exception e) {
            //dataSource关闭异常

        }
    }

    /**
     * 从Bitmap缓存中立刻取到结果
     * @param uriStr
     */
    public static void getBitmapCache(String uriStr) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(uriStr))
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        DataSource<CloseableReference<CloseableImage>> dataSource =
                imagePipeline.fetchImageFromBitmapCache(imageRequest, new Object());
        try {
            CloseableReference<CloseableImage> imageReference = dataSource.getResult();
            if (imageReference != null) {
                try {
                    // Do something with the image, but do not keep the reference to it!
                    // The image may get recycled as soon as the reference gets closed below.
                    // If you need to keep a reference to the image, read the following sections.

                } finally {
                    CloseableReference.closeSafely(imageReference);
                }
            } else {
                // cache miss
            }
        } finally {
            dataSource.close();
        }
    }

    public static void setScaleType(SimpleDraweeView imageView, ScalingUtils.ScaleType scaleType) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        GenericDraweeHierarchy hierarchy =null;
        try {
            hierarchy = imageView.getHierarchy();
        } catch (Exception e) {
            //error
        }

        if (null != hierarchy) {
            hierarchy.setActualImageScaleType(scaleType);
        }
    }
}
