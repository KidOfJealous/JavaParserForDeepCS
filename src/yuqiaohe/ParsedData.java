package yuqiaohe;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import sun.security.krb5.internal.crypto.Des;

import java.util.*;
import java.io.*;

import java.util.ArrayList;

public class ParsedData {
    private static ArrayList<String[]> AllApieSeq = new ArrayList<>();
    private static ArrayList<String[]> MethodName = new ArrayList<>();
    private static ArrayList<String> Desc = new ArrayList<>();
    private static ArrayList<String[]> Token = new ArrayList<>();
    ArrayList<String> ApiSeq = new ArrayList<>();
    //ArrayList<String> Desc = new ArrayList<>();
    //MappingChange.Map<String,Integer> Tokens = new MappingChange.Map<String>();
    Map Tokens = new HashMap();
    VoidVisitorAdapter<Object> ParseAdapter;
    VoidVisitorAdapter<Object> innerParser;
    public void Parse(String s) {
        CompilationUnit cu = JavaParser.parse(s);
        //Process(cu,0);
        ParseAdapter.visit(cu,null);
    }
    private void Process(Node cu,int level)
    {
        //System.out.println("level:"+level+":"+cu.getClass().getSimpleName()+":"+cu.toString());
        if(cu instanceof JavadocComment)return;
        //cu.removeComment();
        if(cu instanceof MethodCallExpr)
        {
            ApiSeq.add(((MethodCallExpr) cu).getNameAsString());
        }
        if(cu.getChildNodes().size()>0)
        {
            for(Node i:cu.getChildNodes())Process(i,level+1);
        }
        else
        {
            addTokens(cu.toString());
        }
    }
    private void addTokens(String s) {
        int start = 0;
        int l = s.length();
        int end;
        while (start < l) {
            StringBuilder sb = new StringBuilder();
            sb.append(Character.toLowerCase(s.charAt(start)));
            end = start + 1;
            while (end<l&&Character.isLowerCase(s.charAt(end))) {
                sb.append(s.charAt(end));
                end++;
            }
            if(sb.length()>1)Tokens.put(sb.toString(),"1");
            if (end < l) {
                start = end;
            } else break;
        }
    }
    private String[] ParsemethodName(String s)
    {

        ArrayList<String> st = new ArrayList<>();
        int start = 0;
        int l = s.length();
        int end;
        while (start < l) {
            StringBuilder sb = new StringBuilder();
            sb.append(Character.toLowerCase(s.charAt(start)));
            end = start + 1;
            while (end<l&&Character.isLowerCase(s.charAt(end))) {
                sb.append(s.charAt(end));
                end++;
            }
            if(sb.length()>1)st.add(sb.toString());
            if (end < l) {
                start = end;
            } else break;
        }
        String[] sa = new String[st.size()];
        st.toArray(sa);
        return sa;
    }
    private static void Write(ArrayList<String[]> a,String filename)
    {
        File f = new File(filename);
        try{
            if(!f.exists()) f.createNewFile();
            BufferedWriter in = new BufferedWriter(new FileWriter(f,true));
            for(String[] i:a)
            {
                for(String j:i) {
                    System.out.println(j);
                    in.write(j+" ");
                }
                in.newLine();
            }
            in.flush();
            in.close();
            a.clear();
        }catch (IOException e){e.printStackTrace();}
    }
    private static void WriteDesc()
    {
        File f = new File("Description");
        try {
            if(!f.exists()) f.createNewFile();
            BufferedWriter in = new BufferedWriter(new FileWriter(f,true));
            for(String i:Desc)
            {
                System.out.println(i);
                in.write(i);
                in.newLine();
            }
            in.flush();
            in.close();
            Desc.clear();
        }catch (IOException e){e.printStackTrace();}
    }
    public static void WriteInFile()
    {
        Write(MethodName,"MethodName");
        Write(AllApieSeq,"ApiSeq");
        Write(Token,"Token");
        WriteDesc();
    }
    public ParsedData() {
        ParseAdapter = new VoidVisitorAdapter() {
            @Override
            public void visit(MethodDeclaration n, Object arg) {
                super.visit(n, arg);
                String[] methodName = ParsemethodName(n.getNameAsString());
                String[] docs = n.getJavadocComment().toString().split("\n");
                if(docs.length<2)return;
                //System.out.print("MethodName:");

                //System.out.println();
                //System.out.println("MethodDes:" + n.getJavadocComment().toString().split("\n")[0]);
                //n.removeComment();
                Process(n,0);
                String[] declarations = docs[1].split("\\*|@");
                if(declarations.length<2)return;
                String declaration = declarations[1].trim();
                if(declaration.length()<3)return;
                //for(String i:docs)System.out.println(i);
                //System.out.println("Tokens:"+Tokens.keySet());
                if(Tokens.size()==0||ApiSeq.size()==0)return;
                Set tokens = Tokens.keySet();
                String[] token = new String[Tokens.keySet().size()];
                tokens.toArray(token);
                String[] apiseq = new String[ApiSeq.size()];
                ApiSeq.toArray(apiseq);
                System.out.println(AllApieSeq.size());
                MethodName.add(methodName);
                AllApieSeq.add(apiseq);
                //System.out.println("ApiSequence:"+ApiSeq);
                Desc.add(declaration);
                Token.add(token);
                //System.out.println("Declaration:"+declaration);
                super.visit(n, arg);
            }
        };
        innerParser = new VoidVisitorAdapter<Object>() {
            @Override
            public void visit(FieldAccessExpr n, Object arg) {
                super.visit(n, arg);
                //Process(n);
                System.out.println(n);
            }
        };

    }
}
