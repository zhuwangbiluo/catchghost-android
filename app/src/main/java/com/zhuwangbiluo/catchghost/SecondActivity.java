package com.zhuwangbiluo.catchghost;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SecondActivity extends AppCompatActivity {

    //模式 多傻瓜：0  多鬼：1
    private int gameModel = 0;
    //参与游戏人数
    private int peopleCount = 5;
    //平民人数
    public static int farmerCount = 0;
    //傻瓜人数
    public static int foolCount = 0;
    //👻人数
    public static int ghostCount = 0;
    //农民词
    private String farmerWord;
    //傻瓜词
    private String foolWord;
    //用户身份
    private List<String> roleList;

    private Handler handler = null;

    public static Map<Integer, Integer> roleButtonMap = new HashMap<>(15);

    static {
        roleButtonMap.put(1, R.id.role1Button);
        roleButtonMap.put(2, R.id.role2Button);
        roleButtonMap.put(3, R.id.role3Button);
        roleButtonMap.put(4, R.id.role4Button);
        roleButtonMap.put(5, R.id.role5Button);
        roleButtonMap.put(6, R.id.role6Button);
        roleButtonMap.put(7, R.id.role7Button);
        roleButtonMap.put(8, R.id.role8Button);
        roleButtonMap.put(9, R.id.role9Button);
        roleButtonMap.put(10, R.id.role10Button);
        roleButtonMap.put(11, R.id.role11Button);
        roleButtonMap.put(12, R.id.role12Button);
        roleButtonMap.put(13, R.id.role13Button);
        roleButtonMap.put(14, R.id.role14Button);
        roleButtonMap.put(15, R.id.role15Button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle myBundle = this.getIntent().getExtras();
        gameModel = Integer.parseInt(myBundle.getString("gameModel"));
        peopleCount = Integer.parseInt(myBundle.getString("peopleCount"));
        farmerWord = myBundle.getString("farmerWord");
        foolWord = myBundle.getString("foolWord");
        roleList = randomUserRole(peopleCount, gameModel, farmerWord, foolWord);
//        Toast.makeText(getBaseContext(), "游戏模式：" + gameModel + " 游戏模人数：" + peopleCount + " 农民词：" + farmerWord + " 傻瓜🍉词：" + foolWord, Toast.LENGTH_LONG).show();
        final LinearLayout layout2 = new LinearLayout(this);
        layout2.setOrientation(LinearLayout.VERTICAL);


        final LinearLayout layout3 = new LinearLayout(this);
        layout3.setOrientation(LinearLayout.HORIZONTAL);
        layout3.setGravity(Gravity.RIGHT);
        layout2.addView(layout3);

        TextView gameTip = new TextView(this);
        gameTip.setId(R.id.gameTip);
//        userWordTextView.setText("当前游戏傻瓜" + foolNum + "人，平民" + farmerNum + "人，👻" + ghostNum + "人");
        gameTip.setText("当前平民" + farmerCount + "人，傻瓜" + foolCount + "人，👻" + ghostCount + "人");
        layout3.addView(gameTip);

        Button clickableButton = new Button(this);
        clickableButton.setText("裁判模式");
        clickableButton.setTextColor(Color.RED);
        layout3.addView(clickableButton);
        clickableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 1; i <= peopleCount; i++) {
                    Button button = findViewById(roleButtonMap.get(i));
                    button.setClickable(true);
                    button.setTextColor(Color.RED);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int buttonIndex = (Integer) view.getTag();
                            TextView textView = findViewById(R.id.userWordTextView);
                            textView.setText(buttonIndex + "号玩家：" + roleList.get(buttonIndex - 1));
                        }
                    });
                    button.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            String word = roleList.get((Integer) v.getTag() - 1);
                            if (farmerWord.equals(word)) {
                                farmerCount--;
                            } else if (foolWord.equals(word)) {
                                foolCount--;
                            } else {
                                ghostCount--;
                            }
                            v.setVisibility(View.INVISIBLE);
                            TextView gameTip = findViewById(R.id.gameTip);
                            gameTip.setText("当前平民" + farmerCount + "人，傻瓜" + foolCount + "人，👻" + ghostCount + "人");

                            String gameTips = "请继续游戏";
                            if (farmerCount == 0) {
                                gameTips = "恭喜👻获胜！";
                            }
                            if (ghostCount == 0) {
                                gameTips = "恭喜平民获胜！";
                            }
                            Toast toast = Toast.makeText(getApplication(), gameTips, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                            return true;
                        }
                    });
                }
            }
        });


        final LinearLayout layout4 = new LinearLayout(this);
        layout4.setOrientation(LinearLayout.HORIZONTAL);
        layout2.addView(layout4);


        Button hideRoleButton = new Button(this);
        hideRoleButton.setText("隐藏身份");
        layout4.addView(hideRoleButton);
        hideRoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.userWordTextView);
                textView.setText("******");
            }
        });

        TextView userWordTextView = new TextView(this);
        userWordTextView.setId(R.id.userWordTextView);
        userWordTextView.setText("******");
        layout4.addView(userWordTextView);


        handler = new Handler();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout2.addView(layout);
        for (int i = 1; i <= peopleCount; i++) {
            if (i % 3 == 1) {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout2.addView(layout);
            }
            Button bt = new Button(this);
            bt.setId(roleButtonMap.get(i));
            bt.setTag(i);
            bt.setText(i + "号玩家");
            bt.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layout.addView(bt,p);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int buttonIndex = (Integer) view.getTag();
                    TextView textView = findViewById(R.id.userWordTextView);
                    textView.setText(buttonIndex + "号玩家：" + roleList.get(buttonIndex - 1));
                    Button currentButton = (Button) view;
                    currentButton.setClickable(false);
                    currentButton.setTextColor(Color.WHITE);
                    if (buttonIndex < peopleCount) {
                        Button nextButton = findViewById(roleButtonMap.get(buttonIndex + 1));
                        nextButton.setClickable(true);
                        nextButton.setTextColor(Color.RED);
                    }

                    new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(2000); //休眠一秒
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    TextView textView = findViewById(R.id.userWordTextView);
                                    textView.setText("******");
                                }
                            });
                        }
                    }.start();
                }
            });
            if (i == 1) {
                bt.setClickable(true);
                bt.setTextColor(Color.RED);
            } else {
                bt.setClickable(false);
            }
        }

        final LinearLayout layout5 = new LinearLayout(this);
        layout5.setOrientation(LinearLayout.HORIZONTAL);
        layout2.addView(layout5);

        Button btnGuessWord = new Button(this);
        btnGuessWord.setText("猜词");
        layout5.addView(btnGuessWord);
        btnGuessWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.guessWordTextView);
                if (farmerWord.equals(textView.getText().toString())) {
                    Toast toast = Toast.makeText(getApplication(), "niubility，猜对了！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplication(), "小🐷🐷，再接再厉~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }
        });

        EditText guessWord = new EditText(this);
        guessWord.setId(R.id.guessWordTextView);
        guessWord.setWidth(600);
        layout5.addView(guessWord);


        setContentView(layout2);
    }


    /**
     * 随机角色
     *
     * @return 角色集合
     */
    public static List<String> randomUserRole(int peopleCount, int model, String farmerWord, String foolWord) {
        List<String> roleList = new ArrayList<>(peopleCount);
        farmerCount = peopleCount / 2 + peopleCount % 2;
        int moreRoleNum = (peopleCount - farmerCount) / 2 + (peopleCount - farmerCount) % 2;
        int lessRoleNum = peopleCount - farmerCount - moreRoleNum;
        if (model == 1) {
            //鬼多
            ghostCount = moreRoleNum;
            foolCount = lessRoleNum;
        } else {
            //傻瓜多
            foolCount = moreRoleNum;
            ghostCount = lessRoleNum;
        }
        for (int i = 0; i < farmerCount; i++) {
            roleList.add(farmerWord);
        }
        for (int i = 0; i < foolCount; i++) {
            roleList.add(foolWord);
        }
        for (int i = 0; i < ghostCount; i++) {
            roleList.add("恭喜，你是👻！");
        }
        Collections.shuffle(roleList);
        return roleList;
    }

}