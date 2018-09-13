package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by ZSpirytus on 2018/9/5.
 */

public class DialogBuilder {

    public static AlertDialog.Builder showSelectDialog(Context context) {
        final String[] items = new String[]{"音乐信息", "获取专辑封面", "获取歌词"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder;
    }
}
