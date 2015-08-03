package com.opensymphony.workflow.spi.jdbc;

import com.opensymphony.workflow.StoreException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class MySQLWorkflowStore extends JDBCWorkflowStore
{
  protected String entrySequenceIncrement = null;
  protected String entrySequenceRetrieve = null;
  protected String stepSequenceIncrement = null;
  protected String stepSequenceRetrieve = null;

  public void init(Map props)
    throws StoreException
  {
    super.init(props);
    this.stepSequenceIncrement = ((String)props.get("step.sequence.increment"));
    this.stepSequenceRetrieve = ((String)props.get("step.sequence.retrieve"));
    this.entrySequenceIncrement = ((String)props.get("entry.sequence.increment"));
    this.entrySequenceRetrieve = ((String)props.get("entry.sequence.retrieve"));
  }

  protected long getNextEntrySequence(Connection c) throws SQLException {
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try
    {
      long l1;
      long id = -2590847602933104640L;
      getJtN().update(this.entrySequenceIncrement);
      SqlRowSet sqlRow = getJtN().queryForRowSet(this.entrySequenceRetrieve);
      sqlRow.next();
      id = sqlRow.getLong(1);

      return id;
    } finally {
      cleanup(null, stmt, rset);
    }
  }

  public long getNextStepSequence(Connection c) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try
    {
      long l1;
      long id = -2590847602933104640L;
      getJtN().update(this.stepSequenceIncrement);
      SqlRowSet sqlRow = getJtN().queryForRowSet(this.stepSequenceRetrieve);
      sqlRow.next();
      id = sqlRow.getLong(1);

      return id;
    } finally {
      cleanup(null, stmt, rset);
    }
  }
}