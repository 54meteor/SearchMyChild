package child.yasite.net.searchmychild.model;

import java.util.List;

import android.content.Context;

import child.yasite.net.searchmychild.entity.AddressEntitiy;
import child.yasite.net.searchmychild.service.AddressService;

public class AddressModel {
	
	Context context;
	AddressService addressService;
	
	public AddressModel(Context context){
		this.context = context;
		addressService = new AddressService(context);
	}
	
	public long addAddress(AddressEntitiy entity){
		return addressService.addAddress(entity);
	}

	public void updateAddress(AddressEntitiy entity){
		addressService.updateAddress(entity);
	}
	public void delAddress(AddressEntitiy entity){
		addressService.delAddress(entity);
	}
	public List<AddressEntitiy> getAddressList(){
		return addressService.getAddressList();
	}
	public AddressEntitiy getAddressInfo(long id){
		return addressService.getAddressInfo(id);
	}
}
