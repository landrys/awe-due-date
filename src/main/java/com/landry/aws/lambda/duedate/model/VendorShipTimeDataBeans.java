package com.landry.aws.lambda.duedate.model;

import java.util.List;

import javax.annotation.PostConstruct;


public class VendorShipTimeDataBeans
{
	private static VendorShipTimeDataBeans instance;
	private VendorShipTimeDataBeanBuilder builder;
	List<VendorShipTimeDataBean> vendorShipTimes;
	private boolean reloading = false;

	private VendorShipTimeDataBeans() {
	        builder = new VendorShipTimeDataBeanBuilder();
	}

	public static VendorShipTimeDataBeans instance()
	{
		if (instance == null)
		{
			synchronized (VendorShipTimeDataBeans.class)
			{
				if (instance == null)
					instance = new VendorShipTimeDataBeans();
			}
		}

		return instance;
	}

	@PostConstruct
	private void init() throws Exception {
		getVendorShipTimes();
	}

	public List<VendorShipTimeDataBean> getVendorShipTimes() throws Exception
	{
		if ( !reloading && vendorShipTimes == null ) {
			this.reloading = true;
			this.vendorShipTimes = builder.getVendorShipTimes();
		    System.out.println( "\nThe size is :" + vendorShipTimes.size());
			this.reloading = false;
		}

		return vendorShipTimes;
	}

	// Called from  do not know yet...
	public void reload() throws Exception {
		this.vendorShipTimes = null;
		getVendorShipTimes();
	}

}
