package fr.ensma.ia.soundservice.tao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.ensma.ia.soundservice.to.Sound;
import fr.ensma.ia.soundservice.to.Tag;
import fr.ensma.ia.soundservice.util.JDBCUtil;
import fr.ensma.ia.soundservice.util.ServerConfig;

public class SoundDAO implements ISoundDAO {

	
	private static SoundDAO dao;
	
	private static final Logger logger = LogManager.getLogger(SoundDAO.class);
	private static fr.ensma.ia.soundservice.util.ServerConfig cfg = ConfigCache.getOrCreate(ServerConfig.class);
	
	private static int NUM_TAGS = 3;

	
	private final String tableSound;
	private final String tableAnno;
	private final String tableUser;
	
	
	private Connection conn = null;
	private Statement st = null;
	private ResultSet rs = null;
	
	
	public static ISoundDAO getInstance() {
		if (dao == null) {
			dao = new SoundDAO();
		}
		return dao;
	}
	
	public SoundDAO() {
		
		tableAnno = cfg.pgTableAnnotations();
		tableUser = cfg.pgTableUsers();
		tableSound = cfg.pgTableSoundfiles();
				
	}
	
	
	
	
	
	
	private List<Tag> getTagsPertinent(Sound sound) {
		
		
		Statement st2 = null;
		ResultSet rs2 = null;
	
		try {
			st2 = conn.createStatement();
			String query = "SELECT tagperso, SUM(credibility_mark) AS pertinence\n" + 
					"FROM "+tableAnno+" INNER JOIN "+tableUser+ " ON "+tableAnno+".author="+tableUser+".id_user\n" + 
					"WHERE sound = '"+sound.getIdSound()+"'\n" + 
					"GROUP BY tagperso\n" + 
					"ORDER BY pertinence DESC;";
			
			rs2 = st2.executeQuery(query);
			
			List<Tag> listtags = new ArrayList<Tag>();
			
//			System.out.println("get tags!!!!");
			
			while(rs2.next()) {
				
				String tagId = rs2.getString("tagperso");
//				System.out.println("!!!!!!!!!!!!!"+tagId);
				
				if(tagId!=null) {
					
					Tag tag = new Tag(tagId);
					
//					System.out.println("!!!!!!!!!!!!!"+tag.toString());
					
					listtags.add(tag);
				}
			}
			
			if(listtags.size()<=NUM_TAGS) {
				return listtags;
			} else {
				return listtags.subList(0, NUM_TAGS-1);
			}
			
		} catch (SQLException e) {
			logger.info("could not get tag information of the sound "+sound.toString(),e);
		} finally {
			JDBCUtil.close(rs2,st2);
		}
		
		return null;
		
	}

	@Override
	public List<Sound> getListSound() {
		
		conn = JDBCUtil.getPostgreConn();
		
		try {
			st = conn.createStatement();
			String query = "SELECT id_sound, url " 
					+ "FROM "+tableSound
					+ " LIMIT 10;";

			
			rs = st.executeQuery(query);
			
			List<Sound> listsound = new ArrayList<Sound>();
			
			while(rs.next()) {
				Sound sound = new Sound(rs.getString("id_sound"), rs.getString("url"));
				
				System.out.println(sound.toString());
				
				//get tags for this sound
				List<Tag> listtags = getTagsPertinent(sound);
				
//				for(Tag tag:listtags) {
//					System.out.println(tag);
//				}
				
				if(!(null==listtags||listtags.isEmpty())) {
					sound.setTags(listtags);
				}
				listsound.add(sound);
			}
			
			return listsound;
					
		} catch (SQLException e) {
			logger.info("could not get list sound",e);
		} finally {
			JDBCUtil.close(rs,st,conn);
		}
		
		return null;
	}

}
