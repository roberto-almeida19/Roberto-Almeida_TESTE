package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoUtil {

	public static Connection getConexao() throws ClassNotFoundException, SQLException {
		Class.forName(Constantes.CONEXAO_BD_DRIVER);

		return DriverManager.getConnection(Constantes.CONEXAO_BD_URL,
				Constantes.CONEXAO_BD_USER, Constantes.CONEXAO_BD_PASSWORD);

	}

	public static void main(String[] args) {
		try {
			System.out.println(getConexao());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
