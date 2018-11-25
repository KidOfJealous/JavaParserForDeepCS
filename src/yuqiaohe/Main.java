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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
        public static void main(String[] args) {
            String s = "package yuqiaohe;\n" +
                    "\n" +
                    "import java.util.Calendar;\n" +
                    "import java.util.Date;\n" +
                    "\n" +
                    "public class Main {\n" +
                    "\n" +
                    "    public static void main(String[] args) {\n" +
                    "\t// write your code here\n" +
                    "\n" +
                    "    }\n" +
                    "    /**\n" +
                    "     * Converts a Date into a Calendar.\n" +
                    "     * @param date the date to convert to a Calendar\n" +
                    "     * @return the created Calendar\n" +
                    "     * @throws NullPointerException if null is passed in\n" +
                    "     * @since 3.0\n" +
                    "     */\n" +
                    "    public static Calendar toCalendar(final Date date) {\n" +
                    "        final Calendar c = Calendar.getInstance();\n" +
                    "        c.setTime(date);\n" +
                    "        return c;\n" +
                    "    }\n" +
                    "}";
            ParsedData pd = new ParsedData();
            pd.Parse(s);
        }

};