package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

//Anotación lombok para logging (traza) de la aplicación
@Slf4j
//Un Repository es un componente y a su vez un estereotipo de Spring
//que forma parte de la ‘capa de persistencia’.
@Repository
public class ComercialDAOImpl implements ComercialDAO {

	//Plantilla jdbc inyectada automáticamente por el framework Spring, gracias a la anotación @Autowired.
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Inserta en base de datos el nuevo Cliente, actualizando el id en el bean Cliente.
	 */
	@Override
	public synchronized void create(Comercial comercial) {

		//Desde java15+ se tiene la triple quote """ para bloques de texto como cadenas.
		String sqlInsert = """
							INSERT INTO comercial (nombre, apellido1, apellido2, comisión) 
							VALUES  (     ?,         ?,         ?,       ?)
						   """;

		KeyHolder keyHolder = new GeneratedKeyHolder();
		//Con recuperación de id generado
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setString(idx++, comercial.getNombre());
			ps.setString(idx++, comercial.getApellido1());
			ps.setString(idx++, comercial.getApellido2());
			ps.setFloat(idx++, comercial.getComisión());
			return ps;
		},keyHolder);

		comercial.setId(keyHolder.getKey().intValue());

		//Sin recuperación de id generado
//		int rows = jdbcTemplate.update(sqlInsert,
//							cliente.getNombre(),
//							cliente.getApellido1(),
//							cliente.getApellido2(),
//							cliente.getCiudad(),
//							cliente.getCategoria()
//					);

		log.info("Insertados {} registros.", rows);
	}

	/**
	 * Devuelve lista con todos loa Clientes.
	 */
	@Override
	public List<Comercial> getAll() {

		List<Comercial> listFab = jdbcTemplate.query(
				"SELECT * FROM comercial",
				(rs, rowNum) -> new Comercial(rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getFloat("comisión")
				)
		);

		log.info("Devueltos {} registros.", listFab.size());

		return listFab;

	}

	/**
	 * Devuelve Optional de Cliente con el ID dado.
	 */
	@Override
	public Optional<Comercial> find(int id) {

		Comercial fab =  jdbcTemplate
				.queryForObject("SELECT * FROM comercial WHERE id = ?"
						, (rs, rowNum) -> new Comercial(rs.getInt("id"),
								rs.getString("nombre"),
								rs.getString("apellido1"),
								rs.getString("apellido2"),
								rs.getFloat("comisión"))
						, id
				);

		if (fab != null) {
			return Optional.of(fab);}
		else {
			log.info("Comercial no encontrado.");
			return Optional.empty(); }

	}


	/**
	 * Actualiza Cliente con campos del bean Cliente según ID del mismo.
	 */
	@Override
	public void update(Comercial comercial) {

		int rows = jdbcTemplate.update("""
										UPDATE comercial SET 
														nombre = ?, 
														apellido1 = ?, 
														apellido2 = ?,
														comisión = ?,
												WHERE id = ?
										"""
				, comercial.getNombre()
				, comercial.getApellido1()
				, comercial.getApellido2()
				, comercial.getComisión()
				, comercial.getId());

		log.info("Update de Comercial con {} registros actualizados.", rows);

	}

	/**
	 * Borra Cliente con ID proporcionado.
	 */
	@Override
	public void delete(long id) {

		int rows = jdbcTemplate.update("DELETE FROM comercial WHERE id = ?", id);

		log.info("Delete de Cliente con {} registros eliminados.", rows);

	}

}
