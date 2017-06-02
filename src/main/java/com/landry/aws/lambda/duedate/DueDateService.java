package com.landry.aws.lambda.duedate;

import java.util.Iterator;
import org.joda.time.DateTime;

import com.landry.aws.lambda.common.invoker.LCVendorAdapterInvoker;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.landry.aws.lambda.common.model.DueDateInput;
import com.landry.aws.lambda.common.model.DueDateOutput;
import com.landry.aws.lambda.common.model.LCVendorAdapterInput;
import com.landry.aws.lambda.common.util.MyDateUtil;
import com.landry.aws.lambda.duedate.model.VendorShipTimeDataBeans;

public class DueDateService
{

	private static DueDateService instance;

	private DueDateService()
	{
	}

	public static DueDateService instance()
	{

		if (instance == null)
		{
			synchronized (DueDateService.class)
			{
				if (instance == null)
					instance = new DueDateService();
			}
		}
		return instance;
	}

	public DueDateOutput getMeTheBestArrivalDate(DueDateInput dueDateInput) throws Exception
	{

		
		VendorShipTimeDataBeans vsdbs = VendorShipTimeDataBeans.instance();
		if (dueDateInput.getReload() != null && dueDateInput.getReload())
			return reload(vsdbs);
		
		DateTime startDate = null;
		DateTime arrivalDate = null;
		Iterator<Integer> it = dueDateInput.getVendorShipTimeIds().iterator();
		Integer usedShipTimeId = null;

		

		

		while (it.hasNext())
		{
			Integer shipTimeId = it.next();
			DueDateCalculator ddc = new DueDateCalculator.Builder().vendorShipTimeId(shipTimeId)
					.store(dueDateInput.getStore()).vendorShipTimeDataBeans(vsdbs).build();
			DateTime arrivalDate1 = ddc.getArrivalDate2();
			DateTime startDate1 = ddc.getStartDate();
			if (arrivalDate == null || arrivalDate1.isAfter(arrivalDate))
			{
				usedShipTimeId = shipTimeId;
				arrivalDate = arrivalDate1;
				startDate = startDate1;
			}
		}

		DueDateOutput calculatedArrivalDate = new DueDateOutput.Builder()
				.arrivalDate(MyDateUtil.toStringDate(arrivalDate, "MM-dd-yyyy"))
				.nextOrderDate(MyDateUtil.toStringDate(startDate, "MM-dd-yyyy")).dueDateInput(dueDateInput)
				.vendorShipTimeId(usedShipTimeId).info("Success").build();

		return calculatedArrivalDate;
	}

	private DueDateOutput reload(VendorShipTimeDataBeans vsdbs) throws Exception
	{
	    vsdbs.reload();
		return new DueDateOutput.Builder().info("Reloaded and built vendor ship time data beans.").build();
	}

}

		// call lcVendorAdapter from its own service from client. then call reload here.
	    //LCVendorAdapterInvoker checkForLCUpdatesService = LambdaInvokerFactory.builder()
	//		.lambdaClient(AWSLambdaClientBuilder.defaultClient()).build(LCVendorAdapterInvoker.class);
	 //   checkForLCUpdatesService.lcVendorAdapter(new LCVendorAdapterInput());