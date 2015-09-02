package child.yasite.net.searchmychild.entity;

import java.io.Serializable;

public class AddressEntitiy implements Serializable {
	
	
	private Long _id;
	private String name;
	private String token;
	private String pinyin;
	
	public static final int SECTION = 1;
	public static final int UP_SETION = 2;
	public int type = SECTION;
	
	
	public Long get_id() {
		return _id;
	}
	public void set_id(Long _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	
	
	
}
