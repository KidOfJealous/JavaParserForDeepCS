package yuqiaohe;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sun.javafx.collections.MappingChange;

import javax.xml.stream.events.Comment;
import java.util.*;

import java.util.ArrayList;

public class ParsedData {
    ArrayList<String> MethodName = new ArrayList<>();
    ArrayList<String> ApiSeq = new ArrayList<>();
    ArrayList<String> Desc = new ArrayList<>();
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
        System.out.println("level:"+level+":"+cu.getClass().getSimpleName()+":"+cu.toString());
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

    //private void
    public ParsedData() {
        ParseAdapter = new VoidVisitorAdapter() {
            @Override
            public void visit(MethodDeclaration n, Object arg) {
                super.visit(n, arg);
                String methodName = n.getNameAsString();
                String[] docs = n.getJavadocComment().toString().split("\n");
                if(docs.length<2)return;
                System.out.println("MethodName:" + n.getName());
                //System.out.println("MethodDes:" + n.getJavadocComment().toString().split("\n")[0]);
                //n.removeComment();
                Process(n,0);
                String[] declarations = docs[1].split("\\*");
                if(declarations.length<2)return;
                String declaration = declarations[1];
                //for(String i:docs)System.out.println(i);
                System.out.println("Tokens:"+Tokens.keySet());
                System.out.println("ApiSequence:"+ApiSeq);
                System.out.println("Declaration:"+declaration);
                //CompilationUnit cu = JavaParser.parse(n.toString());
                //cu.accept(new method)
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
