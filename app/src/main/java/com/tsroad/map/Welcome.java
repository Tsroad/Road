package com.tsroad.map;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * Created by tsroad on 5/6/15.
 */
public class Welcome extends Activity implements  GeocodeSearch.OnGeocodeSearchListener {
    private ImageView welcomeImg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_app);
        welcomeImg = (ImageView) this.findViewById(R.id.welcome_img);
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
        anima.setDuration(2000);// 设置动画显示时间
        welcomeImg.startAnimation(anima);
        anima.setAnimationListener(new AnimationImpl());

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    private class AnimationImpl implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            welcomeImg.setBackgroundResource(R.drawable.welcome_img);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            skip(); // 动画结束后跳转到别的页面
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    private void skip() {
//        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
