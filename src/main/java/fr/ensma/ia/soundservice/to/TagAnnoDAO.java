package fr.ensma.ia.soundservice.to;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import fr.ensma.ia.soundservice.util.JDBCUtil;
import fr.ensma.ia.soundservice.util.ServerConfig;

public class TagAnnoDAO {

	private static final Logger logger = LogManager.getLogger(TagAnnoDAO.class);
	private static fr.ensma.ia.soundservice.util.ServerConfig cfg = ConfigCache.getOrCreate(ServerConfig.class);

	
	private final String tableAnnoName;
	private final String tableTagPersoName;
	private final String tableTagsName;
	
	
	private Connection conn = null;
	private PreparedStatement ps = null;
	private Statement st = null;
	private ResultSet rs = null;
	
	public TagAnnoDAO() {
		tableAnnoName = cfg.pgTableAnnotations();
		tableTagPersoName = cfg.pgTableTagsPerso();
		tableTagsName = cfg.pgTableTags();
		
	}
	
	public void addTagAnnotation(Annotation tann) {
		
		
		List<Tag> listTags = tann.getTags();
		if(listTags==null||listTags.isEmpty()) {
			logger.info("listTags is null, couldn't add Tag or Annotation");
			return;
		} else {
			conn = JDBCUtil.getPostgreConn();
			
			//insert into TagPerso
			List<Tag> listTagPerso = getAndAddTagPerso(tann.getTags());
			
			//insert into annotation
			for(Tag tag : tann.getTags()) {
				try {
					if(listTagPerso.contains(tag)) {
						ps = conn.prepareStatement("insert into "+tableAnnoName+" (date,tagperso,author,sound) values(?,?,?,?);");
					} else {
						ps = conn.prepareStatement("insert into "+tableAnnoName+" (date,tag,author,sound) values(?,?,?,?);");
					}
					ps.setObject(1, new Date());
					ps.setObject(2, tag.getName());
					ps.setObject(3, tann.getAuthor());
					ps.setObject(4, tann.getIdSound());
					ps.execute();
					logger.debug(tann.toString() + "(tag:"+ tag + ") has been inserted into "+ tableAnnoName);
					
				} catch (SQLException e) {
					logger.error("could not insert "+tann.toString()+" into "+tableAnnoName, e);
				} finally {
					JDBCUtil.close(ps);
				}
			} 
			JDBCUtil.close(conn);
		}
		
	}


	private List<Tag> getAndAddTagPerso(List<Tag> list) {

		if(list!=null && !list.isEmpty()) {
			
			List<Tag> listIdTagPerso = new ArrayList<Tag>();
			
			for(Tag tag : list) {
				
				try {
					st = conn.createStatement();
					
					//check if the tag exists in the table Tags
					String query = "select coalesce((select id_tag from "+tableTagsName+" where id_tag='"+tag.getName()+"'),'empty') as tags_exist;";
					rs = st.executeQuery(query);
					String tagsExist = null;
					while(rs.next()) {
						tagsExist = (String) rs.getObject("tags_exist");
					}
					
					if(tagsExist==null||tagsExist.equals("empty")) {
						//add TagsPerso dans la table Tagsperso
						addTagsPers(tag);
						listIdTagPerso.add(tag);
					} else {
						logger.debug(tag.toString()+" already exists in the table "+tableTagsName);
					}
					
				} catch (SQLException e) {
					logger.error(e);
					
				} finally {
					JDBCUtil.close(rs,st);
				}
			}
			
			return listIdTagPerso;
		} else {
			logger.info("list tag is null or empty");
			return null;
		}
	}
	
	


	private void addTagsPers(Tag tag) {
		
		try {
			Statement st2 = conn.createStatement();
			String query = "select coalesce((select id_tag from "+tableTagPersoName+" where id_tag='"+tag.getName()+"'),'empty') as tags_exist;";
			ResultSet rs2 = st2.executeQuery(query);
			
			String tagsExist = null;
			while(rs2.next()) {
				tagsExist = (String) rs2.getObject("tags_exist");
			}
			
			if(tagsExist==null||tagsExist.equals("empty")) {
				ps = conn.prepareStatement("insert into "+tableTagPersoName+"  (id_tag) values(?);");
				ps.setObject(1, tag.getName());
				ps.execute();
				logger.debug(tag.toString() + " has been inserted into "+tableTagPersoName);
				JDBCUtil.close(ps);
			}

		} catch (SQLException e) {
			logger.error("could not insert "+tag.toString()+" into "+tableTagPersoName, e);
		} finally {
			JDBCUtil.close(rs, st);
		}
	}


}