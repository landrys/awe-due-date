package com.landry.aws.lambda.duedate.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalTime;

import com.landry.aws.lambda.dynamo.dao.DynamoVendorShipTimeDAO;
import com.landry.aws.lambda.dynamo.dao.DynamoVendorShipTimeSupportDAO;
import com.landry.aws.lambda.dynamo.domain.VendorShipTime;
import com.landry.aws.lambda.dynamo.domain.VendorShipTimeSupport;


public class VendorShipTimeDataBeanBuilder2
{

	private static final DynamoVendorShipTimeSupportDAO vstsDao = DynamoVendorShipTimeSupportDAO.instance();
	private static final DynamoVendorShipTimeDAO vstDao = DynamoVendorShipTimeDAO.instance();

	public VendorShipTimeDataBeanBuilder2(Integer id)
	{
		super();
		this.id = id;
	}

	private VendorShipTime vendorShipTime;
	private Integer id;
	private Integer defaultBusinessDays = 5; 
	private Integer defaultShippingDays = 5;
	private String defaultCutOffTime = "12:00";
	private String defaultVendorName = "VendorName";

	public synchronized VendorShipTimeDataBean getVendorShipTime() throws Exception
	{
		loadConfigData();
		loadVendorShipTime();
		return buildDueDateFromVendorShipTime();
	}

	private VendorShipTimeDataBean buildDueDateFromVendorShipTime()
	{
		VendorShipTimeDataBean vst = new VendorShipTimeDataBean.Builder().id(vendorShipTime.getId())
				.vendorId(vendorShipTime.getVendorId() == null ? BigInteger.ZERO : BigInteger.valueOf(vendorShipTime.getVendorId()))
				.vendorName(vendorShipTime.getName() == null ? defaultVendorName : vendorShipTime.getName())
				.warehouse(vendorShipTime.getWarehouse() == null ? "" : vendorShipTime.getWarehouse())
				.shippingDays((vendorShipTime.getShippingDays() == null ? defaultShippingDays : vendorShipTime.getShippingDays()))
				.businessDays(vendorShipTime.getLeadBusinessDays() == null ? defaultBusinessDays : vendorShipTime.getLeadBusinessDays())
				.shippingCarrier(vendorShipTime.getShippingCarrier() == null ? "" : vendorShipTime.getShippingCarrier())
				.bike(vendorShipTime.getIsBike() == null ? false : vendorShipTime.getIsBike())
				.weeklyOrder(vendorShipTime.getWeeklyOrder() == null ? false : vendorShipTime.getWeeklyOrder())

				.orderDays(vendorShipTime.getRegularOrderDays() == null ? null : getOrderDaysArray(vendorShipTime.getRegularOrderDays()))

				.cutOffTime(vendorShipTime.getCutOffTime() == null ? getLocalTime(defaultCutOffTime)
						: getLocalTime(vendorShipTime.getCutOffTime()))
				.dropShipToStore(vendorShipTime.getDropShipToStore() == null ? false : vendorShipTime.getDropShipToStore()).build();

		return vst;
	}

	private LocalTime getLocalTime( String string )
	{
		if (string.isEmpty())
			return null;
		String[] timeStrings = string.split(":");
		if (timeStrings[0].equals("00"))
			timeStrings[0] = "0";
		if (timeStrings[1].equals("00"))
			timeStrings[1] = "0";
		Integer[] time = { Integer.parseInt(timeStrings[0]), Integer.parseInt(timeStrings[1]) };

		return new LocalTime(time[0], time[1]);
	}

	private Integer[] getOrderDaysArray( Set<Integer> set )
	{
		Integer[] arr = set.toArray(new Integer[set.size()]);
		Arrays.sort(arr);
		return arr;
	}

	private void loadVendorShipTime() throws Exception
	{
		vendorShipTime = vstDao.findById(id);
		 if ( vendorShipTime == null )
			 throw new Exception("No VendorShipTime entry foudn with id: " + id);
	}

	private void loadConfigData()
	{
		List<VendorShipTimeSupport> vendorShipTimeSupports = vstsDao.findAll();
		VendorShipTimeSupport defaultSupport = null;
		Iterator<VendorShipTimeSupport> it = vendorShipTimeSupports.iterator();
		while (it.hasNext())
		{
			defaultSupport = it.next();
			if (defaultSupport.getSupport().equalsIgnoreCase(VendorShipTimeSupport.DEFAULT_SUPPORT))
				break;
		}

		if (defaultSupport != null)
		{
			defaultBusinessDays = defaultSupport.getLeadBusinessDays() == null ? defaultBusinessDays
					: defaultSupport.getLeadBusinessDays();
			defaultCutOffTime = defaultSupport.getCutOffTime() == null ? defaultCutOffTime
					: defaultSupport.getCutOffTime();
			defaultShippingDays = defaultSupport.getShippingDays() == null ? defaultShippingDays
					: defaultSupport.getShippingDays();
		}		

	}

}
