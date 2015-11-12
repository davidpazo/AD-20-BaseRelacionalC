package BaseRelacional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author oracle
 */
public class BaseRelacionalC {
    
    private PreparedStatement sentencia;
    private Connection conexion;
    String tabla = "productos";
    Statement st;
    ResultSet rs;
   

    public BaseRelacionalC conectar() {
        try {
            String BaseDeDatos = "jdbc:oracle:thin:@localhost:1521:orcl";
            conexion = DriverManager.getConnection(BaseDeDatos, "hr", "hr");
            if (conexion != null) {
                System.out.println("Conexion exitosa!");
            } else {
                System.out.println("Conexion fallida!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public void insirePrep(String cod, String desc, int prezo) {
        try {
            System.out.println(">>Metiendo nueva fila...");
            String consulta = "insert into productos values (?,?,?)";
            sentencia = conexion.prepareStatement(consulta);
            
            sentencia.setString(1, cod);
            sentencia.setString(2, desc);
            sentencia.setInt(3, prezo);
            sentencia.execute();
            System.out.println("Inserccion realizada");
        } catch (SQLException ex) {
            System.out.println("SQLException " + ex);
        }
    }
    public void actuPrep(String desc,int prezo, String cod) {
        try {
            String consulta = "UPDATE productos SET descricion = ?,prezo=? WHERE CODIGO=?";
            sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, desc);
            sentencia.setInt(2, prezo);
            sentencia.setString(3, cod);
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException" + ex);
        }
    }
    public void consultar() {
        try {
            System.out.println("Consultar tabla productos");
            String consulta = "Select * from productos";// o Select productos.* from productos;
            st = conexion.createStatement();
            rs = st.executeQuery(consulta);
            System.out.println("Listado de elementos en " + tabla + ": ");
            while (rs.next()) {
                String cod = rs.getString("codigo");
                String des = rs.getString("descricion");
                int prezo = rs.getInt("prezo");
                System.out.println(cod + " " + des + " " + prezo);
            }

        } catch (SQLException ex) {
            System.out.println("SQLException" + ex);
        }
    }
   
    private void Borrar() {
        try {
            String consulta = "delete from productos";
            st = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(consulta);
            System.out.println("Productos borrados");

        } catch (SQLException ex) {
            System.out.println("SQLException" + ex);
        }
    }
    public static void main(String[] args) {
            BaseRelacionalC br = new BaseRelacionalC().conectar();
            int opc;
            do {
            opc = JOptionPane.showOptionDialog(
                    null, "MENU", "Que desea hacer?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{
                        "Insertar",
                        "Actualizar",
                        "Consultar",
                        "Borrar",
                        "Salir"},
                    "Exit") + 1;

            switch (opc) {
                case 1:
                   br.insirePrep("p1","parafusos",3);
                   br.insirePrep("p2","cravos",4);
                   br.insirePrep("p3","tachas",6);
                    break;
                case 2:
                    br.actuPrep("paraguas",5,"p1");
                    
                    break;
                case 3:
                   br.consultar();
                    break;
                case 4:
                    br.Borrar();
                    break;
                case 5:
                   System.exit(0);
            }
        } while (opc != 0 && opc != 5);
                        
    }
}
