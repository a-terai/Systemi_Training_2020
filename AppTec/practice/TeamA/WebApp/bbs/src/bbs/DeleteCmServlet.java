package bbs;

import static bbs.util.CloseableUtil.*;
import static bbs.util.DBUtil.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/deleteCm"})
public class DeleteCmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");

		int cbId = Integer.parseInt(request.getParameter("cbId"));

		Connection connection = null;
    	PreparedStatement ps = null;

		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.2.6:3306/test";
		String user = "testuser";
		String password = "test";

		try {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}

	      	connection = DriverManager.getConnection(url,user,password);
	      	connection.setAutoCommit(false);

	      	//コメントを削除するSQL文
	      	StringBuilder sql = new StringBuilder();
	      	sql.append("DELETE FROM comments");
	      	sql.append("WHERE contribution_id=");
	      	sql.append(cbId);

	      	//SQLの実行
	      	ps = connection.prepareStatement(sql.toString());
	      	ps.executeUpdate();

	      	commit(connection);
	      	request.getRequestDispatcher("/contributionIndex").forward(request, response);

			close(connection);

	      	} catch (RuntimeException | SQLException e) {
	    		rollback(connection);
	    	} catch (Error e) {
	    		rollback(connection);
	    		throw e;
	    	} finally {
	    		close(ps);
	    	}
	}
}
