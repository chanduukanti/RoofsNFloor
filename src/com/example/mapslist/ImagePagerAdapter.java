package com.example.mapslist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImagePagerAdapter extends PagerAdapter {

    Context context;
    // Image image;
    Drawable drawable[];
    LayoutInflater inflater;
    HolderView [] mHolderView;
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return drawable.length;
    }

    public ImagePagerAdapter(Context context, Drawable[] image) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.drawable = image;
        this.mHolderView = new HolderView[image.length];
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == ((RelativeLayout) object);
    }
    public HolderView[] getHolderViews(){
        return mHolderView;
    }
    @Override
    public Object instantiateItem(View container, int position) {

        // Declare Variables
        ImageView planImage;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // View itemView = inflater.inflate(R.layout.imagepager_item, container,
        // false);
        mHolderView[position] = new HolderView();
        View itemView = inflater.inflate(R.layout.imagepager_item,
                (ViewGroup) container, false);
        // Locate the ImageView in imagepager_item.xml
        planImage = (ImageView) itemView.findViewById(R.id.plan_image);
        // Capture position and set to the ImageView
        planImage.setBackground(drawable[position]);
        mHolderView[position].setView(planImage);
        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
    
    class HolderView{
        ImageView view;
        public void setView(ImageView v){
            this.view = v;
        }
        public ImageView getView(){
            return view;
        }
    }

}
