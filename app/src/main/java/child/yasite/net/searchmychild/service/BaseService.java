package child.yasite.net.searchmychild.service;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import child.yasite.net.searchmychild.dao.DaoMaster;
import child.yasite.net.searchmychild.dao.DaoSession;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.identityscope.IdentityScopeType;

public class BaseService {
	protected Context context;
	private DaoSession daoSession;
	private BaseService() {

	}
	
	public AbstractDao getDao(Class entity){
		return openSession().getDao(entity);
	}
	
	public DaoSession openSession(IdentityScopeType type) {
		daoSession = DaoMaster.getInstance(context).newSession(type);
		return daoSession;
	}
	
	public DaoSession openSession() {
		return openSession(IdentityScopeType.None);
	}

	public void closeSession() {
		if (daoSession != null) {
			if (daoSession.getDatabase() != null && daoSession.getDatabase().isOpen()) {
				daoSession.getDatabase().close();
			}
		}
		DaoMaster.clear();
	}
	

	public NameValuePair getValue(String key,String value){
		return new BasicNameValuePair(key, value);
	}
	
	public BaseService(Context context) {
		this.context = context;
	}

}
