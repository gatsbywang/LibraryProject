package myapplication.libraryproject;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater layoutInflater
                = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory
                (layoutInflater, new LayoutInflaterFactory() {
                    @Override
                    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                        //拦截view的创建，获取View之后要去解析
                        //1、创建view

                        //2、解析属性 src textColor background 自定义属性

                        //3、统一交给SkinManager管理



                        return null;
                    }
                });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
