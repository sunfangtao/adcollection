package com.sft.adcollection.common;

import java.io.File;

/**
 * 项目 adcollection
 * Created by SunFangtao on 2016/8/16.
 */
public class Constant {

    public static String IPPORT = "http://123.130.125.47:80";

    public static final class FilePath {
        // 采集视频路径
        public static final String COLLECTION_VIDEO_PATH = "collect" + File.separator + "video" + File.separator;
        // 采集图片路径
        public static final String COLLECTION_PHOTO_PATH = "collect" + File.separator + "photo" + File.separator;
        // 采集版面图片路径
        public static final String COLLECTION_PAGE_PHOTO_PATH = "collect" + File.separator + "photo" + File.separator + "page" + File.separator;
        // 检测图片路径
        public static final String COLLECTION_SUPERVISE_PHOTO_PATH = "supervise" + File.separator + "photo" + File.separator;
        // 处理图片路径
        public static final String COLLECTION_HANDLE_PHOTO_PATH = "handle" + File.separator + "photo" + File.separator;
    }

    public static final class HTTPUrl {
        // 登录
        public static final String LOGIN_URL = IPPORT + "/a/login";
        // 广告
        public static final String MEDIA_AROUND_URL = IPPORT + "/a/mobile/mobile/advertLocation";
        // 广告详情
        public static final String MEDIA_AROUND_DETAIL_URL = IPPORT + "/a/mobile/mobile/getMobileMtzl";
        // 获取媒体资料图片
        public static final String MEDIA_IMAGE_URL = IPPORT + "/a/mobile/mobile/getMobileBmImage";
        // 监测
        public static final String MEDIA_SUPERVISE_URL = IPPORT + "/a/mobile/mobile/advertLocationJcbg";
        // 监测详情
        public static final String MEDIA_SUPERVISE_DETAIL_URL = IPPORT + "/a/mobile/mobile/getMobileJcbg";
        // 监测详情版面
        public static final String MEDIA_SUPERVISE_DETAIL_PAGE_URL = IPPORT + "/a/mobile/mobile/getJcbgBm";
        // 处理
        public static final String MEDIA_HANDLE_URL = IPPORT + "/a/mobile/mobile/getDoCase";
        // 查询采购商、供应商
        public static final String CONSUMER_URL = IPPORT + "/a/mobile/mobile/getKehu";
        // 查询广告类别
        public static final String AD_TYPE_URL = IPPORT + "/a/mobile/mobile/getMobileGglb";
        // 法律法规分类
        public static final String LAW_TYPE_URL = IPPORT + "/a/mobile/mobile/getMobileFlfgfl";
        // 法律法规分类的id获取具体法律法规具体信息
        public static final String LAW_TYPE_DETAIL_URL = IPPORT + "/a/mobile/mobile/getMobileFlfg";
        // 更新监测版面
        public static final String UPDATE_SUPERVISE_PAGE_URL = IPPORT + "/a/mobile/mobile/saveJcbgBm";
        // 画面分类
        public static final String DAMAGED_CON_RUL = IPPORT + "/a/mobile/mobile/getMobileHmfl";
        // 获取媒体形式
        public static final String MEDIA_STYLE_URL = IPPORT + "/a/mobile/mobile/getMtxs";
        // 获取媒体类别
        public static final String MEDIA_TYPE_URL = IPPORT + "/a/mobile/mobile/getMtfl";
        // 上传采集的新媒体
        public static final String UPLOAD_NEW_MEDIA_URL = IPPORT + "/a/mobile/mobile/mtzlMobileSave";
        // 上传采集的新媒体版面
        public static final String UPLOAD_NEW_PAGE_URL = IPPORT + "/a/mobile/mobile/saveJcbgMobileBm";
        // 获取当前用户地域权限等级
        public static final String USER_PERMISSION_LEVEL_URL = IPPORT + "/a/mobile/mobile/loginfindgrade";
        // 获取当前用户的子类部门列表
        public static final String USER_PERMISSION_AREA_URL = IPPORT + "/a/mobile/mobile/getarea";
        // 获取违法案件详情
        public static final String HANDLE_DETAIL_URL = IPPORT + "/a/mobile/mobile/getAjjbDetails";
        // 上传处理案件的图片
        public static final String UPLOAD_HANDLE_PHOTO_URL = IPPORT + "/a/mobile/mobile/saveDoCase";

    }
}
