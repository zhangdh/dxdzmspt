package ictp;

import com.cownew.cownewsql.imsql.ISQLTranslator;
import com.cownew.cownewsql.imsql.common.DialectManager;
import com.cownew.cownewsql.imsql.common.TranslateException;

public class TestCownew {
	public static void main(String[] args) throws TranslateException {
		ISQLTranslator txMysql = DialectManager.createTranslator("mysql");
		ISQLTranslator txMssql = DialectManager.createTranslator("mssqlserver");
		ISQLTranslator txOracle = DialectManager.createTranslator("oracle");
		String sql;
		String[] venderSQLs;
		sql = "select concat(tochar(1),'b') as x,trim(c),tochar(1) from t where sj>{'2008-9-1'}";
		venderSQLs = txMysql.translateSQL(sql);
		System.out.println("mysql翻译后的SQL:" + venderSQLs[0]);
		venderSQLs = txMssql.translateSQL(sql);
		System.out.println("sqlserver翻译后的SQL:" + venderSQLs[0]);
		venderSQLs = txOracle.translateSQL(sql);
		System.out.println("oracle翻译后的SQL:" + venderSQLs[0]);
	}
}
