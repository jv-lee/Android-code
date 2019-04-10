package com.lee.code.uidraw.widget.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.graphics.PathParser;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lee.code.uidraw.R;
import com.lee.code.uidraw.utils.ThreadPoolManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @author jv.lee
 * @date 2019/4/10
 */
public class MapView extends View {
    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5,0xFF80CBF1, 0xFFFFFFFF};

    private Context context;
    /**
     * 从svg xml中解析出来所有的path绘制图 存放的list
     */
    private List<ProviceItem> itemList;
    private Paint paint;

    /**
     * 当前选中的省份地区
     */
    private ProviceItem select;

    /**
     * 整张svg图最大的矩形范围
     * @param context
     */
    private RectF totalRect;
    private float scale = 1.0f;

    public MapView(Context context) {
        this(context,null);
    }

    public MapView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
            init(context);
    }

    private void init(Context context)  {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        itemList = new ArrayList<>();
        ThreadPoolManager.getInstance().addTask(loadThread);
    }

    private Runnable loadThread = new Runnable() {
        @Override
        public void run() {
            try {
                //解析svg的xml
                InputStream inputStream = context.getResources().openRawResource(R.raw.china);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = null;
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(inputStream);
                Element rootElement = doc.getDocumentElement();
                NodeList items = rootElement.getElementsByTagName("path");

                float left = -1;
                float right = -1;
                float top = -1;
                float bottom = -1;

                List<ProviceItem> list = new ArrayList<>();
                //遍历所有的path标签  获取android:pathData路径中的字符串 创建一个Path路径
                for (int i = 0; i < items.getLength(); i++) {
                    Element element = (Element) items.item(i);
                    String pathData = element.getAttribute("android:pathData");
                    //谷歌官方实现的 svg路径转path类
                    @SuppressLint("RestrictedApi") Path path = PathParser.createPathFromPathData(pathData);
                    ProviceItem proviceItem = new ProviceItem(path);
                    //随机设置颜色
                    proviceItem.setDrawColor(colorArray[i%4]);
                    RectF rectF = new RectF();
                    //首先获得当前path最大值 再去获取上下左右最大值
                    path.computeBounds(rectF,true);
                    //求出最左边的left位置 求所有矩形的左边最小值 right反之
                    left = left == -1 ? rectF.left : Math.min(left, rectF.left);
                    right = right == -1 ? rectF.right : Math.max(right, rectF.right);
                    top = top == -1 ? rectF.top : Math.min(top, rectF.top);
                    bottom = bottom == -1 ? rectF.bottom : Math.max(bottom, rectF.bottom);
                    list.add(proviceItem);
                }
                //尽量避免在读写操作中 做成员变量的add 写入操作 所以创建一个list读写 在所有数据记录成功后 再把list赋值给成员变量list
                itemList = list;
                //绘制最大的矩形®
                totalRect = new RectF(left, top, right, bottom);
                //异步回调刷新
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //重新设置view的宽高
                        requestLayout();
                        //通知更新view绘制
                        invalidate();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取当前控件宽高
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //设置当前view缩放系数 达到所有分辨率下都能完整显示
        if (totalRect != null) {
            double mapWidth = totalRect.width();
            scale = (float) (width / mapWidth);
        }
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //计算缩放系数 得到正确的点击位置
        handlerTouch(event.getX()/scale, event.getY()/scale);
        return super.onTouchEvent(event);
    }

    /**
     * 设置点击选中效果
     * @param x
     * @param y
     */
    private void handlerTouch(float x, float y) {
        if (itemList == null) {
            return;
        }
        //遍历循环当前省份是否被点击
        ProviceItem selectItem = null;
        for (ProviceItem proviceItem : itemList) {
            if (proviceItem.isTouch(x, y)) {
                selectItem = proviceItem;
            }
            //被点击 设置成功后 把成员变量select 设置为点击对象 更新ui
            if (selectItem != null) {
                select = selectItem;
                postInvalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (itemList != null) {
            canvas.save();
            canvas.scale(scale,scale);
            for (ProviceItem proviceItem : itemList) {
                if (proviceItem != select) {
                    proviceItem.drawItem(canvas, paint, false);
                }else{
                    select.drawItem(canvas, paint, true);
                }
            }
        }
    }
}
