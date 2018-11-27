package yuqiaohe;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.metamodel.AnnotationExprMetaModel;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
        public static void main(String[] args) {
            try {
                File Filedirs = new File("1.txt");
                String[] filedirs = new String[10000];
                int line = 0;
                String temp = null;
                BufferedReader reader = new BufferedReader(new FileReader(Filedirs));
                while ((temp = reader.readLine()) != null) {
                    filedirs[line++] = temp;
                }
                reader.close();
                for (int i = 0; i < line; ++i) {
                    //System.out.println("parse "+i+"start:");
                    String pathToAppSourceCode = filedirs[i];
                    System.out.println("parse start:" + i);
                    getAllJavaFilePaths(new File(pathToAppSourceCode));
                    ParsedData.WriteInFile();
                }
            }catch (IOException ex){ex.printStackTrace();}
            String s = "package com.example.bottombar.sample;\n" +
                    "\n" +
                    "import android.os.Bundle;\n" +
                    "import android.support.annotation.IdRes;\n" +
                    "import android.support.annotation.Nullable;\n" +
                    "import android.support.v7.app.AppCompatActivity;\n" +
                    "import android.widget.TextView;\n" +
                    "import android.widget.Toast;\n" +
                    "\n" +
                    "import com.roughike.bottombar.BottomBar;\n" +
                    "import com.roughike.bottombar.BottomBarTab;\n" +
                    "import com.roughike.bottombar.OnTabReselectListener;\n" +
                    "import com.roughike.bottombar.OnTabSelectListener;\n" +
                    "\n" +
                    "/**\n" +
                    " * Created by iiro on 7.6.2016.\n" +
                    " */\n" +
                    "public class BadgeActivity extends AppCompatActivity {\n" +
                    "    private TextView messageView;\n" +
                    "\n" +
                    "    @Override\n" +
                    "    protected void onCreate(@Nullable Bundle savedInstanceState) {\n" +
                    "        super.onCreate(savedInstanceState);\n" +
                    "        setContentView(R.layout.activity_three_tabs);\n" +
                    "\n" +
                    "        messageView = (TextView) findViewById(R.id.messageView);\n" +
                    "\n" +
                    "        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);\n" +
                    "        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {\n" +
                    "            @Override\n" +
                    "            public void onTabSelected(@IdRes int tabId) {\n" +
                    "                messageView.setText(TabMessage.get(tabId, false));\n" +
                    "            }\n" +
                    "        });\n" +
                    "\n" +
                    "        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {\n" +
                    "            @Override\n" +
                    "            public void onTabReSelected(@IdRes int tabId) {\n" +
                    "                Toast.makeText(getApplicationContext(), TabMessage.get(tabId, true), Toast.LENGTH_LONG).show();\n" +
                    "            }\n" +
                    "        });\n" +
                    "\n" +
                    "        BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_nearby);\n" +
                    "        nearby.setBadgeCount(5);\n" +
                    "    }\n" +
                    "}";
            //ParsedData pd = new ParsedData();
            //pd.Parse(s);
        }
        private static void getAllJavaFilePaths(File srcFolder) {
            File[] fileArray = srcFolder.listFiles();
            if(fileArray==null)return;
            for (File file : fileArray) {
                if (file.isDirectory()) {
                    getAllJavaFilePaths(file);
                } else {
                    if (file.getName().endsWith(".java")) {
                        ParsedData dt = new ParsedData();
                        dt.Parse(readToString(file));
                        System.out.println(" "+file.getName()+" parsed");
                    }
                }
            }
        }
        private static String readToString(File file) {
            String encoding = "UTF-8";
            //File file = new File(fileName);
            Long filelength = file.length();
            byte[] filecontent = new byte[filelength.intValue()];
            try {
                FileInputStream in = new FileInputStream(file);
                in.read(filecontent);
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return new String(filecontent, encoding);
            } catch (UnsupportedEncodingException e) {
                System.err.println("The OS does not support " + encoding);
                e.printStackTrace();
                return null;
            }
        }

};