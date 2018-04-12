package com.landry.aws.lambda.duedate;

import java.util.Iterator;

import org.joda.time.DateTime;

import com.landry.aws.lambda.businessday.BusinessDayService;
import com.landry.aws.lambda.common.util.MyDateUtil;
import com.landry.aws.lambda.duedate.model.VendorShipTimeDataBean;
import com.landry.aws.lambda.duedate.model.VendorShipTimeDataBeans;

public class DueDateCalculator
{
	VendorShipTimeDataBeans vendorShipTimeDataBeans;
	private DateTime arrivalDate;
	private DateTime startDate;
	private Integer vendorShipTimeId;
	private VendorShipTimeDataBean vendorShipTime;
	private String store;

	public DateTime getArrivalDate2() throws Exception
	{
		findVendorShipTime();
		calculateArrivalDate();
		return arrivalDate;
	}

	private void findVendorShipTime() throws Exception
	{
		Iterator<VendorShipTimeDataBean> it = vendorShipTimeDataBeans.getVendorShipTimes().iterator();
		while (it.hasNext())
		{
			VendorShipTimeDataBean bean = it.next();
			if (bean.getId().intValue() == vendorShipTimeId)
			{
				vendorShipTime = bean;
				return;
			}
		}
		throw new Exception("No vendorShipTime for vendorShipTimeId: " + vendorShipTimeId);

	}

	private void calculateArrivalDate()
	{
		if (vendorShipTime.isWeeklyOrder())
			startDate = getStartDateForWeeklyOrder();
		else
			// This can be a weekend day or holiday. The ArrivalDate stuff
			// will take care of moving it to a business day.
			startDate = getStartDateForRegularOrder();

		arrivalDate = BusinessDayService.moveForward(vendorShipTime.getBusinessDays(), startDate);


		if (!store.equalsIgnoreCase("Natick") && !vendorShipTime.isDropShipToStore())
			arrivalDate = BusinessDayService.moveForward(1, arrivalDate);

                if (vendorShipTime.isBike()) {
                    arrivalDate = BusinessDayService.moveForward(1, arrivalDate);
                    if (store.equalsIgnoreCase("Natick"))
                        arrivalDate = BusinessDayService.moveForward(1, arrivalDate);
                }
	}

	private DateTime getStartDateForWeeklyOrder()
	{
		WeeklyOrderStartDateCalculator wosdc = new WeeklyOrderStartDateCalculator.Builder()
				.vendorShipTime(vendorShipTime).startDate(DateTime.now()).build();
		return wosdc.getStartDate();
	}

	private DateTime getStartDateForRegularOrder()
	{
		return BusinessDayService.moveForward(vendorShipTime.getBusinessDays(),
				MyDateUtil.addDayToNowIfPastCutOffTime(vendorShipTime.getCutOffTime()));
	}

	public DateTime getStartDate()
	{
		return startDate;
	}

	public static class Builder
	{
		private VendorShipTimeDataBeans vendorShipTimeDataBeans;
		private Integer vendorShipTimeId;
		private String store;

		public Builder vendorShipTimeDataBeans( VendorShipTimeDataBeans vendorShipTimeDataBeans )
		{
			this.vendorShipTimeDataBeans = vendorShipTimeDataBeans;
			return this;
		}

		public Builder vendorShipTimeId( Integer vendorShipTimeId )
		{
			this.vendorShipTimeId = vendorShipTimeId;
			return this;
		}

		public Builder store( String store )
		{
			this.store = store;
			return this;
		}

		public DueDateCalculator build()
		{
			return new DueDateCalculator(this);
		}
	}

	private DueDateCalculator(Builder builder)
	{
		this.vendorShipTimeDataBeans = builder.vendorShipTimeDataBeans;
		this.vendorShipTimeId = builder.vendorShipTimeId;
		this.store = builder.store;
	}
}
/*
		Sheets service = LandryElevenSheetService.getSheetsService();
		//String spreadsheetId = "12tIQAoZ6SEn1hdu2GZf4R5aGJPzaCnK9kPvo-TY79EY";
		//String range = "ShipTime-DoNotUse";
		String spreadsheetId = "1Jvcogcj3Z7_V4hAr5debBKLr7cfW04Tbr5yQc0iOcHg";
		String range = "VendorShipTime";
		ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
		//System.out.println(response.toPrettyString());
		List<List<Object>> values = response.getValues();
		if (values == null || values.size() == 0)
		{
			System.out.println("No data found.");
		}
		else
		{
			System.out.println("Name, Major");
			for (List<Object> row : values)
			{
				// Print columns A and E, which correspond to indices 0 and
				// 4.
				System.out.printf("%s, %s\n", row.get(0), row.get(1));
				break;
			}
		}
*/
	/*
	{
		// TODO: Put in more here. For now just lets tr to get this to work
		final DateBusinessDaysOut service = LambdaInvokerFactory.builder()
				.lambdaClient(AWSLambdaClientBuilder.defaultClient()).build(DateBusinessDaysOut.class);
		BusinessDayInput input = new BusinessDayInput();
		input.setBusinessDays(3);
		input.setDate("04-17-2017");
		BusinessDayOutput out = service.dateBusinessDaysOut(input);
		arrivalDate = MyDateUtil.toDateTime(out.getDate(), "MM-dd-yyyy");
	
	}
	*/

