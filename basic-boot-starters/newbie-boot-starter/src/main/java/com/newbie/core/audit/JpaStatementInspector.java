package com.newbie.core.audit;

import com.newbie.context.NewbieBootContext;
import com.newbie.core.util.id.IdUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
@Data
@Accessors(chain = true)
public class JpaStatementInspector implements StatementInspector {
    final static String CREATED_DATE = "cjsj";
    final static String MODIFIED_DATE = "zhxgsj";
    final static String NETWORK_ID = "sjly";
    final static String BSBH = "sjbsbh";

    @Override
    public String inspect(String sql) {
        Statements statements;
        try {
            statements = CCJSqlParserUtil.parseStatements(sql);
            StringBuilder sqlStringBuilder = new StringBuilder();
            int i = 0;
            for (Statement statement : statements.getStatements()) {
                if (null != statement) {
                    if (i++ > 0) {
                        sqlStringBuilder.append(';');
                    }
                    sqlStringBuilder.append(this.processParser(statement));
                }
            }
            String newSql = sqlStringBuilder.toString();
            log.info(newSql);
            return newSql;
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String processParser(Statement statement) {
        if (statement instanceof Insert) {
            this.processInsert((Insert) statement);
        }
        if (statement instanceof Update) {
            this.processUpdate((Update) statement);
        }
        /**
         * 返回处理后的SQL
         */
        return statement.toString();
    }

    private void processUpdate(Update update) {
        String bsbh = IdUtil.fastUUID().toString();
        String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
        String netId = NewbieBootContext.getApplicationContext().getEnvironment().getProperty("application.network-id");
        // 处理已有字段的更新
        final PreStatementFlag preStatementFlag = new PreStatementFlag();
        for (int i = 0; i < update.getColumns().size(); i++) {
            if(update.getColumns().get(i).getColumnName().toLowerCase().equals(MODIFIED_DATE)) {
                preStatementFlag.hasZhxgsj = true;
                preStatementFlag.indexOfZhxgsj = i;
            }
            if(update.getColumns().get(i).getColumnName().toLowerCase().equals(BSBH)) {
                preStatementFlag.hasSjbsbh = true;
                preStatementFlag.indexOfSjbsbh = i;
            }
        }

        if(!preStatementFlag.hasZhxgsj) {
            update.getColumns().add(new Column(MODIFIED_DATE));
            update.getExpressions().add(new StringValue(date));
        }

        if(!preStatementFlag.hasSjbsbh) {
            update.getColumns().add(new Column(BSBH));
            update.getExpressions().add(new StringValue(bsbh));
        }
    }


    public void processInsert(Insert insert) {
        String bsbh = IdUtil.fastUUID().toString();
        String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
        String netId = NewbieBootContext.getApplicationContext().getEnvironment().getProperty("application.network-id");
        // 处理已有字段的更新
        final PreStatementFlag preStatementFlag = new PreStatementFlag();
        for (int i = 0; i < insert.getColumns().size(); i++) {
            if(insert.getColumns().get(i).getColumnName().toLowerCase().equals(CREATED_DATE)) {
                preStatementFlag.hasCjsj = true;
                preStatementFlag.indexOfCjsj = i;
            }
            if(insert.getColumns().get(i).getColumnName().toLowerCase().equals(MODIFIED_DATE)) {
                preStatementFlag.hasZhxgsj = true;
                preStatementFlag.indexOfZhxgsj = i;
            }
            if(insert.getColumns().get(i).getColumnName().toLowerCase().equals(BSBH)) {
                preStatementFlag.hasSjbsbh = true;
                preStatementFlag.indexOfSjbsbh = i;
            }
            if(insert.getColumns().get(i).getColumnName().toLowerCase().equals(NETWORK_ID)) {
                preStatementFlag.hasSjly = true;
                preStatementFlag.indexOfSjly = i;
            }
        }
        if(!preStatementFlag.hasCjsj) {
            insert.getColumns().add(new Column(CREATED_DATE));
            ((ExpressionList) insert.getItemsList()).getExpressions().add(new StringValue(date));
        }
        if(!preStatementFlag.hasZhxgsj) {
            insert.getColumns().add(new Column(MODIFIED_DATE));
            ((ExpressionList) insert.getItemsList()).getExpressions().add(new StringValue(date));
        }

        if(!preStatementFlag.hasSjbsbh) {
            insert.getColumns().add(new Column(BSBH));
            ((ExpressionList) insert.getItemsList()).getExpressions().add(new StringValue(bsbh));
        }

        if(!preStatementFlag.hasSjly) {
            insert.getColumns().add(new Column(NETWORK_ID));
            ((ExpressionList) insert.getItemsList()).getExpressions().add(new StringValue(netId));
        }
    }

    @Data
    class PreStatementFlag{
        private boolean hasCjsj;
        private boolean hasZhxgsj;
        private boolean hasSjly;
        private boolean hasSjbsbh;

        private int indexOfCjsj;
        private int indexOfZhxgsj;
        private int indexOfSjly;
        private int indexOfSjbsbh;
    }
}

