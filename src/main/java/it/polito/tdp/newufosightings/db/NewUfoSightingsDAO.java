package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Adiacenza;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

/*	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	} */

	public void loadAllStates(Map<String, State> idMap) {
		String sql = "SELECT * FROM state";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!idMap.containsKey(rs.getString("id"))) {
					State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
							rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
							rs.getString("Neighbors"));
					idMap.put(rs.getString("id"), state);
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<String> getShapesByYear(int anno) {
		String sql = "SELECT DISTINCT shape FROM sighting WHERE YEAR(datetime) = ? AND shape <> \"\" ORDER BY shape";
		List<String> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getString("shape"));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Adiacenza> getAdiacenze(Map<String, State> idMap, int anno, String forma) {
		String sql = "SELECT state1, state2, COUNT(s1.shape) AS peso FROM neighbor, sighting s1, sighting s2 WHERE s1.state = state1 AND s2.state = state2 AND s1.shape = s2.shape AND s1.shape = ? AND YEAR(s1.datetime) = YEAR(s2.datetime) AND YEAR(s1.datetime) = ? AND state1 < state2 GROUP BY state1, state2";
		List<Adiacenza> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, forma);
			st.setInt(2, anno);
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Adiacenza a = new Adiacenza(idMap.get(rs.getString("state1")), idMap.get(rs.getString("state2")), rs.getInt("peso"));
				result.add(a);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Sighting> loadSightingsByYearAndShape(int anno, String forma, Map<String, State> idMap) {
		String sql = "SELECT * FROM sighting WHERE YEAR(datetime) = ? AND shape = ?";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2, forma);
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), idMap.get(res.getString("state").toUpperCase()), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

}

