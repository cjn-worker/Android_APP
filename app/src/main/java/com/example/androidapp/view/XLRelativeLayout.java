package com.example.androidapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.example.androidapp.Constant.Constant;
import com.example.androidapp.LinkGame.LinkModel.LinkInfo;
import com.example.androidapp.LinkGame.LinkModel.Point;
import com.example.androidapp.LinkGame.Utils.LinkUtils;

import java.util.List;


public class XLRelativeLayout extends RelativeLayout {
    //点的信息
    private LinkInfo linkInfo;

    //画笔
    //private Paint paint;
    private CustomPaint customPaint;

    public XLRelativeLayout(Context context) {
        super(context);
        //让onDraw方法执行
        setWillNotDraw(false);
    }
    public XLRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        //让onDraw方法执行
        setWillNotDraw(false);
    }

    //setter getter
    public LinkInfo getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(LinkInfo linkInfo) {
        this.linkInfo = linkInfo;

        //初始化画笔
        customPaint = new CustomPaint();

        invalidate();
    }

    //重绘
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //linkInfo不为空
        if (linkInfo != null){

            //获取点数据
            List<Point> points = linkInfo.getPointsList();

            //判断linkInfo是否有数据
            if (points.size() != 0)
            {
                //画路径
                for (int i = 0; i < points.size() - 1; i++)
                {
                    //转换坐标
                    Point realPoint1 = LinkUtils.getRealAnimalPoint(
                            points.get(i),
                            getContext()
                    );
                    Point realPoint2 = LinkUtils.getRealAnimalPoint(
                            points.get(i + 1),
                            getContext()
                    );

                    //画闪电
                    customPaint.drawLightning(realPoint1.getX(), realPoint1.getY(), realPoint2.getX(), realPoint2.getY(), 5, canvas);
                    customPaint.drawLightningBold(realPoint1.getX(), realPoint1.getY(), realPoint2.getX(), realPoint2.getY(), 5, canvas);

                }
            }
        }
    }

}
