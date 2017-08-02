package com.landry.aws.lambda.duedate.model;

import java.util.List;

import javax.annotation.PostConstruct;


public class VendorShipTimeDataBeansNoCache
{
	private VendorShipTimeDataBeanBuilder builder;
	List<VendorShipTimeDataBean> vendorShipTimes;

	public VendorShipTimeDataBeansNoCache() {
	        builder = new VendorShipTimeDataBeanBuilder();
	}

	@PostConstruct
	private void init() throws Exception {
		getVendorShipTimes();
	}

	public List<VendorShipTimeDataBean> getVendorShipTimes() throws Exception
	{
		this.vendorShipTimes = builder.getVendorShipTimes();
		System.out.println("\nThe size is :" + vendorShipTimes.size());

		return vendorShipTimes;
	}

}
