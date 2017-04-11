package test.example.com.toggleanim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DetailsActivity extends AppCompatActivity {

    private int originY;
    private int img_Y;
    private int height;
    private int[] location = new int[2];
    private int[] location_ll_bottom = new int[2];
    private ImageView center, img_top1, img_top2;
    private LinearLayout ll_bottom;
    private float translateY;
    private int animEndY=0;

    /**
     * 获取状态栏高度——方法1
     */
    int statusBarHeight1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels; // 屏幕高度（像素）

        center = (ImageView) findViewById(R.id.center);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        img_top1 = (ImageView) findViewById(R.id.img_top1);
        img_top2 = (ImageView) findViewById(R.id.img_top2);
        img_top1.setVisibility(View.INVISIBLE);
        img_top2.setVisibility(View.INVISIBLE);
        center.setVisibility(View.INVISIBLE);
        ll_bottom.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enterAnim();
            }
        }, 100);
    }

    private void enterAnim() {
        img_top1.setVisibility(View.VISIBLE);
        img_top2.setVisibility(View.VISIBLE);
//        ll_bottom.setVisibility(View.VISIBLE);
//        center.setVisibility(View.VISIBLE);
        originY = getIntent().getIntExtra("y", 0) - statusBarHeight1;
        center.getLocationOnScreen(location);
        img_Y = location[1] - statusBarHeight1;
        translateY = originY - location[1];
        center.setY(center.getY() + translateY + statusBarHeight1);
        ll_bottom.getLocationOnScreen(location_ll_bottom);
//        if (center.getY() > img_Y)
//            if(center.getY()-img_Y>100)
        ll_bottom.setY(center.getY() + translateY + statusBarHeight1 + (center.getY() + translateY/2));
//            else
//                ll_bottom.setY(center.getY() + translateY + statusBarHeight1 + 800);
//        else
//            ll_bottom.setY(ll_bottom.getY() + center.getY() + translateY + statusBarHeight1);
        img_top1.setAlpha(0f);
        img_top2.setAlpha(0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                center.setVisibility(View.VISIBLE);
                ValueAnimator translateAnim = ValueAnimator.ofFloat(center.getY(), img_Y);
                translateAnim.setDuration(200);
                translateAnim.start();
                translateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        center.setY((Float) animation.getAnimatedValue());
                    }
                });
                translateAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        img_top1.setAlpha(1f);
                        img_top2.setAlpha(1f);
                        center.getLocationOnScreen(location);
                        animEndY=location[1];
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        }, 100);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_bottom.setVisibility(View.VISIBLE);
                if (ll_bottom.getY() < location_ll_bottom[1])
                    ll_bottom.setY(location_ll_bottom[1] + statusBarHeight1);
                ValueAnimator ll_bottom_translateAnim =
                        ValueAnimator.ofFloat(ll_bottom.getY(), location_ll_bottom[1] - statusBarHeight1);
                ll_bottom_translateAnim.setDuration(300);
                ll_bottom_translateAnim.start();
                ll_bottom_translateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ll_bottom.setY((Float) animation.getAnimatedValue());
                    }
                });
            }
        }, 100);
    }

    private void exitAnim() {
        center.getLocationOnScreen(location);
        ValueAnimator translateAnim = null;
        if(location[1]<animEndY)
            translateAnim = ValueAnimator.ofFloat(location[1] - statusBarHeight1 + (animEndY-location[1]), getIntent().getIntExtra("y", 0) - statusBarHeight1
                    + (animEndY-location[1]));
        else
            translateAnim = ValueAnimator.ofFloat(location[1] - statusBarHeight1, getIntent().getIntExtra("y", 0) - statusBarHeight1);
        translateAnim.setDuration(200);
        translateAnim.start();
        translateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                center.setY((Float) animation.getAnimatedValue());
            }
        });
        translateAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                DetailsActivity.this.finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        img_top1.setVisibility(View.INVISIBLE);
        img_top2.setVisibility(View.INVISIBLE);
        ll_bottom.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exitAnim();
            }
        },100);
    }
}
