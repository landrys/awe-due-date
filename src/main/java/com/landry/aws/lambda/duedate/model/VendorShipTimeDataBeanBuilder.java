package com.landry.aws.lambda.duedate.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalTime;

import com.landry.aws.lambda.dynamo.dao.DynamoVendorShipTimeDAO;
import com.landry.aws.lambda.dynamo.dao.DynamoVendorShipTimeSupportDAO;
import com.landry.aws.lambda.dynamo.domain.VendorShipTime;
import com.landry.aws.lambda.dynamo.domain.VendorShipTimeSupport;


public class VendorShipTimeDataBeanBuilder
{

	private static final DynamoVendorShipTimeSupportDAO vstsDao = DynamoVendorShipTimeSupportDAO.instance();
	private static final DynamoVendorShipTimeDAO vstDao = DynamoVendorShipTimeDAO.instance();
	//@Autowired
	//VendorRepository vendorRepository;
    //private List<Vendor> archivedVendors;

	public VendorShipTimeDataBeanBuilder()
	{
		super();
	}

	private List<VendorShipTime> vendorShipTimes;
	private Integer defaultBusinessDays = 5; 
	private Integer defaultShippingDays = 5;
	private String defaultCutOffTime = "12:00";
	private String defaultVendorName = "VendorName";

	public synchronized List<VendorShipTimeDataBean> getVendorShipTimes() throws Exception
	{
		loadConfigData();
		loadVendorShipTimes();
		return buildDueDatesFromVendorShipTimes();
	}

	private List<VendorShipTimeDataBean> buildDueDatesFromVendorShipTimes()
	{
		List<VendorShipTimeDataBean> dueDates = new ArrayList<VendorShipTimeDataBean>();
		int i = 0;
		for (VendorShipTime row : vendorShipTimes)
		{
			if (i != 0)
			{
				VendorShipTimeDataBean vst = new VendorShipTimeDataBean.Builder().id(row.getId())
						.vendorId(row.getVendorId()==null ? BigInteger.ZERO:BigInteger.valueOf(row.getVendorId()))
						.vendorName(row.getName() == null ? defaultVendorName: row.getName())
						.warehouse(row.getWarehouse() == null ? "" : row.getWarehouse())
						.shippingDays((row.getShippingDays()==null ? defaultShippingDays : row.getShippingDays()))
						.businessDays(row.getLeadBusinessDays() == null ? defaultBusinessDays : row.getLeadBusinessDays())
						.shippingCarrier(row.getShippingCarrier() == null ? "" : row.getShippingCarrier())
						.bike(row.getIsBike() == null ? false : row.getIsBike())
						.weeklyOrder(row.getWeeklyOrder() == null ? false : row.getWeeklyOrder())

						.orderDays(row.getRegularOrderDays() == null ? null
								: getOrderDaysArray(row.getRegularOrderDays()))

						.cutOffTime(row.getCutOffTime() == null ? getLocalTime(defaultCutOffTime) : getLocalTime(row.getCutOffTime()))
						.dropShipToStore(row.getDropShipToStore() == null ? false
								: row.getDropShipToStore())
						.build();
				dueDates.add(vst);
			}
			i++;
		}

		return dueDates;
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

	private void loadVendorShipTimes() throws Exception
	{

		/*
		GentVendorShipTimesInvoker service = LambdaInvokerFactory.builder()
				.lambdaClient(AWSLambdaClientBuilder.defaultClient()).build(GentVendorShipTimesInvoker.class);
		vendorShipTimes = service.getVendorShipTimes("");
		*/
		vendorShipTimes = vstDao.findAll();
	}

	private void loadConfigData()
	{
		//GentVendorShipTimeSupportsInvoker service = LambdaInvokerFactory.builder()
		//		.lambdaClient(AWSLambdaClientBuilder.defaultClient()).build(GentVendorShipTimeSupportsInvoker.class);
		//Set<VendorShipTimeSupport> vendorShipTimeSupports = service.getVendorShipTimeSupports("");
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
