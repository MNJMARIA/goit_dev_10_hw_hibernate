package planet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanetCrudService implements IPlanetCrudService{
    private PreparedStatement createSt;
    private PreparedStatement getByIdSt;

    private PreparedStatement getAllSt;

    private PreparedStatement deleteSt;

    public PlanetCrudService(Connection connection){
        try{
            createSt = connection
                    .prepareStatement("INSERT INTO planet (id, name) VALUES (?, ?)");

            deleteSt = connection
                    .prepareStatement("DELETE FROM planet WHERE id = ?");

            getByIdSt = connection
                    .prepareStatement("SELECT id, name FROm planet WHERE id = ?");

            getAllSt = connection.prepareStatement("SELECT id, name FROM planet");

        }catch(SQLException e){
            throw new RuntimeException (e);
        }
    }

    @Override
    public void create(Planet planet){
        try{
            createSt.setString(1, planet.getId());
            createSt.setString(2, planet.getName());

            createSt.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException (e);
        }
    }

    @Override
    public String getById(String id){
        try{
            getByIdSt.setString(1, id);

            try(ResultSet rs = getByIdSt.executeQuery()){
                if(!rs.next()){
                    return null;
                }
                String name = rs.getString("name");
                return name;
            }
        } catch(SQLException e){
            throw new RuntimeException (e);
        }
    }

    @Override
    public List<Planet> getAll() {
        try(ResultSet rs = getAllSt.executeQuery()){
            List<Planet> planets = new ArrayList<>();

            while(rs.next()){
                Planet planet = new Planet();
                planet.setId(rs.getString("id"));
                planet.setName(rs.getString("name"));

                planets.add(planet);
            }
            return planets;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id){
        try{
            deleteSt.setString(1, id);

            deleteSt.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException (e);
        }
    }
}
