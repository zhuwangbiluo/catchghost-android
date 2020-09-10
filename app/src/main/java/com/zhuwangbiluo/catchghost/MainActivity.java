package com.zhuwangbiluo.catchghost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //模式 多傻瓜：0  多鬼：1
    private int gameModel = 0;
    //参与游戏人数
    private int peopleCount = 5;

    private RadioButton radioMultiFool, radioMultiGhost;

    private ArrayAdapter adapter;

    private EditText edtFarmerWord, edtFoolWord;

    public static Map<String, String> randomWordRepository = new HashMap<>();

    static {
        randomWordRepository.put("恐龙", "孔融");
        randomWordRepository.put("玫瑰", "月季");
        randomWordRepository.put("肥皂", "香皂");
        randomWordRepository.put("矿泉水", "纯净水");
        randomWordRepository.put("奶粉", "藕粉");
        randomWordRepository.put("辣椒", "芥末");
        randomWordRepository.put("气泡", "水泡");
        randomWordRepository.put("唇膏", "口红");
        randomWordRepository.put("太监", "人妖");
        randomWordRepository.put("白菜", "生菜");
        randomWordRepository.put("碎碎冰", "棒棒冰");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtFarmerWord = findViewById(R.id.edt_farmer_word);
        edtFoolWord = findViewById(R.id.edt_fool_word);


        //游戏模式单选框事件
        RadioGroup ragGameModel = findViewById(R.id.rag_game_model);
        radioMultiFool = findViewById(R.id.rad_multi_fool);
        radioMultiGhost = findViewById(R.id.rad_multi_ghost);
        ragGameModel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioMultiFool.getId()) {
                    gameModel = 0;
                } else if (checkedId == radioMultiGhost.getId()) {
                    gameModel = 1;
                }
            }
        });


        //选择人数下拉框事件
        //第一步：定义下拉列表内容
        List<String> list = new ArrayList<>();
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        Spinner spinner = findViewById(R.id.sp_people_count);

        //第二步：为下拉列表定义一个适配器
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);

        //第三步：设置下拉列表下拉时的菜单样式
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        //第四步：将适配器添加到下拉列表上
        spinner.setAdapter(adapter);

        //第五步：添加监听器，为下拉列表设置事件的响应
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                peopleCount = Integer.parseInt(String.valueOf(adapter.getItem(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        //调换词语按钮事件
        Button btnShuffle = findViewById(R.id.btn_shuffle);
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempWord = edtFarmerWord.getText().toString();
                edtFarmerWord.setText(edtFoolWord.getText().toString());
                edtFoolWord.setText(tempWord);
            }
        });

        //系统出题按钮事件
        Button btnSystemModel = findViewById(R.id.btn_system_model);
        btnSystemModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] keys = randomWordRepository.keySet().toArray(new String[0]);
                Random random = new Random();
                int randomNum = random.nextInt(keys.length);
                String randomKey = keys[randomNum];
                String randomValue = randomWordRepository.get(randomKey);
                int randomRoleType = random.nextInt(keys.length);
                if (randomRoleType % 2 == 0) {
                    edtFoolWord.setText(randomKey);
                    edtFarmerWord.setText(randomValue);
                } else {
                    edtFoolWord.setText(randomValue);
                    edtFarmerWord.setText(randomKey);
                }
            }
        });

        //开始游戏按钮事件
        Button btnStartGame = findViewById(R.id.btn_start_game);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(edtFarmerWord.getText().toString()) || "".equals(edtFoolWord.getText().toString())) {
                    Toast toast = Toast.makeText(getApplication(), "你怕不是个🐷吧，没有词就开始了~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("foolWord", edtFoolWord.getText().toString());
                    bundle.putString("farmerWord", edtFarmerWord.getText().toString());
                    bundle.putString("gameModel", String.valueOf(gameModel));
                    bundle.putString("peopleCount", String.valueOf(peopleCount));
                    intent.putExtras(bundle);
                    intent.setClass(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                }
            }
        });

        //无裁判模式按钮事件
        Button btnNoReferee = findViewById(R.id.btn_no_referee);
        btnNoReferee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] keys = randomWordRepository.keySet().toArray(new String[0]);
                Random random = new Random();
                int randomNum = random.nextInt(keys.length);
                String randomKey = keys[randomNum];
                String randomValue = randomWordRepository.get(randomKey);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                int randomRoleType = random.nextInt(keys.length);
                if (randomRoleType % 2 == 0) {
                    bundle.putString("foolWord", randomKey);
                    bundle.putString("farmerWord", randomValue);
                } else {
                    bundle.putString("foolWord", randomKey);
                    bundle.putString("farmerWord", randomValue);
                }
                bundle.putString("gameModel", String.valueOf(gameModel));
                bundle.putString("peopleCount", String.valueOf(peopleCount));
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

    }

//    public static void main(String[] args) {
////        readStringFromFile("ic_launcher_background.xml");
//        addFile("");
//    }
//
//    public static String readStringFromFile(String fileName) {
//        StringBuilder sb = new StringBuilder("");
//        try {
//            //打开文件输入流
//            FileInputStream inputStream = new FileInputStream(fileName);
//            byte[] buffer = new byte[1024];
//            int len = inputStream.read(buffer);
//            //读取文件内容
//            while (len > 0) {
//                sb.append(new String(buffer, 0, len));
//                //继续将数据放到buffer中
//                len = inputStream.read(buffer);
//            }
//            //关闭输入流
//            inputStream.close();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        return sb.toString();
//    }
//
//    public static void addFile(String requestUrlStr) {
//        String filePath = Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + File.separator + "RequestHistory" + File.separator;
//        String fileName = filePath + "saveAnswer.txt";
//        File file = null;
//        try {
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                file = new File(fileName);
//                if (!file.exists()) {
////                    Files.createParentDirs(file);
//                    if (file.createNewFile()) {
//                        writeAppend(fileName, requestUrlStr);
//                    }
//                } else {
//                    writeAppend(fileName, "\n" + requestUrlStr);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void writeAppend(String fileName, String requestUrlStr) {
//        FileWriter writer = null;
//        try {
//            writer = new FileWriter(fileName, true);
//            writer.write(requestUrlStr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (writer != null) {
//                    writer.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
