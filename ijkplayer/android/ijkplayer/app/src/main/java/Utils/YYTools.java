package Utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class YYTools {
    public static void copyStrTo(Context mContext, String str) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", str);
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }
}
