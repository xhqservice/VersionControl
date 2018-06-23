package com.jadlsoft.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

public class MapResultSetExtractor implements ResultSetExtractor {

	private final RowMapper rowMapper;

	private final int rowsExpected;
	
	private final String keyColumn;


	/**
	 * Create a new RowMapperResultSetExtractor.
	 * @param rowMapper the RowMapper which creates an object for each row
	 */
	public MapResultSetExtractor(RowMapper rowMapper, String keyColumn) {
		this(rowMapper, 0, keyColumn);
	}

	/**
	 * Create a new RowMapperResultSetExtractor.
	 * @param rowMapper the RowMapper which creates an object for each row
	 * @param rowsExpected the number of expected rows
	 * @param keyColumn the key column name of each row
	 * (just used for optimized collection handling)
	 */
	public MapResultSetExtractor(RowMapper rowMapper, int rowsExpected, String keyColumn) {
		Assert.notNull(rowMapper, "RowMapper is required");
		Assert.notNull(keyColumn, "KeyColumn is required");
		this.rowMapper = rowMapper;
		this.rowsExpected = rowsExpected;
		this.keyColumn = keyColumn;
	}


	public Object extractData(ResultSet rs) throws SQLException {
		Map results = (this.rowsExpected > 0 ? new LinkedHashMap(this.rowsExpected) : new LinkedHashMap());
		int rowNum = 0;
		while (rs.next()) {
			Map row = (Map) this.rowMapper.mapRow(rs, rowNum++);
			results.put(row.get(this.keyColumn),row);
		}
		return results;
	}

}

