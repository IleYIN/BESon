package fr.ensma.ia.soundservice.util;


import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;


@Sources("classpath:soundservice.properties")
public interface ServerConfig extends Config {

	//@Key("postgresql.driver")
	//String pgDriver();

	@Key("postgresql.ip")
	String pgAddress();

	@Key("postgresql.port")
	Integer pgPort();

	@Key("postgresql.db")
	String pgDatabase();

	@Key("postgresql.user")
	String pgUser();

	@Key("postgresql.password")
	String pgPassword();

	@Key("postgresql.tablename.tags")
	String pgTableTags();

	@Key("postgresql.tablename.tagsperso")
	String pgTableTagsPerso();
	
	@Key("postgresql.tablename.users")
	String pgTableUsers();
	
	@Key("postgresql.tablename.soundfiles")
	String pgTableSoundfiles();
	
	@Key("postgresql.tablename.annotations")
	String pgTableAnnotations();



}
