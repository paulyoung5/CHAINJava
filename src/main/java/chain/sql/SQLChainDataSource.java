package chain.sql;

import chain.core.ChainDataSource;

import java.sql.ResultSet;

/**
 *
 */
public interface SQLChainDataSource extends ChainDataSource {
    public ResultSet executeQuery(String query) throws ChainDataSourceException;
}
