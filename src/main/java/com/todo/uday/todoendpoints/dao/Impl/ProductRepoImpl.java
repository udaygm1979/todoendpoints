package com.todo.uday.todoendpoints.dao.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.todo.uday.todoendpoints.dao.ProductRepo;
import com.todo.uday.todoendpoints.entities.Product;

@Repository
public class ProductRepoImpl implements ProductRepo {

	@Autowired
	private JdbcOperations jdbcOperations;

	private RowMapper<Product> prodRowMapper = (rs, rownumber) -> {
		Product product = new Product();
		product.setId(rs.getLong("id"));
		product.setDescription(rs.getString("description"));
		product.setCompletion(rs.getString("completion"));
		product.setStatus(rs.getInt("status"));
		return product;
	};

	private RowMapper<Long> maxRowId = (rs, rownumber) -> {
		return rs.getLong("id");
	};

	/**
	 * Method to establish a connection with database 
	 * and perform select all operation.
	 * JDBC Template is used.
	 */
	@Override
	public List<Product> fetchAllProduct() {
		return jdbcOperations.query(Queries.QUERY_FETCH_ALL, prodRowMapper);
	}

	/**
	 * Method to establish a connection with database 
	 * and perform select operation for a particular product.
	 * JDBC Template is used.
	 */
	@Override
	public Product findProductById(long id) {
		return jdbcOperations.queryForObject(Queries.QUERY_FETCH_BY_ID, prodRowMapper, id);
	}

	/**
	 * Method to establish a connection with database 
	 * and perform insert operation.
	 * JDBC Template is used.
	 */

	@Override
	public void saveProduct(Product product) {
		jdbcOperations.update(Queries.QUERY_INSERT_PRODUCT, product.getDescription(), product.getCompletion(),
				product.getStatus());

	}

	/**
	 * Method to establish a connection with database 
	 * and perform update operation for a selected record and provided description.
	 * JDBC Template is used.
	 */

	@Override
	public void patchProduct(Product product) {
		jdbcOperations.update(Queries.QUERY_PATCH_PRODUCT, product.getDescription(), product.getId());

	}
	

	/**
	 * Method to establish a connection with database 
	 * and perform delete operation for the provided Product id.
	 * JDBC Template is used.
	 */

	@Override
	public void deleteProductyId(long id) {
		jdbcOperations.update(Queries.QUERY_DELETE_BY_ID, id);

	}

	/**
	 * Method to establish a connection with database 
	 * and perform select operation and return MAX id in the Product table.
	 * JDBC Template is used.
	 */
	@Override
	public Long getMaxProductId() {
		return jdbcOperations.query(Queries.QUERY_MAX_PRODUCT_ID, maxRowId).get(0);
	}

	interface Queries {
		String QUERY_FETCH_ALL = "SELECT * FROM PRODUCT";
		String QUERY_FETCH_BY_ID = "SELECT * FROM PRODUCT WHERE ID =?";
		String QUERY_DELETE_BY_ID = "DELETE FROM PRODUCT WHERE ID = ?";
		String QUERY_INSERT_PRODUCT = "INSERT INTO PRODUCT (DESCRIPTION, COMPLETION, STATUS) VALUES (?,?,?)";
		String QUERY_PATCH_PRODUCT = "UPDATE PRODUCT SET DESCRIPTION = ? WHERE ID = ?";
		String QUERY_MAX_PRODUCT_ID = "SELECT MAX(ID) AS ID FROM PRODUCT";
	}
}
